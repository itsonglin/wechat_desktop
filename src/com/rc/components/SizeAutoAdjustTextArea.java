package com.rc.components;

import com.rc.components.message.JIMSendTextPane;
import com.rc.utils.EmojiUtil;
import com.rc.utils.FontUtil;
import com.rc.utils.OSUtil;
import com.vdurmont.emoji.EmojiParser;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by song on 17-6-4.
 */
public class SizeAutoAdjustTextArea extends JIMSendTextPane
{
    private final FontMetrics fontMetrics;
    private String[] lineArr;
    private int maxWidth;
    private Object tag;
    private Pattern emojiPattern;

    private String emojiRegx;
    private int emojiSize = 20;

    private boolean parseUrl = false;


    // 最长一行是第几行
    private int maxLengthLinePosition = 0;

    // 所有的url地址
    private List<String> urlList;

    // 记录每个url地址的起始位置与结束位置
    int[][] urlRange;


    public SizeAutoAdjustTextArea(int maxWidth)
    {
        this.maxWidth = maxWidth;
        setOpaque(false);
        //setLineWrap(true);
        //setWrapStyleWord(false);
        this.setFont(FontUtil.getDefaultFont(14));
        setEditable(false);

        emojiRegx = ":.+?:";
        emojiPattern = Pattern.compile(emojiRegx);// 懒惰匹配，最小匹配
        fontMetrics = getFontMetrics(getFont());

        setListeners();
    }


    @Override
    public void setText(String t)
    {
        // 对emoji的Unicode编码转别名
        t = EmojiParser.parseToAliases(t);

        if (t == null)
        {
            return;
        }

        // 总行数
        int lineCount = parseLineCount(t);

        // 每一行的emoji表情信息
        List<LineEmojiInfo> lineEmojiInfoList = parseLineEmojiInfo();

        // 每一行的实际宽度，即插入表情后的宽度
        int[] lineWidthArr = parseLineActualWidth(lineEmojiInfoList);

        int lineHeight = fontMetrics.getHeight();
        int targetHeight = lineHeight * lineCount;
        int targetWidth = 20;


        if (lineCount > 0)
        {
            targetWidth = lineWidthArr[maxLengthLinePosition] + 10;
        }
        // 输入全为\n的情况
        else
        {
            targetHeight = lineHeight;
            t = " ";
        }

        int contentWidth = maxWidth - 10;
        // 如果最长的一行宽度超过了最大宽度，就要重新计算高度
        if (targetWidth > maxWidth)
        {
            targetWidth = maxWidth;

            int totalLine = 0;
            for (int lineWidth : lineWidthArr)
            {
                int ret = lineWidth / contentWidth;
                int l = ret == 0 ? ret : ret + 1;
                totalLine += l == 0 ? 1 : l;
            }

            targetHeight = lineHeight * totalLine;
        }


        String targetText = t.replaceAll(emojiRegx, "");
        super.setText(targetText);

        /*long start = System.currentTimeMillis();
        Map<Integer, String> emojiPositionMap = insertEmoji(t);
        System.out.println("花费时间 ：" + (System.currentTimeMillis() - start));*/

        // 插入emoji表情，并计算需要增加的高度
        Map<Integer, String> emojiPositionMap = insertEmoji(t);
        String exceptEmoji = t.replaceAll("\\r\\n", "\n");
        for (String emoji : emojiPositionMap.values())
        {
            exceptEmoji = exceptEmoji.replace(emoji, "\0");
        }
        int emojiCount = emojiPositionMap.size();

        //全是emoji的情况
        if (exceptEmoji.matches("\0+"))
        {
            int totalWidth = emojiCount * emojiSize;
            targetHeight = emojiSize;
            if (totalWidth > maxWidth)
            {
                targetWidth = maxWidth;
                int ret = totalWidth / maxWidth;
                int l = ret == 0 ? ret : ret + 1;
                targetHeight = l * emojiSize;
            }

            int h = OSUtil.getOsType() == OSUtil.Mac_OS ? 0 : 3;
            setPreferredSize(new Dimension(targetWidth, targetHeight + h));
            return;
        }

        int emojiExtraHeight = OSUtil.getOsType() == OSUtil.Mac_OS ? 8 : 5;
        int emojiIndex = 1;
        for (int pos : emojiPositionMap.keySet())
        {
            String substr = exceptEmoji.substring(0, pos + 1);
            int width = fontMetrics.stringWidth(substr) + emojiSize;
            if (width > maxWidth || substr.indexOf("\n") > -1)
            {
                targetHeight += emojiExtraHeight;
                break;
            }
            emojiIndex++;
        }
        if (emojiIndex < emojiCount)
        {
            targetHeight += (emojiCount - emojiIndex) * emojiExtraHeight;
        }

        // 如果有emoji表情，高度就要适当增加
        if (emojiCount > 0)
        {
            targetHeight += emojiExtraHeight;
        }

        this.setPreferredSize(new Dimension(targetWidth, targetHeight + 2));

        if (parseUrl)
        {
            // 解析网址
            highlightUrls(t);
        }

    }

    /**
     * 网址高亮
     *
     * @param src
     */
    private void highlightUrls(String src)
    {
        urlList = parseUrl(src);
        urlRange = new int[urlList.size()][2];

        SimpleAttributeSet bSet = new SimpleAttributeSet();
        StyleConstants.setForeground(bSet, Color.blue);
        StyleConstants.setUnderline(bSet, true);
        StyledDocument doc = getStyledDocument();
        doc.setCharacterAttributes(0, src.length(), getCharacterAttributes(), true);

        int startIndex = 0;
        int endIndex = 0;
        for (int i = 0; i < urlList.size(); i++)
        {
            String url = urlList.get(i);
            startIndex = src.indexOf(url);
            if (startIndex > -1)
            {
                endIndex = startIndex + url.length();
                doc.setCharacterAttributes(src.indexOf(url, startIndex), url.length(), bSet, false);

                urlRange[i][0] = startIndex;
                urlRange[i][1] = endIndex;

                startIndex++;
            }
        }
    }

    private Map<Integer, String> insertEmoji(String src)
    {
        src = src.replaceAll("\\r\\n", "\n");
        Document doc = getDocument();

        // 这里要求key是按位置升序排列的，所有使用TreeMap
        Map<Integer, String> retMap = new TreeMap<>();
        StringBuilder stringBuilder = new StringBuilder();

        char[] charArr = src.toCharArray();
        char ch;
        boolean emojiStart = false;
        int pos = -1;
        for (int i = 0; i < charArr.length; i++)
        {
            ch = charArr[i];
            if (ch == ':')
            {
                if (!emojiStart)
                {
                    emojiStart = true;
                    stringBuilder.append(ch);
                    pos = i;
                }
                else
                {
                    emojiStart = false;
                    stringBuilder.append(ch);

                    setCaretPosition(pos);


                    Icon icon = EmojiUtil.getEmoji(this, stringBuilder.toString());

                    if (icon != null)
                    {
                        retMap.put(pos, stringBuilder.toString());

                        insertIcon(icon);

                        charArr = resetCharArr(new String(charArr), stringBuilder.toString());
                        i = pos;
                        stringBuilder.setLength(0);
                    }
                    else
                    {
                        // 表情不存在，原样输出
                        try
                        {
                            doc.insertString(pos, stringBuilder.toString(), getCharacterAttributes());
                        }
                        catch (BadLocationException e)
                        {
                            e.printStackTrace();
                        }
                        stringBuilder.setLength(0);

                    }
                }
            }
            else
            {
                if (emojiStart)
                {
                    stringBuilder.append(ch);
                }
            }

        }

        return retMap;
    }

    private char[] resetCharArr(String src, String s)
    {
        String str = src.replaceFirst(s, "#");
        return str.toCharArray();
    }


    /**
     * 分析每一行的emoji数量
     *
     * @return
     */
    private List<LineEmojiInfo> parseLineEmojiInfo()
    {
        List<LineEmojiInfo> infoList = new ArrayList<>(lineArr.length);
        List<String> emojiList;
        LineEmojiInfo info;
        for (int i = 0; i < lineArr.length; i++)
        {
            emojiList = parseEmoji(lineArr[i]);
            info = new LineEmojiInfo(emojiList.size(), emojiList);
            infoList.add(info);
        }


        return infoList;
    }

    /**
     * 提取字符串中的所有emoji表情编码
     *
     * @param src
     * @return
     */
    private List<String> parseEmoji(String src)
    {
        List<String> emojiList = new ArrayList<>();

        Matcher emojiMatcher = emojiPattern.matcher(src);

        while (emojiMatcher.find())
        {
            String code = emojiMatcher.group();
            if (EmojiUtil.isRecognizableEmoji(this, code))
            {
                emojiList.add(code);
            }
        }

        return emojiList;
    }


    /**
     * 分析消息文本一共有几行
     *
     * @param text 消息文本
     * @return 消息的行数，以\n分隔
     */
    private int parseLineCount(String text)
    {
        lineArr = text.split("\\n");

        return lineArr.length;
    }


    /**
     * 解析每一行的实际宽度，包括字符串宽度和表情宽度
     *
     * @param lineEmojiInfoList
     * @return
     */
    private int[] parseLineActualWidth(List<LineEmojiInfo> lineEmojiInfoList)
    {
        String[] lineArrCopy = lineArr.clone();
        int[] retArr = new int[lineArrCopy.length];

        int maxLength = 0;
        maxLengthLinePosition = 0;

        for (int i = 0; i < lineArrCopy.length; i++)
        {
            //String exceptEmoji = lineArrCopy[i].replaceAll(emojiRegx, "");
            String exceptEmoji = lineArrCopy[i];

            List<String> emojiList = lineEmojiInfoList.get(i).getEmojiList();
            for (String emoji : emojiList)
            {
                exceptEmoji = exceptEmoji.replace(emoji, "");
            }

            int width = fontMetrics.stringWidth(exceptEmoji);
            width += lineEmojiInfoList.get(i).getCount() * emojiSize;
            retArr[i] = width;

            if (width > maxLength)
            {
                maxLength = width;
                maxLengthLinePosition = i;
            }
        }

        return retArr;
    }

    private List<String> parseUrl(String src)
    {
        List<String> urlList = new ArrayList<>();


        //long start = System.currentTimeMillis();
        String regex = "(?:https?://)?(www\\.)?[\\w]+(?:\\.[\\w]+)+[\\w,\\-_/?&=#%.:]*";
        Pattern urlPattern = Pattern.compile(regex);
        Matcher urlMatcher = urlPattern.matcher(src);
        while (urlMatcher.find())
        {
            urlList.add(urlMatcher.group());
        }

        //System.out.println("花费时间 ：" + (System.currentTimeMillis() - start));

        return urlList;
    }


    private void setListeners()
    {
        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    int position = getCaretPosition();
                    int urlIndex = 0;
                    for (int[] range : urlRange)
                    {
                        if (position >= range[0] && position <= range[1])
                        {
                            String url = urlList.get(urlIndex);
                            openUrlWithDefaultBrowser(url);
                        }

                        urlIndex++;
                    }
                }

                super.mouseClicked(e);
            }
        });
    }

    /**
     * 打开默认浏览器访问页面
     */
    public static void openUrlWithDefaultBrowser(String url)
    {
        //启用系统默认浏览器来打开网址。
        try
        {
            URI uri = new URI(url);
            Desktop.getDesktop().browse(uri);
        }
        catch (Exception e)
        {
            System.out.println("URL打开失败");
        }
    }


    public Object getTag()
    {
        return tag;
    }

    public void setTag(Object tag)
    {
        this.tag = tag;
    }

    public boolean isParseUrl()
    {
        return parseUrl;
    }

    public void setParseUrl(boolean parseUrl)
    {
        this.parseUrl = parseUrl;
    }

/*    @Override
    public synchronized void addMouseListener(MouseListener l)
    {
        for (MouseListener listener : getMouseListeners())
        {
            if (listener == l)
            {
                return;
            }
        }

        super.addMouseListener(l);
    }*/
}


class LineEmojiInfo
{
    /**
     * emoji 表情的数量
     */
    private int count;

    private List<String> emojiList;

    public LineEmojiInfo(int count, List<String> emojiList)
    {
        this.count = count;
        this.emojiList = emojiList;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public List<String> getEmojiList()
    {
        return emojiList;
    }

    public void setEmojiList(List<String> emojiList)
    {
        this.emojiList = emojiList;
    }
}

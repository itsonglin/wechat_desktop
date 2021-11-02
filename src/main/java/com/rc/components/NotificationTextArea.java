package com.rc.components;

import com.rc.res.Colors;
import com.rc.utils.EmojiUtil;
import com.vdurmont.emoji.EmojiParser;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author song
 * @date 19-10-8 09:24
 * @description
 * @since
 */
public class NotificationTextArea extends JTextPane
{
    private final FontMetrics fontMetrics;
    private int maxWidth;

    public NotificationTextArea()
    {
        this(150);
    }
    public NotificationTextArea(int maxWidth)
    {
        this.maxWidth = maxWidth;
        initView();
        setListeners();
        fontMetrics = getFontMetrics(getFont());
    }

    private void setListeners()
    {
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                setCursor(Cursor.getDefaultCursor());
                super.mouseEntered(e);
            }
        });
    }


    private void initView()
    {
        this.setEditable(false);
        this.setBackground(Colors.WINDOW_BACKGROUND);
    }


    @Override
    public void setText(String t)
    {
        if (t == null)
        {
            return;
        }

        // 对emoji的Unicode编码转别名
        t = EmojiParser.parseToAliases(t);

        if (t == null)
        {
            return;
        }

        String[] lineArr = t.split("\\n");
        String line = lineArr[0];
        try
        {
            insertEmoji(line);
        } catch (BadLocationException e)
        {
            e.printStackTrace();
        }
    }

    private void insertEmoji(String src) throws BadLocationException
    {
        Document doc = getDocument();
        doc.remove(0, doc.getLength());

        // 这里要求key是按位置升序排列的，所有使用TreeMap
        StringBuilder stringBuilder = new StringBuilder();

        char[] charArr = src.toCharArray();
        char ch;
        boolean emojiStart = false;
        int pos = 0;
        int width = 0;
        for (int i = 0; i < charArr.length; i++)
        {
            ch = charArr[i];
            if (ch == ':')
            {
                if (!emojiStart)
                {
                    emojiStart = true;
                    stringBuilder.append(ch);
                } else
                {
                    emojiStart = false;
                    stringBuilder.append(ch);

                    setCaretPosition(pos);

                    String code = stringBuilder.toString();
                    boolean emojiInserted = false;
                    if (EmojiUtil.isCustomEmoji(code))
                    {
                        code = "[" + code.substring(1, code.length() - 1) + "]";
                    }
                    else
                    {
                        ImageIcon icon = EmojiUtil.getEmoji(this, code);
                        if (icon != null)
                        {
                            ImageIcon scaledIcon = new ImageIcon(icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
                            width += scaledIcon.getIconWidth();
                            if (width > maxWidth)
                            {
                                doc.insertString(pos, "...", getCharacterAttributes());
                                break;
                            } else
                            {
                                insertIcon(scaledIcon);
                                stringBuilder.setLength(0);
                                pos++;
                                emojiInserted = true;
                            }
                        }
                    }

                    if (!emojiInserted)
                    {
                        width += fontMetrics.stringWidth(code);
                        if (width > maxWidth)
                        {
                            doc.insertString(pos, "...", getCharacterAttributes());
                            break;
                        } else
                        {
                            doc.insertString(pos, code, getCharacterAttributes());
                            stringBuilder.setLength(0);
                            pos += code.length();
                        }
                    }
                }
            } else
            {
                if (emojiStart)
                {
                    stringBuilder.append(ch);
                } else
                {
                    width += fontMetrics.charWidth(ch);
                    if (width > maxWidth)
                    {
                        doc.insertString(pos, "...", getCharacterAttributes());
                        break;
                    } else
                    {
                        doc.insertString(pos, ch + "", getCharacterAttributes());
                    }

                    pos++;
                }
            }

        }
    }
}

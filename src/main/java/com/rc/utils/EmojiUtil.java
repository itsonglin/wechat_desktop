package com.rc.utils;

import com.rc.app.Launcher;
import com.rc.frames.MainFrame;
import com.rc.tasks.HttpBytesGetTask;
import com.rc.tasks.HttpResponseListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rc.utils.ImageUtil.isGif;

/**
 * Created by song on 2017/7/1.
 */
public class EmojiUtil
{
    public static final String CUSTOM_EMOJI_CACHE_PATH = Launcher.appFilesBasePath + "/cache/emoji_custom";

    /**
     * :dog: -> dog.png
     */
    private static final Map<String, String> customEmojiCacheMap = new HashMap<>();

    /**
     * :dog: -> dog.png
     */
    private static final Map<String, String> emojiCacheMap = new HashMap<>();

    /**
     * :dog: -> ImageIcon
     */
    private static final Map<String, ImageIcon> iconCache = new HashMap<>();

    public static String EMOJI_REGX;

    private static Pattern emojiPattern;


    static
    {
        File dir = new File(CUSTOM_EMOJI_CACHE_PATH);
        if (!dir.exists())
        {
            System.out.println("创建自定义表情缓存目录:" + CUSTOM_EMOJI_CACHE_PATH);
            dir.mkdirs();
        }

        EMOJI_REGX = ":.+?:";
        emojiPattern = Pattern.compile(EMOJI_REGX);// 懒惰匹配，最小匹配
    }

    /**
     * 获取Emoji表情
     *
     * @param code emoji代码，形式如 {@code :dog:}
     * @return
     */
    public static ImageIcon getEmoji(Object context, String code)
    {
        ImageIcon icon = iconCache.get(code);
        if (icon == null)
        {
            String iconPath = "/emoji/" + code.subSequence(1, code.length() - 1) + ".png";
            URL url = context.getClass().getResource(iconPath);

            if (url == null)
            {
                return null;
            }

            icon = new ImageIcon(url);
            iconCache.put(code, icon);
        }

        return icon;
    }

    /**
     * 判断给定的emoji代码是否可识别
     *
     * @param code
     * @return
     */
    public static boolean isRecognizableEmoji(Object context, String code)
    {
        return getEmoji(context, code) != null;
    }

    /**
     * 判断给定的emoji代码是否是自定义表情
     *
     * @param code
     * @return
     */
    public static boolean isCustomEmoji(String code)
    {
        return customEmojiCacheMap.containsKey(code);
    }

    /**
     * 添加自定义表情
     *
     * @param name      名称, 如test
     * @param extension 扩展名, 如png
     */
    public static void addCustomEmoji(String name, String extension)
    {
        final String ext = extension.startsWith(".") ? extension : "." + extension;

        File destFile = new File(CUSTOM_EMOJI_CACHE_PATH, name + ext);

        if (!destFile.exists())
        {
            HttpBytesGetTask task = new HttpBytesGetTask();
            task.setListener(new HttpResponseListener<byte[]>()
            {
                @Override
                public void onSuccess(byte[] ret)
                {
                    try (FileOutputStream fos = new FileOutputStream(destFile))
                    {
                        fos.write(ret);
                        fos.flush();
                        customEmojiCacheMap.put(":" + name + ":", name + ext);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailed(Object why)
                {
                    System.err.println("自定义表情获取失败:" + name);
                }
            });

            task.execute("");
        } else
        {
            customEmojiCacheMap.put(":" + name + ":", name + ext);
        }
    }

    public static ImageIcon getCustomEmoji(String code, int maxWidth, int maxHeight)
    {
        return getCustomEmoji(code, maxWidth, maxHeight, true);
    }

    public static ImageIcon getCustomEmoji(String code, int maxWidth, int maxHeight, boolean useCache)
    {
        ImageIcon imageIcon = useCache ? iconCache.get(code) : null;

        if (imageIcon == null)
        {
            String path = CUSTOM_EMOJI_CACHE_PATH + File.separator + customEmojiCacheMap.get(code);
            imageIcon = new ImageIcon(path);

            int iconWidth = imageIcon.getIconWidth();
            int iconHeight = imageIcon.getIconHeight();
            if (iconWidth > maxWidth || iconHeight > maxHeight)
            {
                float scale = iconWidth * 1.0F / iconHeight;

                if (iconWidth > iconHeight)
                {
                    iconWidth = iconWidth > maxWidth ? maxWidth : iconWidth;
                    iconHeight = (int) (iconWidth / scale);
                } else
                {
                    iconHeight = iconHeight > maxHeight ? maxHeight : iconHeight;
                    iconWidth = (int) (iconHeight * scale);
                }

                if (isGif(path))
                {
                    imageIcon = ImageUtil.scaleGif(path, iconWidth * 1.0F / imageIcon.getIconWidth(), iconHeight * 1.0F / imageIcon.getIconHeight());

                } else
                {
                    imageIcon.setImage(imageIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH));
                }
            }

            if (useCache)
            {
                iconCache.put(code, imageIcon);
            }
        }

        return imageIcon;
    }

    public static ImageIcon getCustomEmojiThumb(String code, int maxWidth)
    {
        String path = CUSTOM_EMOJI_CACHE_PATH + File.separator + customEmojiCacheMap.get(code);
        try
        {
            Image img = ImageIO.read(new File(path));
            img = img.getScaledInstance(maxWidth, maxWidth, Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(img);
            return imageIcon;

        } catch (Exception e)
        {
           // e.printStackTrace();
        }

        return null;
    }

    public static Set<String> getRegisteredCustomEmojis()
    {
        return customEmojiCacheMap.keySet();
    }

    /**
     * 根据前缀查找表情
     *
     * @param prefix
     * @return
     */
    public static List<String> findEmojisByPrefix(String prefix)
    {
        File dir = new File(CUSTOM_EMOJI_CACHE_PATH);
        File[] files = dir.listFiles((dir1, name) -> name.startsWith(prefix));
        List<String> codes = new ArrayList<>(files.length);
        for (File file : files)
        {
            String name = file.getName();
            codes.add(":" + name.substring(0, name.lastIndexOf(".")) + ":");
        }

        // dir =

        return codes;
    }

    /**
     * 提取字符串中的所有emoji表情编码
     *
     * @param src
     * @return
     */
    public static List<String> parseEmoji(String src)
    {
        List<String> emojiList = new ArrayList<>();

        Matcher emojiMatcher = emojiPattern.matcher(src);

        while (emojiMatcher.find())
        {
            String code = emojiMatcher.group();
            if (EmojiUtil.isRecognizableEmoji(MainFrame.getContext(), code))
            {
                emojiList.add(code);
            }
        }

        return emojiList;
    }
}

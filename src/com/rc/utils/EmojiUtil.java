package com.rc.utils;

import javax.swing.*;
import java.net.URL;

/**
 * Created by song on 2017/7/1.
 */
public class EmojiUtil
{

    /**
     * 获取Emoji表情
     *
     * @param code emoji代码，形式如 {@code :dog:}
     * @return
     */
    public static ImageIcon getEmoji(Object context, String code)
    {
        String iconPath = "/emoji/" + code.subSequence(1, code.length() - 1) + ".png";
        URL url = context.getClass().getResource(iconPath);
        return url == null ? null : new ImageIcon(url);
    }

    /**
     * 判断给定的emoji代码是否可识别
     * @param code
     * @return
     */
    public static boolean isRecognizableEmoji(Object context, String code)
    {
        return getEmoji(context, code) != null;
    }
}

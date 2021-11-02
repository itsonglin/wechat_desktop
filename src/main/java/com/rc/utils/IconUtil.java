package com.rc.utils;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by song on 2017/6/7.
 */
public class IconUtil
{
    private static Map<String, ImageIcon> iconCache = new HashMap<>();


    public static ImageIcon getIcon(Object context, String path, boolean cache)
    {
        return getIcon(context, path, -1, -1, Image.SCALE_SMOOTH, cache);
    }

    public static ImageIcon getIcon(Object context, String path, int width, int height, boolean cache)
    {
        return getIcon(context, path, width, height, Image.SCALE_SMOOTH, cache);
    }

    public static ImageIcon getIcon(Object context, String path, int width, int height, int hints, boolean cache)
    {
        ImageIcon imageIcon = iconCache.get(path);
        if (imageIcon == null)
        {
            imageIcon = getAndScaleCache(context, path, width, height, hints, cache);
        } else if (imageIcon.getIconWidth() != width || imageIcon.getIconHeight() != height)
        {
            imageIcon = getAndScaleCache(context, path, width, height, hints, cache);
        }

        return imageIcon;
    }


    private static ImageIcon getAndScaleCache(Object context, String path, int width, int height, int hints, boolean cache)
    {
        ImageIcon imageIcon = getImageIcon(context, path);
        if (width > 0 && height > 0)
        {
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(width, height, hints));
        }


        if (cache)
        {
            iconCache.put(path, imageIcon);
        }

        return imageIcon;
    }

    private static ImageIcon getImageIcon(Object context, String path)
    {
        URL url = context.getClass().getResource(path);
        if (url == null)
        {
            throw new RuntimeException("找不到资源:" + path);
        }

        ImageIcon imageIcon = new ImageIcon(url);
        return imageIcon;
    }
}

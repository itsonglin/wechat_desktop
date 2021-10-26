package com.rc.utils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by song on 2017/6/7.
 */
public class IconUtil
{
    private static Map<String, ImageIcon> iconCache = new HashMap<>();


    public static ImageIcon getIcon(Object context, String path)
    {
        return getIcon(context, path, -1, -1, Image.SCALE_SMOOTH);
    }

    public static ImageIcon getIcon(Object context, String path, int width)
    {
        return getIcon(context, path, width, width, Image.SCALE_SMOOTH);
    }

    public static ImageIcon getIcon(Object context, String path, int width, int height)
    {
        return getIcon(context, path, width, height, Image.SCALE_SMOOTH);
    }

    public static ImageIcon getIcon(Object context, String path, int width, int height, int hints)
    {
        ImageIcon imageIcon = iconCache.get(path);
        if (imageIcon == null)
        {
            imageIcon = getAndSetToCache(context, path, width, height, hints);
        } else if (imageIcon.getIconWidth() != width || imageIcon.getIconHeight() != height)
        {
            imageIcon = getAndSetToCache(context, path, width, height, hints);
        }

        return imageIcon;
    }

    private static ImageIcon getAndSetToCache(Object context, String path, int width, int height, int hints)
    {
        URL url = context.getClass().getResource(path);
        if (url == null)
        {
            return null;
        }

        ImageIcon imageIcon = new ImageIcon(url);

        if (width > 0 && height > 0)
        {
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(width, height, hints));
        }

        iconCache.put(path, imageIcon);

        return imageIcon;
    }
}

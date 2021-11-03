package com.rc.utils;

import com.rc.db.service.MessageService;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.*;
import java.util.Enumeration;

/**
 * Created by song on 17-5-29.
 */
public class FontUtil
{
    private static Font font;
    private static Font fontBold;

    static
    {
        /*if (OSUtil.getOsType() == OSUtil.Windows)
        {
            font = new Font("微软雅黑", Font.PLAIN, 14);
        }
        else if (OSUtil.getOsType() == OSUtil.Mac_OS)
        {
            font = new Font("PingFang SC", Font.PLAIN, 14);
        }
        else if (OSUtil.getOsType() == OSUtil.Linux)
        {
            String fontName = "PingFang SC Regular";
            GraphicsEnvironment environment;
            environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            String[] fonts = environment.getAvailableFontFamilyNames();//获得系统字体
            for (int i = 0; i < fonts.length; i++)
            {
                *//*if (fonts[i].equals("Monospaced"))
                {
                    fontName = "Monospaced";
                    break;
                }*//*

                System.out.println(fonts[i]);
            }

            font = new Font(fontName, Font.PLAIN, 14);
        }*/

        if (font == null)
        {
            font = loadFont("/fonts/PingFang-Regular.ttf");
        }
        if (fontBold == null)
        {
            fontBold = loadFont("/fonts/PingFang-Bold-2.ttf");
        }
    }

    public static Font loadFont(String path)
    {
        /*System.out.println(FontUtil.class.getClassLoader().getClass().getResourceAsStream(path));
        System.out.println(FontUtil.class.getResourceAsStream(path));

        System.out.println(FontUtil.class.getClassLoader().getClass().getResourceAsStream("fonts/PingFang-Regular.ttf"));
        System.out.println(FontUtil.class.getResourceAsStream("fonts/PingFang-Regular.ttf"));*/
        try (InputStream is = FontUtil.class.getResourceAsStream(path))
        {
            return  Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static Font getDefaultFont()
    {
        return getDefaultFont(14, Font.PLAIN);
    }

    public static Font getDefaultFont(int size)
    {
        return getDefaultFont(size, Font.PLAIN);
    }

    public static Font getDefaultFont(int size, int style)
    {
        if (style == Font.BOLD)
        {
            return fontBold.deriveFont(Font.PLAIN, size);
        }
        return font.deriveFont(style, size);
        //return new Font("YaHei Consolas Hybrid",  style, size);
        //return new Font("微软雅黑", style, size);
    }
}

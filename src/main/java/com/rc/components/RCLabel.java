package com.rc.components;

import javax.swing.*;
import java.awt.*;

/**
 * @author song
 * @date 19-10-25 15:36
 * @description
 * @since
 */
public class RCLabel extends JLabel
{
    private final FontMetrics fontMetrics;

    public RCLabel(String text)
    {
        this();
        this.setText(text);
    }

    public RCLabel()
    {
        fontMetrics = getFontMetrics(this.getFont());
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        String drawText = getText();
        int len = 0;
        int maxLen = getPreferredSize().width;
        char[] chars = getText().toCharArray();
        int pos = chars.length;
        for (int i = 0; i < chars.length; i++)
        {
            len += fontMetrics.charWidth(chars[i]);
            if (len > maxLen)
            {
                pos = i > 0 ? i : 0;
                break;
            }
        }

        if (pos < chars.length)
        {
            drawText = getText().substring(0, pos - 2) + "...";
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawString(drawText, 0, fontMetrics.getHeight() - 4);
    }
}

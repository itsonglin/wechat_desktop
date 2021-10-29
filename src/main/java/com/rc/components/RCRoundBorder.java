package com.rc.components;

import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * @author song
 * @date 21-10-29 17:42
 * @description
 * @since
 */
public class RCRoundBorder extends LineBorder
{
    public RCRoundBorder(Color color, int thickness)
    {
        super(color, thickness, true);
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
    {
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Graphics2D g2 = (Graphics2D) g;
        int i;
        g2.setRenderingHints(rh);
        g2.setColor(lineColor);
        for (i = 0; i < thickness; i++)
        {
            g2.fillRoundRect(x, y, width, height, 5, 5);
        }
        g2.dispose();
    }
}

package com.rc.components;

import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by song on 17-5-29.
 */
public class RCBorder implements Border
{
    public static final int TOP = 0;
    public static final int BOTTOM = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    private int orientation;
    private Color color;
    private float heightScale = 1.0F;

    public RCBorder(int orientation)
    {
        this(orientation, Colors.DARKER);
    }

    public RCBorder(int orientation, Color color)
    {
        this.orientation = orientation;
        this.color = color;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
    {
        g.setColor(color);

        switch (this.orientation)
        {
            case TOP:
            {
                g.drawLine(x, 1, width, 1);
                break;
            }
            case BOTTOM:
            {
                g.drawLine(x, height - 1, width, height - 1);
                break;
            }
            case RIGHT:
            {
                int h = (int) (height * heightScale);
                g.drawLine(width - 1, y + h, width - 1, height - h);
                break;
            }

            case LEFT:
            {
                g.drawLine(x, y, x, height);
                break;
            }
        }

        //g.setColor(Colors.DARKER);
        //g.drawLine(x, height -1 , width, height -1 );
    }

    public void setHeightScale(float scale)
    {
        this.heightScale = scale;
    }

    @Override
    public Insets getBorderInsets(Component c)
    {
        return new Insets(1, 5, 5, 1);
    }

    @Override
    public boolean isBorderOpaque()
    {
        return false;
    }
}

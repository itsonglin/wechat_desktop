package com.rc.forms;

import javax.swing.*;
import javax.swing.plaf.basic.BasicMenuItemUI;
import java.awt.*;

/**
 * Created by song on 2017/6/5.
 */
public class RCMenuItemUI extends BasicMenuItemUI
{

    private int width;
    private int height;
    private Font font;

    public RCMenuItemUI()
    {
        this(70, 25, new Font("微软雅黑", Font.PLAIN, 12));
    }

    public RCMenuItemUI(int width, int height, Font font)
    {

        this.width = width;
        this.height = height;
        this.font = font;
    }

    @Override
    public void installUI(JComponent c)
    {
        super.installUI(c);

        c.setPreferredSize(new Dimension(width, height));
        c.setBackground(Color.white);
        c.setFont(font);
        c.setBorder(null);
        selectionForeground = Color.BLACK;
        selectionBackground = new Color(214, 214, 214);
    }


    @Override
    protected void paintText(Graphics g, JMenuItem menuItem, Rectangle textRect, String text)
    {
        int x = (int) ((menuItem.getSize().getWidth() - textRect.width) / 2);

        g.setColor(Color.BLACK);
        Rectangle newRect =  new Rectangle(x, textRect.y, textRect.width, textRect.height);
        super.paintText(g, menuItem, newRect, text);
    }
}

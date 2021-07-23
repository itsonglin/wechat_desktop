package com.rc.components.message;

import com.rc.utils.FontUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Created by song on 12/06/2017.
 */
public class SystemMessagePanel extends JPanel
{
    private String text;
    FontMetrics fm = getFontMetrics(getFont());

    public SystemMessagePanel()
    {
        setPreferredSize(new Dimension(21,21));
    }

    public void setText(String text)
    {
        this.text = text;
    }



    public void paint(Graphics g)
    {
        int strWidth = fm.stringWidth(text);
        int strHeight = fm.getHeight();

        int width = strWidth + 6;
        int height = strHeight + 4;

        System.out.println(text + " == " + width +  " , " +  height);

        setPreferredSize(new Dimension(width, height));

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(195, 195, 195));
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        g2d.setColor(new Color(248, 248, 248));
        g2d.setFont(FontUtil.getDefaultFont(12));
        int x = (width - strWidth) / 2;
        g2d.drawString(text, x, strHeight - 2);
        g2d.dispose();


    }
}

package com.rc.panels;

import com.rc.components.Colors;
import com.rc.utils.FontUtil;

import javax.swing.*;
import java.awt.*;

/**
 * @author song
 * @date 19-9-24 15:55
 * @description
 * @since
 */
public class MessageTimePanel extends JPanel
{
    public JLabel time = new JLabel();

    public MessageTimePanel()
    {
        setFont(FontUtil.getDefaultFont(12));
        add(time);
    }

    @Override
    public Insets getInsets()
    {
        return new Insets(-3, 0, -3, 0);
    }

    public void paint(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Colors.ITEM_SELECTED);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        g2d.setColor(Colors.FONT_WHITE);
        FontMetrics fm = getFontMetrics(getFont());
        int x = (getWidth() - fm.stringWidth(time.getText())) / 2;
        g2d.drawString(time.getText(), x, fm.getHeight() - 1);
        g2d.dispose();
    }

    public void setText(String timeText)
    {
        this.time.setText(timeText);
    }
}

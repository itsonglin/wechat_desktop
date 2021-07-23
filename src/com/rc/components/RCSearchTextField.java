package com.rc.components;

import com.rc.utils.FontUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Created by song on 17-5-29.
 */
public class RCSearchTextField extends JTextField
{

    private RoundRectangle2D.Double shape;

    public RCSearchTextField()
    {
        //setBorder(null);
        setBackground(Colors.BG_GRAY);
        setForeground(Colors.FONT_BLACK);
        setCaretColor(Color.GRAY);
        setFont(FontUtil.getDefaultFont(12));

        //setSelectedTextColor(Color.red);

        setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                if (getText().isEmpty())
                {
                    repaint();
                }

            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {

            }
        });
    }

    @Override
    public boolean contains(int x, int y)
    {
        shape = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15);
        return shape.contains(x, y);
    }

    @Override
    protected void paintComponent(java.awt.Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(30, 30, 30, 22));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

        if (getText().isEmpty()/* && !(FocusManager.getCurrentKeyboardFocusManager().getFocusOwner() == this)*/)
        {
            //g2.setBackground(Color.gray);
            g2.setFont(FontUtil.getDefaultFont());
            g2.setColor(Color.GRAY);
            g2.drawString("搜索 ", 10, 18);
            g2.dispose();
        }
    }
}

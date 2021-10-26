package com.rc.components;

import com.rc.utils.FontUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class RCButton extends JButton
{
    private static final long serialVersionUID = 39082560987930759L;
    private Color BUTTON_COLOR1 = new Color(236, 236, 236);
    private Color BUTTON_COLOR2 = new Color(228, 228, 228);
    private Color BUTTON_COLOR3 = new Color(216, 216, 216);
    private Color borderColor = new Color(193, 193, 193);

    public static final Color BUTTON_FOREGROUND_COLOR = Color.WHITE;
    private boolean hover;

    public RCButton()
    {
        this("");
    }

    public RCButton(String title, Color normal, Color hover, Color press)
    {
        this(title);
        this.BUTTON_COLOR1 = normal;
        this.BUTTON_COLOR2 = hover;
        this.BUTTON_COLOR3 = press;
        this.borderColor = press;
    }

    public RCButton(String name)
    {
        this.setText(name);
        setFont(FontUtil.getDefaultFont(14));
        setBorderPainted(false);
        setForeground(BUTTON_FOREGROUND_COLOR);
        setFocusPainted(false);
        setContentAreaFilled(false);
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                hover = true;
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                hover = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        int h = getHeight();
        int w = getWidth();

        GradientPaint gp;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isPressed())
        {
            gp = new GradientPaint(0.0F, 0.0F, BUTTON_COLOR3, 0.0F,
                    h, BUTTON_COLOR3, true);
        }
        else
        {
            if (hover)
            {
                gp = new GradientPaint(0.0F, 0.0F, BUTTON_COLOR2, 0.0F,
                        h, BUTTON_COLOR2, true);
            }
            else
            {
                gp = new GradientPaint(0.0F, 0.0F, BUTTON_COLOR1, 0.0F,
                        h, BUTTON_COLOR1, true);
            }
        }

        /*g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                tran));*/

        RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0, 0, w - 1,
                h - 1, 4, 4);
        Shape clip = g2d.getClip();
        g2d.clip(r2d);

        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
        g2d.setClip(clip);


        g2d.setColor(borderColor);
        g2d.drawRoundRect(0, 0, w - 2, h - 2, 5, 5);
        // g2d.drawRoundRect(1, 1, w - 4, h - 4, 3, 3);
        g2d.dispose();
        super.paintComponent(g);
    }
}
package com.rc.components.message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 附件气泡
 *
 * Created by song on 17-6-3.
 */
public class RCAttachmentMessageBubble extends JPanel implements RCMessageBubble
{
    private  NinePatchImageIcon backgroundNormalIcon;
    private  NinePatchImageIcon backgroundActiveIcon;
    private Icon currentBackgroundIcon;


    public RCAttachmentMessageBubble()
    {
        setOpaque(false);
        setListener();
    }

    public void setBackgroundIcon(Icon icon)
    {
        currentBackgroundIcon = icon;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        if (currentBackgroundIcon != null)
        {
            currentBackgroundIcon.paintIcon(this, g, 0, 0);
        }
        super.paintComponent(g);
    }

    private void setListener()
    {
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                setActiveStatus(true);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                setActiveStatus(false);
            }
        });
    }

    public void setActiveStatus(boolean status)
    {
        if (status)
        {
            setBackgroundIcon(backgroundActiveIcon);
        }
        else
        {
            setBackgroundIcon(backgroundNormalIcon);
        }

        RCAttachmentMessageBubble.this.repaint();
    }


    public NinePatchImageIcon getBackgroundNormalIcon()
    {
        return backgroundNormalIcon;
    }

    public void setBackgroundNormalIcon(NinePatchImageIcon backgroundNormalIcon)
    {
        this.backgroundNormalIcon = backgroundNormalIcon;
    }

    public NinePatchImageIcon getBackgroundActiveIcon()
    {
        return backgroundActiveIcon;
    }

    public void setBackgroundActiveIcon(NinePatchImageIcon backgroundActiveIcon)
    {
        this.backgroundActiveIcon = backgroundActiveIcon;
    }

    public Icon getCurrentBackgroundIcon()
    {
        return currentBackgroundIcon;
    }

    public void setCurrentBackgroundIcon(Icon currentBackgroundIcon)
    {
        this.currentBackgroundIcon = currentBackgroundIcon;
    }

    @Override
    public synchronized void addMouseListener(MouseListener l)
    {
        for (MouseListener listener : getMouseListeners())
        {
            if (listener == l)
            {
                return;
            }
        }

        super.addMouseListener(l);
    }
}

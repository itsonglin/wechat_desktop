package com.rc.app;

import com.rc.components.Colors;
import com.rc.components.GBC;
import com.rc.frames.MainFrame;
import com.rc.panels.ChatPanel;
import com.rc.panels.MessagePanel;
import com.rc.utils.FontUtil;

import java.awt.*;
import javax.swing.*;

/**
 * 吐司提示框组件
 *
 * @author ccw
 * @since:2014-2-28
 */
public class Toast extends JWindow
{
    private final JLabel messageLabel;




    public Toast(Frame parent, String message)
    {
        super(parent);

        setForeground(Colors.FONT_WHITE);
        getContentPane().setBackground(Colors.DARKER);
        setOpacity(0.6F);

        setLayout(new GridBagLayout());
        messageLabel = new JLabel(message);
        messageLabel.setForeground(Colors.FONT_WHITE);
        messageLabel.setFont(FontUtil.getDefaultFont(12));
        add(messageLabel, new GBC(0, 0).setFill(GBC.BOTH).setInsets(5, 10, 5, 10));

        adjustLocation();

        setCursor(new Cursor(Cursor.HAND_CURSOR));

        pack();
    }


    @Override
    public void setVisible(boolean b)
    {
        if (b)
        {
            if (!this.isVisible())
            {
                adjustLocation();
            }
        }

        super.setVisible(b);


    }

    private void adjustLocation()
    {
        setLocationRelativeTo(MessagePanel.getContext());
        Point point = getLocation();
        setLocation(point.x, point.y + 170);
    }

    /**
     * 修改消息
     *
     * @param message
     */
    public void setMessage(String message)
    {
        this.messageLabel.setText(message);
    }
}
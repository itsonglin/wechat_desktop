package com.rc.adapter.message;

import com.rc.components.Colors;
import com.rc.components.GBC;
import com.rc.components.message.MessageImageLabel;
import com.rc.components.VerticalFlowLayout;
import com.rc.components.message.RCLeftImageMessageBubble;
import com.rc.utils.FontUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Created by song on 17-6-2.
 */
public class MessageLeftImageViewHolder extends BaseMessageViewHolder
{
    public JLabel sender = new JLabel();
    //public JLabel avatar = new JLabel();
    //public JLabel size = new JLabel();
    public MessageImageLabel image = new MessageImageLabel();
    public RCLeftImageMessageBubble imageBubble = new RCLeftImageMessageBubble();
    private JPanel timePanel = new JPanel();
    private JPanel messageAvatarPanel = new JPanel();

    public MessageLeftImageViewHolder()
    {
        initComponents();
        initView();
    }

    private void initComponents()
    {
        timePanel.setBackground(Colors.WINDOW_BACKGROUND);
        messageAvatarPanel.setBackground(Colors.WINDOW_BACKGROUND);


        imageBubble.add(image);

        time.setForeground(Colors.FONT_GRAY);
        time.setFont(FontUtil.getDefaultFont(12));

        sender.setFont(FontUtil.getDefaultFont(12));
        sender.setForeground(Colors.FONT_GRAY);
        //sender.setVisible(false);
    }

    private void initView()
    {
        setLayout(new BorderLayout());
        timePanel.add(time);

        JPanel senderMessagePanel = new JPanel();
        senderMessagePanel.setBackground(Colors.WINDOW_BACKGROUND);
        senderMessagePanel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0,0,true, false));
        senderMessagePanel.add(sender);
        senderMessagePanel.add(imageBubble);

        messageAvatarPanel.setLayout(new GridBagLayout());
        messageAvatarPanel.add(avatar, new GBC(1, 0).setWeight(1, 1).setAnchor(GBC.NORTH).setInsets(4, 5,0,0));
        messageAvatarPanel.add(senderMessagePanel, new GBC(2, 0)
                .setWeight(1000, 1)
                .setAnchor(GBC.WEST)
                .setInsets(0,5,5,0));

        add(timePanel, BorderLayout.NORTH);
        add(messageAvatarPanel, BorderLayout.CENTER);
    }
}

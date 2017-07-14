package com.rc.adapter.message;

import com.rc.components.*;
import com.rc.components.message.RCLeftImageMessageBubble;
import com.rc.utils.FontUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Created by song on 17-6-2.
 */
public class MessageLeftAttachmentViewHolder extends MessageAttachmentViewHolder
{
    public JLabel sender = new JLabel();

    public MessageLeftAttachmentViewHolder()
    {
        initComponents();
        initView();
    }

    private void initComponents()
    {
        messageBubble = new RCLeftImageMessageBubble();

        /*timePanel.setBackground(Colors.WINDOW_BACKGROUND);
        messageAvatarPanel.setBackground(Colors.WINDOW_BACKGROUND);

        size.setForeground(Colors.FONT_GRAY);
        size.setFont(FontUtil.getDefaultFont(12));*/


        sender.setFont(FontUtil.getDefaultFont(12));
        sender.setForeground(Colors.FONT_GRAY);
        //sender.setVisible(false);

        /*attachmentPanel.setOpaque(false);

        progressBar.setMaximum(100);
        progressBar.setMinimum(0);
        progressBar.setValue(100);
        progressBar.setUI(new GradientProgressBarUI());
        progressBar.setVisible(false);

        messageBubble.setCursor(new Cursor(Cursor.HAND_CURSOR));

        sizeLabel.setFont(FontUtil.getDefaultFont(12));
        sizeLabel.setForeground(Colors.FONT_GRAY);*/

        messageBubble.setCursor(new Cursor(Cursor.HAND_CURSOR));


    }

    private void initView()
    {
        setLayout(new BorderLayout());

        timePanel.add(time);

        attachmentPanel.setLayout(new GridBagLayout());
        attachmentPanel.add(attachmentIcon, new GBC(0, 0).setWeight(1, 1).setInsets(5,5,5,0));
        attachmentPanel.add(attachmentTitle, new GBC(1, 0).setWeight(100, 1).setAnchor(GBC.NORTH)
                .setInsets(5, 8, 5, 5));
        attachmentPanel.add(progressBar, new GBC(1, 1).setWeight(1, 1).setFill(GBC.HORIZONTAL)
                .setAnchor(GBC.SOUTH).setInsets(0, 8, 5, 5));

        attachmentPanel.add(sizeLabel, new GBC(1, 1).setWeight(1, 1).setFill(GBC.HORIZONTAL).setAnchor(GBC.SOUTH).setInsets(-20,8,3,0));


        messageBubble.add(attachmentPanel);


        JPanel senderMessagePanel = new JPanel();
        senderMessagePanel.setBackground(Colors.WINDOW_BACKGROUND);
        senderMessagePanel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0,0,true, false));
        senderMessagePanel.add(sender);
        senderMessagePanel.add(messageBubble);

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

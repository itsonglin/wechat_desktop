package com.rc.adapter.message;

import com.rc.components.*;
import com.rc.components.message.RCRightAttachmentMessageBubble;

import javax.swing.*;
import java.awt.*;

/**
 * Created by song on 17-6-3.
 */
public class MessageRightAttachmentViewHolder extends MessageAttachmentViewHolder
{
    public JLabel resend = new JLabel(); // 重发按钮


    public MessageRightAttachmentViewHolder()
    {
        initComponents();
        initView();
    }

    private void initComponents()
    {
        messageBubble = new RCRightAttachmentMessageBubble();

        /*timePanel.setBackground(Colors.WINDOW_BACKGROUND);
        messageAvatarPanel.setBackground(Colors.WINDOW_BACKGROUND);

        size.setForeground(Colors.FONT_GRAY);
        size.setFont(FontUtil.getDefaultFont(12));*/

        ImageIcon resendIcon = new ImageIcon(getClass().getResource("/image/resend.png"));
        resendIcon.setImage(resendIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        resend.setIcon(resendIcon);
        resend.setToolTipText("文件发送失败，点击重新发送");
        resend.setCursor(new Cursor(Cursor.HAND_CURSOR));


        //resend.setVisible(false);


       /* attachmentPanel.setOpaque(false);


        progressBar.setMaximum(100);
        progressBar.setMinimum(0);
        progressBar.setValue(5);
        progressBar.setUI(new GradientProgressBarUI());*/

        messageBubble.setCursor(new Cursor(Cursor.HAND_CURSOR));

       /* sizeLabel.setFont(FontUtil.getDefaultFont(12));
        sizeLabel.setForeground(Colors.FONT_GRAY);*/

    }

    private void initView()
    {
        setLayout(new BorderLayout());

        timePanel.add(time);

        attachmentPanel.setLayout(new GridBagLayout());
        attachmentPanel.add(attachmentIcon, new GBC(0, 0).setWeight(1, 1).setInsets(5,5,5,0));
        attachmentPanel.add(attachmentTitle, new GBC(1, 0).setWeight(100, 1).setAnchor(GBC.NORTH)
                .setInsets(5, 5, 0, 5));
        attachmentPanel.add(progressBar, new GBC(1, 1).setWeight(1, 1).setFill(GBC.HORIZONTAL).setAnchor(GBC.SOUTH).setInsets(0, 8, 5, 5));
        attachmentPanel.add(sizeLabel, new GBC(1, 1).setWeight(1, 1).setFill(GBC.HORIZONTAL).setAnchor(GBC.SOUTH).setInsets(-20,8,0,0));

        messageBubble.add(attachmentPanel);


        JPanel resendAttachmentPanel = new JPanel();
        resendAttachmentPanel.setBackground(Colors.WINDOW_BACKGROUND);
        resendAttachmentPanel.add(resend, BorderLayout.WEST);
        resendAttachmentPanel.add(messageBubble, BorderLayout.CENTER);

        messageAvatarPanel.setLayout(new GridBagLayout());
        messageAvatarPanel.add(resendAttachmentPanel, new GBC(1, 0).setWeight(1000, 1)
                .setAnchor(GBC.EAST).setInsets(0, 0, 5, 0));
        messageAvatarPanel.add(avatar, new GBC(2, 0).setWeight(1, 1).setAnchor(GBC.NORTH)
                .setInsets(5, 0, 0, 10));

        add(timePanel, BorderLayout.NORTH);
        add(messageAvatarPanel, BorderLayout.CENTER);
    }

}

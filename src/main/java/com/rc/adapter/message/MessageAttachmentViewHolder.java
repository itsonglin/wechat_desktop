package com.rc.adapter.message;

import com.rc.components.Colors;
import com.rc.components.GradientProgressBarUI;
import com.rc.components.RCProgressBar;
import com.rc.components.SizeAutoAdjustTextArea;
import com.rc.components.message.AttachmentPanel;
import com.rc.components.message.RCAttachmentMessageBubble;
import com.rc.frames.MainFrame;
import com.rc.utils.FontUtil;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by song on 16/06/2017.
 */
public class MessageAttachmentViewHolder extends BaseMessageViewHolder
{
    public SizeAutoAdjustTextArea attachmentTitle;
    public RCProgressBar progressBar = new RCProgressBar(); // 进度条
    public JPanel timePanel = new JPanel(); // 时间面板
    public JPanel messageAvatarPanel = new JPanel(); // 消息 + 头像组合面板
    public AttachmentPanel attachmentPanel = new AttachmentPanel(); // 附件面板
    public JLabel attachmentIcon = new JLabel(); // 附件类型icon
    public JLabel sizeLabel = new JLabel();
    public RCAttachmentMessageBubble messageBubble;

    public MessageAttachmentViewHolder()
    {
        initComponents();
        setListeners();
    }

    private void setListeners()
    {
        MouseAdapter listener = new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                messageBubble.setActiveStatus(true);
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                messageBubble.setActiveStatus(false);
                super.mouseExited(e);
            }
        };

        attachmentPanel.addMouseListener(listener);
        attachmentTitle.addMouseListener(listener);

    }

    private void initComponents()
    {
        int maxWidth = (int) (MainFrame.getContext().currentWindowWidth * 0.427);
        attachmentTitle = new SizeAutoAdjustTextArea(maxWidth);

        timePanel.setBackground(Colors.WINDOW_BACKGROUND);
        messageAvatarPanel.setBackground(Colors.WINDOW_BACKGROUND);

        time.setForeground(Colors.FONT_GRAY);
        time.setFont(FontUtil.getDefaultFont(12));

        attachmentPanel.setOpaque(false);

        progressBar.setMaximum(100);
        progressBar.setMinimum(0);
        progressBar.setValue(100);
        progressBar.setUI(new GradientProgressBarUI());
        progressBar.setVisible(false);

        sizeLabel.setFont(FontUtil.getDefaultFont(12));
        sizeLabel.setForeground(Colors.FONT_GRAY);
    }
}

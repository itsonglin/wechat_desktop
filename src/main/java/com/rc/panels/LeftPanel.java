package com.rc.panels;

import com.rc.frames.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 * @author song
 * @date 19-9-24 14:42
 * @description
 * @since
 */
public class LeftPanel extends JPanel
{
    private NavPanel navPanel;
    private ChatRoomsPanel chatRoomsPanel;

    public LeftPanel()
    {
        initComponents();
        initView();
    }

    private void initComponents()
    {
        navPanel = new NavPanel(this);
        chatRoomsPanel = new ChatRoomsPanel();
    }

    private void initView()
    {
        this.setPreferredSize(new Dimension(310, MainFrame.DEFAULT_HEIGHT));
        this.setLayout(new BorderLayout());
        add(navPanel, BorderLayout.WEST);
        add(chatRoomsPanel, BorderLayout.CENTER);
    }

}

package com.rc.panels;

import com.rc.components.Colors;
import com.rc.components.GBC;
import com.rc.frames.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Created by song on 17-5-29.<br/>
 *
 * <P>下图 #ChatRoomsPanel# 对应的位置</P>
 *
 * 包含 用户信息面板、搜索面板、功能TAB以及房间列表等
 *
 * <P>推荐使用Menlo或Consolas字体</P>
 * ┌────────────────────────┬────────────────────────────────────────────────────────┐
 * │                        │  Room Title                                         ≡  │
 * │                        ├────────────────────────────────────────────────────────┤
 * │                        │                                                        │
 * │                        │                     message time                       │
 * │                        │  ┌──┐ ┌────────────┐                                   │
 * │                        │  └──┘ │  message   │                                   │
 * │                        │       └────────────┘                                   │
 * │                        │                                                        │
 * │                        │                                                        │
 * │     #ChatRoomsPanel#        │                     message time                       │
 * │                        │                                    ┌────────────┐ ┌──┐ │
 * │                        │                                    │  message   │ └──┘ │
 * │                        │                                    └────────────┘      │
 * │                        │                                                        │
 * │                        │                                                        │
 * │                        ├────────────────────────────────────────────────────────┤
 * │                        │  ▆   ▆   ▆                                             │
 * │                        │                                                        │
 * │                        │                                                        │
 * │                        │                                                ┌─────┐ │
 * │                        │                                                └─────┘ │
 * └────────────────────────┴────────────────────────────────────────────────────────┘
 */
public class ChatRoomsPanel extends JPanel
{
    private SearchPanel searchPanel;
    private ListPanel listPanel;

    private static ChatRoomsPanel context;

    public ChatRoomsPanel()
    {
        context = this;
        initComponents();
        initView();
    }

    private void initComponents()
    {
        searchPanel = new SearchPanel(this);

        //mainOperationPanel.setBackground(Color.blue);

        listPanel = new ListPanel(this);
        listPanel.setBackground(Colors.BG_GRAY);

        this.setPreferredSize(new Dimension(250, MainFrame.DEFAULT_HEIGHT));
        this.setMaximumSize(new Dimension(250, MainFrame.DEFAULT_HEIGHT));
    }

    private void initView()
    {
        this.setBackground(Colors.BG_GRAY);
        this.setLayout(new GridBagLayout());

        add(searchPanel, new GBC(0, 0).setAnchor(GBC.CENTER).setFill(GBC.HORIZONTAL).setWeight(1, 2));
        add(listPanel, new GBC(0, 2).setAnchor(GBC.CENTER).setFill(GBC.BOTH).setWeight(1, 60));

    }

    public ListPanel getListPanel()
    {
        return this.listPanel;
    }


    public static ChatRoomsPanel getContext()
    {
        return context;
    }
}

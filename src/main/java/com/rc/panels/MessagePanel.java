package com.rc.panels;

import com.rc.components.Colors;
import com.rc.components.RCListView;
import com.rc.components.message.MessagePopupMenu;
import com.rc.frames.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Created by song on 17-5-30.
 *
 * <P>下图 #MessagePanel# 对应的位置</P>
 *
 * 消息列表区域
 *
 * <P>推荐使用Menlo或Consolas字体</P>
 * ┌────────────────────────┬────────────────────────────────────────────────────────┐
 * │ ┌─────┐                │  Room Title                                         ≡  │
 * │ │     │ name         ≡ ├────────────────────────────────────────────────────────┤
 * │ └─────┘                │                                                        │
 * ├────────────────────────┤                                                        │
 * │    search              │                                                        │
 * ├────────────────────────┤                                                        │
 * │  ▆    │    ▆   │   ▆   │                                                        │
 * ├────────────────────────┤                                                        │
 * │ ┌──┐ name         14:01│                    #MessagePanel#                      │
 * │ └──┘ message        99+│                                                        │
 * ├────────────────────────┤                                                        │
 * │                        │                                                        │
 * │                        │                                                        │
 * │                        │                                                        │
 * │          Room          │                                                        │
 * │                        ├────────────────────────────────────────────────────────┤
 * │                        │  ▆   ▆   ▆                                             │
 * │          List          │                                                        │
 * │                        │                                                        │
 * │                        │                                                ┌─────┐ │
 * │                        │                                                └─────┘ │
 * └────────────────────────┴────────────────────────────────────────────────────────┘
 */
public class MessagePanel extends ParentAvailablePanel
{
    private static MessagePanel context;

    RCListView listView;

    public MessagePanel(JPanel parent)
    {
        super(parent);
        context = this;

        initComponents();
        setListeners();
        initView();
    }


    private void initComponents()
    {
        listView = new RCListView(0, 15);
        listView.setScrollBarColor(Colors.SCROLL_BAR_THUMB, Colors.WINDOW_BACKGROUND);
        //listView.setScrollBarColor(Colors.WINDOW_BACKGROUND, Colors.WINDOW_BACKGROUND);
        listView.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //listView.setScrollHiddenOnMouseLeave(listView);
    }

    private void setListeners()
    {
        /*listView.addMouseListener(new AbstractMouseListener(){

            @Override
            public void mouseClicked(MouseEvent e)
            {
                RoomMembersPanel.getContext().setVisible(false);
                super.mouseClicked(e);
            }
        });*/
    }

    private void initView()
    {
        this.setLayout(new BorderLayout());
        add(listView, BorderLayout.CENTER);
    }

    public RCListView getMessageListView()
    {
        return listView;
    }

    public static MessagePanel getContext()
    {
        return context;
    }
}


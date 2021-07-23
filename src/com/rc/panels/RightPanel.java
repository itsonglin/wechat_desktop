package com.rc.panels;

import com.rc.components.Colors;
import com.rc.frames.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Created by song on 17-5-29.
 * <p>
 * 下图 #RightPanel# 对应的位置<br/>
 * <p>
 * 包含 房间标题面板、房间信息按钮、窗口控制面板、消息列表、消息编辑面板等
 *
 * <P>推荐使用Menlo或Consolas字体</P>
 * ┌────────────────────────┬────────────────────────────────────────────────────────┐
 * │ ┌─────┐                │                                                        │
 * │ │     │ name         ≡ │                                                        │
 * │ └─────┘                │                                                        │
 * ├────────────────────────┤                                                        │
 * │    search              │                                                        │
 * ├────────────────────────┤                                                        │
 * │  ▆    │    ▆   │   ▆   │                                                        │
 * ├────────────────────────┤                                                        │
 * │ ┌──┐ name         14:01│                                                        │
 * │ └──┘ message        99+│                   #RightPanel#                         │
 * ├────────────────────────┤                                                        │
 * │                        │                                                        │
 * │                        │                                                        │
 * │                        │                                                        │
 * │          Room          │                                                        │
 * │                        │                                                        │
 * │                        │                                                        │
 * │          List          │                                                        │
 * │                        │                                                        │
 * │                        │                                                        │
 * │                        │                                                        │
 * └────────────────────────┴────────────────────────────────────────────────────────┘
 */
public class RightPanel extends JPanel
{
    private static RightPanel context;
    private TitlePanel titlePanel;
    private RoomMembersPanel roomMembersPanel;

    private ChatPanel chatPanel;
    private TipPanel tipPanel;
    private UserInfoPanel userInfoPanel;
    private WebBrowserPanel webBroswerPanel;


    private JPanel contentPanel;

    private CardLayout cardLayout;

    public static final String MESSAGE = "MESSAGE";
    public static final String TIP = "TIP";
    public static final String USER_INFO = "USER_INFO";
    public static final String WEB_BROWSER = "WEB_BROWSER";


    public RightPanel()
    {
        context = this;
        initComponents();
        initView();

    }

    private void initComponents()
    {
        cardLayout = new CardLayout();
        contentPanel = new JPanel();
        contentPanel.setLayout(cardLayout);

        titlePanel = new TitlePanel(this);
        chatPanel = new ChatPanel(this);
        roomMembersPanel = new RoomMembersPanel(this);
        tipPanel = new TipPanel(this);
        userInfoPanel = new UserInfoPanel(this);
        webBroswerPanel = new WebBrowserPanel(this);
    }

    private void initView()
    {
        contentPanel.add(tipPanel, TIP);
        contentPanel.add(userInfoPanel, USER_INFO);
        contentPanel.add(chatPanel, MESSAGE);
        contentPanel.add(webBroswerPanel, WEB_BROWSER);

        this.setBackground(Colors.FONT_WHITE);
        this.setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(roomMembersPanel, BorderLayout.EAST);
        add(contentPanel, BorderLayout.CENTER);

        this.setPreferredSize(new Dimension(540, MainFrame.DEFAULT_HEIGHT));
    }

    public void showPanel(String who)
    {
        cardLayout.show(contentPanel, who);
    }


    public RoomMembersPanel getRoomMembersPanel()
    {
        return roomMembersPanel;
    }

    public JPanel getTipPanel()
    {
        return tipPanel;
    }

    public UserInfoPanel getUserInfoPanel()
    {
        return userInfoPanel;
    }

    public static RightPanel getContext()
    {
        return context;
    }

    public JPanel getContentPanel()
    {
        return contentPanel;
    }


}

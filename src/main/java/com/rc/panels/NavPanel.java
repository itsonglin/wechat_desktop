package com.rc.panels;

import com.rc.app.Launcher;
import com.rc.components.VerticalFlowLayout;
import com.rc.res.Colors;
import com.rc.components.GBC;
import com.rc.components.RCListView;
import com.rc.components.message.MainOperationPopupMenu;
import com.rc.db.service.CurrentUserService;
import com.rc.entity.RoomItem;
import com.rc.frames.MainFrame;
import com.rc.frames.SystemConfigDialog;
import com.rc.listener.AbstractMouseListener;
import com.rc.listener.WindowMouseListener;
import com.rc.res.Cursors;
import com.rc.utils.AvatarUtil;
import com.rc.utils.FontUtil;
import com.rc.utils.IconUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by song on 19-9-24.<br/>
 * 导航栏
 */
public class NavPanel extends BasePanel
{
    private static NavPanel context;
    private final FontMetrics fontMetrics;


    /**
     * 导航项面板
     */
    private JPanel navItemsPanel;

    /**
     * 导航底部面板
     */
    private JPanel bottomPanel;


    /**
     * 头像
     */
    private JLabel avatarLabel;

    /**
     * 聊天
     */
    private JLabel chatLabel;

    /**
     * 联系人
     */
    private JLabel contactsLabel;


    /**
     * 收藏
     */
    private JLabel collectionLabel;

    /**
     * 菜单
     */
    private JLabel menuLabel;

    private MainOperationPopupMenu mainOperationPopupMenu;

    public JPanel chatPanel;
    public JLabel newMsgIcon;

    public JPanel collectionPanel;
    public JLabel collectionNewMsgIcon;

    private long chatLabelLastClickTime;

    public static final int CHAT = 1;
    public static final int CONTACTS = 2;
    public static final int COLLECTION = 3;

    /**
     * 当前选中的tab
     */
    private int selectedTab = CHAT;

    private CurrentUserService currentUserService = Launcher.currentUserService;

    public NavPanel(JPanel parent)
    {
        super(parent);
        context = this;
        fontMetrics = getFontMetrics(getFont());

        initialize();
    }

    public void initComponents()
    {
        navItemsPanel = new JPanel();
        navItemsPanel.setBackground(Colors.DARK);

        bottomPanel = new JPanel();
        bottomPanel.setBackground(Colors.DARK);

        avatarLabel = new JLabel();
        avatarLabel.setIcon(new ImageIcon(AvatarUtil.createOrLoadUserAvatar(Launcher.currentUser.getUsername()).getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
        avatarLabel.setCursor(Cursors.HAND_CURSOR);


        chatLabel = new JLabel();
        chatLabel.setIcon(IconUtil.getIcon(this, "/image/chat_active.png", true));
        chatLabel.setHorizontalAlignment(JLabel.CENTER);
        chatLabel.setCursor(Cursors.HAND_CURSOR);

        newMsgIcon = buildMsgIcon();
        newMsgIcon.setCursor(Cursors.HAND_CURSOR);

        collectionNewMsgIcon = buildMsgIcon();
        collectionNewMsgIcon.setCursor(Cursors.HAND_CURSOR);

        contactsLabel = new JLabel();
        contactsLabel.setIcon(IconUtil.getIcon(this, "/image/contacts_normal.png", true));
        contactsLabel.setHorizontalAlignment(JLabel.CENTER);
        contactsLabel.setCursor(Cursors.HAND_CURSOR);

        collectionLabel = new JLabel();
        collectionLabel.setIcon(IconUtil.getIcon(this, "/image/collection_normal.png", true));
        collectionLabel.setHorizontalAlignment(JLabel.CENTER);
        collectionLabel.setCursor(Cursors.HAND_CURSOR);

        menuLabel = new JLabel();
        menuLabel.setIcon(IconUtil.getIcon(this, "/image/menu_normal.png", true));
        menuLabel.setCursor(Cursors.HAND_CURSOR);
        menuLabel.setHorizontalAlignment(JLabel.CENTER);


        chatPanel = new JPanel();
        chatPanel.setBackground(Colors.DARK);

        collectionPanel = new JPanel();
        collectionPanel.setLayout(new GridBagLayout());
        collectionPanel.setBackground(Colors.DARK);
        collectionPanel.add(collectionNewMsgIcon, new GBC(0, 0).setWeight(1, 1).setFill(GBC.VERTICAL).setAnchor(GBC.EAST).setInsets(0, 0, 16, 0));
        collectionPanel.add(collectionLabel, new GBC(0, 1).setWeight(1, 1).setFill(GBC.BOTH).setInsets(-45, 0, 0, 0));

        mainOperationPopupMenu = new MainOperationPopupMenu();
    }

    public void initView()
    {
        this.setBackground(Colors.DARK);
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(60, MainFrame.DEFAULT_HEIGHT));

        chatPanel.setLayout(new GridBagLayout());
        chatPanel.add(newMsgIcon, new GBC(0, 0).setWeight(1, 1).setFill(GBC.VERTICAL).setAnchor(GBC.EAST).setInsets(0, 0, 16, 0));
        chatPanel.add(chatLabel, new GBC(0, 1).setWeight(1, 1).setFill(GBC.BOTH).setInsets(-45, 0, 0, 0));

        navItemsPanel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 12, true, false));
        navItemsPanel.add(chatPanel);
        navItemsPanel.add(contactsLabel);
        navItemsPanel.add(collectionPanel);

        bottomPanel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 10, true, false));
        bottomPanel.add(menuLabel);

        add(avatarLabel, new GBC(0, 0).setFill(GBC.NONE).setAnchor(GBC.NORTH).setWeight(1, 1).setInsets(20, 0, 0, 0));
        add(navItemsPanel, new GBC(0, 1).setFill(GBC.HORIZONTAL).setAnchor(GBC.NORTH).setWeight(10, 100).setInsets(5, 0, 0, 0));
        add(bottomPanel, new GBC(0, 2).setFill(GBC.BOTH).setAnchor(GBC.SOUTH).setWeight(1, 4).setInsets(10, 0, 0, 0));
    }


    public void setListeners()
    {
        NavPanel.TabItemClickListener  clickListener = new NavPanel.TabItemClickListener();
        chatLabel.addMouseListener(clickListener);
        contactsLabel.addMouseListener(clickListener);
        collectionLabel.addMouseListener(clickListener);

        MouseAdapter mouseEnterExitListener = new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                if (e.getComponent() == chatLabel && selectedTab != CHAT)
                {
                    chatLabel.setIcon(IconUtil.getIcon(this, "/image/chat_hover.png", true));
                }
                else if (e.getComponent() == contactsLabel && selectedTab != CONTACTS)
                {
                    contactsLabel.setIcon(IconUtil.getIcon(this, "/image/contacts_hover.png", true));
                }
                else if (e.getComponent() == collectionLabel && selectedTab != COLLECTION)
                {
                    collectionLabel.setIcon(IconUtil.getIcon(this, "/image/collection_hover.png", true));
                } else if (e.getComponent() == menuLabel)
                {
                    menuLabel.setIcon(IconUtil.getIcon(this, "/image/menu_hover.png", true));
                }
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                if (e.getComponent() == chatLabel && selectedTab != CHAT)
                {
                    chatLabel.setIcon(IconUtil.getIcon(this, "/image/chat_normal.png", true));
                }
                else if (e.getComponent() == contactsLabel && selectedTab != CONTACTS)
                {
                    contactsLabel.setIcon(IconUtil.getIcon(this, "/image/contacts_normal.png", true));
                }
                else if (e.getComponent() == collectionLabel && selectedTab != COLLECTION)
                {
                    collectionLabel.setIcon(IconUtil.getIcon(this, "/image/collection_normal.png", true));
                }
                else if (e.getComponent() == menuLabel)
                {
                    menuLabel.setIcon(IconUtil.getIcon(this, "/image/menu_normal.png", true));
                }
            }
        };
        chatLabel.addMouseListener(mouseEnterExitListener);
        contactsLabel.addMouseListener(mouseEnterExitListener);
        collectionLabel.addMouseListener(mouseEnterExitListener);
        menuLabel.addMouseListener(mouseEnterExitListener);

        menuLabel.addMouseListener(new AbstractMouseListener()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    Component component = e.getComponent();
                    mainOperationPopupMenu.show(component, -70, -130);
                    super.mouseClicked(e);
                }

            }
        });

        avatarLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    SystemConfigDialog.display();
                    super.mouseClicked(e);
                }
            }
        });

        chatLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                // 双击聊天图标时,循环滚动到含有未读消息的room位置
                if (System.currentTimeMillis() - chatLabelLastClickTime < 200)
                {
                    scrollToUnreadRoom();
                    super.mouseClicked(e);
                } else
                {
                    chatLabelLastClickTime = System.currentTimeMillis();
                }
            }
        });

        WindowMouseListener windowMouseListener = new WindowMouseListener(MainFrame.getContext());
        this.addMouseListener(windowMouseListener);
        this.addMouseMotionListener(windowMouseListener);
    }


    /**
     * 循环滚动到含有未读消息的room位置
     */
    private void scrollToUnreadRoom()
    {
        RCListView roomsListView = RoomsPanel.getContext().getRoomItemsListView();
        List<RoomItem> roomItems = RoomsPanel.getContext().getRoomItemList();
        List<Integer> posList = new ArrayList<>();
        for (int i = 0; i < roomItems.size(); i++)
        {
            if (roomItems.get(i).getUnreadCount() > 0)
            {
                posList.add(i);
            }
        }

        if (posList.size() > 0)
        {
            int currPos = roomsListView.getScrollPosition();
            int targetPos = posList.get(0);

            for (Integer pos : posList)
            {
                if (pos <= currPos)
                {
                    continue;
                } else
                {
                    targetPos = pos;
                    break;
                }
            }

            roomsListView.scrollToPosition(targetPos);
        }
    }


    private JLabel buildMsgIcon()
    {
        JLabel msgIcon = new JLabel();
        msgIcon.setPreferredSize(new Dimension(10, 10));
        msgIcon.setFont(FontUtil.getDefaultFont(12));
        msgIcon.setForeground(Colors.FONT_WHITE);
        msgIcon.setPreferredSize(new Dimension(10, 10));
        msgIcon.setHorizontalTextPosition(SwingConstants.CENTER);
        msgIcon.setHorizontalAlignment(SwingConstants.CENTER);
        msgIcon.setVerticalAlignment(SwingConstants.CENTER);
        msgIcon.setPreferredSize(new Dimension(25, 15));
        msgIcon.setIcon(null);
        msgIcon.setText("");
        return msgIcon;
    }


    class TabItemClickListener extends MouseAdapter
    {

        @Override
        public void mouseReleased(MouseEvent e)
        {
            if (e.getButton() == MouseEvent.BUTTON1)
            {

                // 搜索框内容清空
                SearchAndCreateGroupPanel.getContext().clearSearchText();

                if (e.getComponent() == chatLabel)
                {
                    show(CHAT);
                } else if (e.getComponent() == contactsLabel)
                {
                    show(CONTACTS);
                } else if (e.getComponent() == collectionLabel)
                {
                    show(COLLECTION);

                    //NotificationUtil.show("Swsjxy4PXXJxd8dXk", IconUtil.getIcon(this, "/image/avatarLabel.jpg", 50, 50), "信息技术部", "Jogen", "(后台服务) 自动部署 ZXJ-AbsProjectManagementService-DEV 成功");

                    /*if (Math.random() > 0.5)
                    {
                        setUnreadMessageCount((int) (Math.random() * 150));
                    }
                    else
                    {
                        setUnreadMessageCount(0);
                    }*/
                }
            }
        }
    }

    public void reloadAvatar()
    {
        String currentUsername = currentUserService.findAll().get(0).getUsername();
        avatarLabel.setIcon(new ImageIcon(AvatarUtil.createOrLoadUserAvatar(currentUsername).getScaledInstance(35, 35, Image.SCALE_SMOOTH)));


        avatarLabel.revalidate();
        avatarLabel.repaint();
    }

    public void show(int who)
    {
        switch (who)
        {
            case CHAT:
            {
                selectedTab = CHAT;
                chatLabel.setIcon(IconUtil.getIcon(this, "/image/chat_active.png", true));
                contactsLabel.setIcon(IconUtil.getIcon(this, "/image/contacts_normal.png", true));
                collectionLabel.setIcon(IconUtil.getIcon(this, "/image/collection_normal.png", true));
                ChatRoomsPanel.getContext().getListPanel().showPanel(ListPanel.CHAT);

                if (ChatPanel.CHAT_ROOM_OPEN_ID == null && ChatPanel.CHAT_ROOM_OPEN_ID.isEmpty())
                {
                    RightPanel.getContext().showPanel(RightPanel.TIP);
                }

                RightPanel.getContext().showPanel(RightPanel.TIP);
                TitlePanel.getContext().showAppTitle();
                break;
            }
            case CONTACTS:
            {
                selectedTab = CONTACTS;
                chatLabel.setIcon(IconUtil.getIcon(this, "/image/chat_normal.png", true));
                contactsLabel.setIcon(IconUtil.getIcon(this, "/image/contacts_active.png", true));
                collectionLabel.setIcon(IconUtil.getIcon(this, "/image/collection_normal.png", true));
                ChatRoomsPanel.getContext().getListPanel().showPanel(ListPanel.CONTACTS);
                RightPanel.getContext().showPanel(RightPanel.TIP);
                RoomsPanel.getContext().restoreActiveItem();
                TitlePanel.getContext().showAppTitle();
                RoomsPanel.getContext().clearSelectedViewHolder();
                break;
            }
            case COLLECTION:
            {
                selectedTab = COLLECTION;
                chatLabel.setIcon(IconUtil.getIcon(this, "/image/chat_normal.png", true));
                contactsLabel.setIcon(IconUtil.getIcon(this, "/image/contacts_normal.png", true));
                collectionLabel.setIcon(IconUtil.getIcon(this, "/image/collection_active.png", true));
                ChatRoomsPanel.getContext().getListPanel().showPanel(ListPanel.COLLECTIONS);
                RightPanel.getContext().showPanel(RightPanel.TIP);
                RoomsPanel.getContext().restoreActiveItem();
                TitlePanel.getContext().showAppTitle();
                RoomsPanel.getContext().clearSelectedViewHolder();
                break;
            }

        }
    }

    public static NavPanel getContext()
    {
        return context;
    }

    /**
     * 设置未读消息数
     *
     * @param count 如果 > 0, 则显示消息气泡, 否则隐藏气泡
     */
    public void setUnreadMessageCount(int count)
    {
        updateUnreadMessageIcon(count, newMsgIcon);
    }

    /**
     * 设置未读消息数
     *
     * @param count 如果 > 0, 则显示消息气泡, 否则隐藏气泡
     */
    public void setUnreadCollectionMessageCount(int count)
    {
        updateUnreadMessageIcon(count, collectionNewMsgIcon);
    }

    private void updateUnreadMessageIcon(int count, JLabel msgIcon)
    {
        if (count < 1)
        {
            msgIcon.setIcon(null);
            msgIcon.setText("");
        } else
        {
            String txt = count > 99 ? "99+" : count + "";
            int fontWidth = fontMetrics.stringWidth(txt);
            if (fontWidth < 20)
            {
                fontWidth += 15;
            }

            msgIcon.setPreferredSize(new Dimension(fontWidth, 15));
            msgIcon.setIcon(IconUtil.getIcon(this, "/image/count_bg.png", true));
            msgIcon.setText(txt);
        }
    }


}

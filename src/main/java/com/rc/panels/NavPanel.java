package com.rc.panels;

import com.rc.app.Launcher;
import com.rc.components.Colors;
import com.rc.components.GBC;
import com.rc.components.RCListView;
import com.rc.components.message.MainOperationPopupMenu;
import com.rc.db.model.CurrentUser;
import com.rc.db.service.CurrentUserService;
import com.rc.entity.RoomItem;
import com.rc.frames.MainFrame;
import com.rc.frames.SystemConfigDialog;
import com.rc.listener.AbstractMouseListener;
import com.rc.listener.WindowMouseListener;
import com.rc.panels.collections.BugListPanel;
import com.rc.panels.collections.TaskListPanel;
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
public class NavPanel extends JPanel
{
    private static NavPanel context;
    private final FontMetrics fontMetrics;

    private JLabel avatar;
    private JLabel chatLabel;
    private JLabel contactsLabel;
    private JLabel collectionLabel;
    private JLabel menuIcon;
    private MainOperationPopupMenu mainOperationPopupMenu;

    public JPanel chatNewMsgPanel;
    public JLabel newMsgIcon;

    public JPanel collectionNewMsgPanel;
    public JLabel collectionNewMsgIcon;


    private ImageIcon chatIconActive;
    private ImageIcon chatIconNormal;
    private ImageIcon contactIconNormal;
    private ImageIcon contactIconActive;
    private ImageIcon collectionIconNormal;
    private ImageIcon collectionIconActive;

    private long chatLabelLastClickTime;

    private NavPanel.TabItemClickListener clickListener;

    public static final int CHAT = 1;
    public static final int CONTACTS = 2;
    public static final int COLLECTION = 3;

    private CurrentUserService currentUserService = Launcher.currentUserService;

    public NavPanel()
    {
        context = this;
        fontMetrics = getFontMetrics(getFont());

        initComponents();
        setListeners();
        initView();
    }

    private void setListeners()
    {
        menuIcon.addMouseListener(new AbstractMouseListener()
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

        avatar.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    /*SystemConfigDialog dialog = new SystemConfigDialog(MainFrame.getContext(), true);
                    dialog.setVisible(true);*/
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
                }
                else
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
        System.out.println(roomsListView.getScrollPosition());
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
                }
                else
                {
                    targetPos = pos;
                    break;
                }
            }

            roomsListView.scrollToPosition(targetPos);
        }
    }


    private void initComponents()
    {
        Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
        List<CurrentUser> users = currentUserService.findAll();
        String currentUsername = null;
        if (users.size() < 1)
        {
            currentUsername = Launcher.currentUser.getUsername();
        }
        else
        {
            currentUsername = users.get(0).getUsername();
        }

        avatar = new JLabel();
        avatar.setIcon(new ImageIcon(AvatarUtil.createOrLoadUserAvatar(currentUsername).getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
        avatar.setCursor(handCursor);

        clickListener = new NavPanel.TabItemClickListener();

        chatIconActive = new ImageIcon(getClass().getResource("/image/chat_active.png"));
        chatIconNormal = new ImageIcon(getClass().getResource("/image/chat_normal.png"));
        chatLabel = new JLabel();
        chatLabel.setIcon(chatIconActive);
        chatLabel.setHorizontalAlignment(JLabel.CENTER);
        chatLabel.setCursor(handCursor);
        chatLabel.addMouseListener(clickListener);

        newMsgIcon = buildMsgIcon();
        newMsgIcon.setCursor(handCursor);

        collectionNewMsgIcon = buildMsgIcon();
        collectionNewMsgIcon.setCursor(handCursor);

        contactIconNormal = new ImageIcon(getClass().getResource("/image/contacts_normal.png"));
        contactIconActive = new ImageIcon(getClass().getResource("/image/contacts_active.png"));
        contactsLabel = new JLabel();
        contactsLabel.setIcon(contactIconNormal);
        contactsLabel.setHorizontalAlignment(JLabel.CENTER);
        contactsLabel.setCursor(handCursor);
        contactsLabel.addMouseListener(clickListener);


        collectionIconNormal = new ImageIcon(getClass().getResource("/image/me_normal.png"));
        collectionIconActive = new ImageIcon(getClass().getResource("/image/me_active.png"));
        collectionLabel = new JLabel();
        collectionLabel.setIcon(collectionIconNormal);
        collectionLabel.setHorizontalAlignment(JLabel.CENTER);
        collectionLabel.setCursor(handCursor);
        collectionLabel.addMouseListener(clickListener);

        menuIcon = new JLabel();
        menuIcon.setIcon(new ImageIcon(getClass().getResource("/image/options.png")));
        menuIcon.setForeground(Colors.FONT_WHITE);
        menuIcon.setCursor(handCursor);

        chatNewMsgPanel = new JPanel();
        chatNewMsgPanel.setLayout(new GridBagLayout());
        chatNewMsgPanel.setBackground(Colors.DARK);
        chatNewMsgPanel.add(newMsgIcon, new GBC(0, 0).setWeight(1, 1).setFill(GBC.VERTICAL).setAnchor(GBC.EAST).setInsets(0, 0, 16, 0));
        chatNewMsgPanel.add(chatLabel, new GBC(0, 1).setWeight(1, 1).setFill(GBC.BOTH).setInsets(-45, 0, 0, 0));

        collectionNewMsgPanel = new JPanel();
        collectionNewMsgPanel.setLayout(new GridBagLayout());
        collectionNewMsgPanel.setBackground(Colors.DARK);
        collectionNewMsgPanel.add(collectionNewMsgIcon, new GBC(0, 0).setWeight(1, 1).setFill(GBC.VERTICAL).setAnchor(GBC.EAST).setInsets(0, 0, 16, 0));


        collectionNewMsgPanel.add(collectionLabel, new GBC(0, 1).setWeight(1, 1).setFill(GBC.BOTH).setInsets(-45, 0, 0, 0));

        mainOperationPopupMenu = new MainOperationPopupMenu();
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

    private void initView()
    {
        this.setBackground(Colors.DARK);
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(60, MainFrame.DEFAULT_HEIGHT));


        add(avatar, new GBC(0, 0).setFill(GBC.NONE).setAnchor(GBC.NORTH).setWeight(1, 1).setInsets(20, 0, 0, 0));
        add(chatNewMsgPanel, new GBC(0, 1).setFill(GBC.NONE).setAnchor(GBC.NORTH).setWeight(1, 10).setInsets(25, 0, 0, 0).setIpad(25, 25));
        add(contactsLabel, new GBC(0, 2).setFill(GBC.NONE).setAnchor(GBC.NORTH).setWeight(1, 20).setInsets(15, 0, 0, 0).setIpad(25, 25));
        add(collectionNewMsgPanel, new GBC(0, 3).setFill(GBC.NONE).setAnchor(GBC.NORTH).setWeight(1, 330).setInsets(10, 0, 0, 2).setIpad(25, 25));
        add(menuIcon, new GBC(0, 4).setFill(GBC.SOUTH).setAnchor(GBC.NORTH).setWeight(1, 10).setInsets(15, 0, 0, 0));

    }

    class TabItemClickListener extends MouseAdapter
    {

        @Override
        public void mouseReleased(MouseEvent e)
        {
            if (e.getButton() == MouseEvent.BUTTON1)
            {

                // 搜索框内容清空
                SearchPanel.getContext().clearSearchText();

                if (e.getComponent() == chatLabel)
                {
                    show(CHAT);
                } else if (e.getComponent() == contactsLabel)
                {
                    show(CONTACTS);
                } else if (e.getComponent() == collectionLabel)
                {
                    show(COLLECTION);

                    //NotificationUtil.show("Swsjxy4PXXJxd8dXk", IconUtil.getIcon(this, "/image/avatar.jpg", 50, 50), "信息技术部", "Jogen", "(后台服务) 自动部署 ZXJ-AbsProjectManagementService-DEV 成功");

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

    int i = 0;

    public void reloadAvatar()
    {
        String currentUsername = currentUserService.findAll().get(0).getUsername();
        avatar.setIcon(new ImageIcon(AvatarUtil.createOrLoadUserAvatar(currentUsername).getScaledInstance(35, 35, Image.SCALE_SMOOTH)));


        avatar.revalidate();
        avatar.repaint();
    }

    public void show(int who)
    {
        switch (who)
        {
            case CHAT:
            {
                chatLabel.setIcon(chatIconActive);
                contactsLabel.setIcon(contactIconNormal);
                collectionLabel.setIcon(collectionIconNormal);
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
                chatLabel.setIcon(chatIconNormal);
                contactsLabel.setIcon(contactIconActive);
                collectionLabel.setIcon(collectionIconNormal);
                ChatRoomsPanel.getContext().getListPanel().showPanel(ListPanel.CONTACTS);
                RightPanel.getContext().showPanel(RightPanel.TIP);
                RoomsPanel.getContext().restoreActiveItem();
                TitlePanel.getContext().showAppTitle();
                RoomsPanel.getContext().clearSelectedViewHolder();
                break;
            }
            case COLLECTION:
            {
                chatLabel.setIcon(chatIconNormal);
                contactsLabel.setIcon(contactIconNormal);
                collectionLabel.setIcon(collectionIconActive);
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
     * @param count 如果 > 0, 则显示消息气泡, 否则隐藏气泡
     */
    public void setUnreadMessageCount(int count)
    {
        updateUnreadMessageIcon(count, newMsgIcon);
    }

    /**
     * 设置未读消息数
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
        }
        else
        {
            String txt = count > 99 ? "99+": count + "";
            int fontWidth = fontMetrics.stringWidth(txt);
            if (fontWidth < 20)
            {
                fontWidth += 15;
            }

            msgIcon.setPreferredSize(new Dimension( fontWidth, 15));
            msgIcon.setIcon(IconUtil.getIcon(this, "/image/count_bg.png"));
            msgIcon.setText(txt);
        }
    }




}

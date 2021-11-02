package com.rc.panels;

import com.rc.app.Launcher;
import com.rc.res.Colors;
import com.rc.components.GBC;
import com.rc.components.message.MainOperationPopupMenu;
import com.rc.db.service.CurrentUserService;
import com.rc.frames.SystemConfigDialog;
import com.rc.listener.AbstractMouseListener;
import com.rc.utils.AvatarUtil;
import com.rc.utils.FontUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by song on 17-5-29.
 *
 * 下图 #RightPanel# 对应的位置<br/>
 *
 *  显示 个人头像、用户名以及主菜单按钮
 *
 * <P>推荐使用Menlo或Consolas字体</P>
 * ┌────────────────────────┬────────────────────────────────────────────────────────┐
 * │                        │  Room Title                                         ≡  │
 * │      #MyInfoPanel#     │────────────────────────────────────────────────────────┤
 * │                        │                                                        │
 * ├────────────────────────┤                     message time                       │
 * │    search              │  ┌──┐ ┌────────────┐                                   │
 * ├────────────────────────┤  └──┘ │  message   │                                   │
 * │  ▆    │    ▆   │   ▆   │       └────────────┘                                   │
 * ├────────────────────────┤                                                        │
 * │ ┌──┐ name         14:01│                                                        │
 * │ └──┘ message        99+│                     message time                       │
 * ├────────────────────────┤                                    ┌────────────┐ ┌──┐ │
 * │                        │                                    │  message   │ └──┘ │
 * │                        │                                    └────────────┘      │
 * │                        │                                                        │
 * │          Room          │                                                        │
 * │                        │────────────────────────────────────────────────────────┤
 * │                        │  ▆   ▆   ▆                                             │
 * │          List          │                                                        │
 * │                        │                                                        │
 * │                        │                                                ┌─────┐ │
 * │                        │                                                └─────┘ │
 * └────────────────────────┴────────────────────────────────────────────────────────┘
 */
public class MyInfoPanel extends ParentAvailablePanel
{
    private static MyInfoPanel context;

    private JLabel avatar;
    private JLabel username;
    private JLabel menuIcon;
    private CurrentUserService currentUserService = Launcher.currentUserService;

    MainOperationPopupMenu mainOperationPopupMenu;
    private String currentUsername;


    public MyInfoPanel(JPanel parent)
    {
        super(parent);
        context = this;

        initComponents();
        setListeners();
        initView();
    }


    private void initComponents()
    {

        //GImage.setBorder(new SubtleSquareBorder(true));
        currentUsername = currentUserService.findAll().get(0).getUsername();
        avatar = new JLabel();
        avatar.setIcon(new ImageIcon(AvatarUtil.createOrLoadUserAvatar(currentUsername).getScaledInstance(50,50,Image.SCALE_SMOOTH)));

        avatar.setPreferredSize(new Dimension(50, 50));
        avatar.setCursor(new Cursor(Cursor.HAND_CURSOR));


        username = new JLabel();
        username.setText(currentUsername);
        username.setFont(FontUtil.getDefaultFont(16));
        username.setForeground(Colors.FONT_WHITE);


        menuIcon = new JLabel();
        menuIcon.setIcon(new ImageIcon(getClass().getResource("/image/options.png")));
        menuIcon.setForeground(Colors.FONT_WHITE);
        menuIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));


        mainOperationPopupMenu = new MainOperationPopupMenu();
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
                    mainOperationPopupMenu.show(component, -112, 50);
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
    }

    private void initView()
    {
        this.setBackground(Colors.BG_GRAY);
        this.setLayout(new GridBagLayout());

        add(avatar, new GBC(0, 0).setFill(GBC.NONE).setWeight(2, 1));
        add(username, new GBC(1, 0).setFill(GBC.BOTH).setWeight(7, 1));
        add(menuIcon, new GBC(2, 0).setFill(GBC.BOTH).setWeight(1, 1));
    }
}

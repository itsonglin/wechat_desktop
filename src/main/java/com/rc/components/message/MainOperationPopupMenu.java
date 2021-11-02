package com.rc.components.message;

import com.rc.components.Colors;
import com.rc.components.RCMainOperationMenuItemUI;
import com.rc.frames.CreateGroupDialog;
import com.rc.frames.MainFrame;
import com.rc.frames.SystemConfigDialog;
import com.rc.utils.IconUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

import static com.rc.app.Launcher.APP_NAME;

/**
 * Created by song on 2017/6/5.
 */
public class MainOperationPopupMenu extends JPopupMenu
{
    public MainOperationPopupMenu()
    {
        initMenuItem();
    }

    private void initMenuItem()
    {
        JMenuItem item1 = new JMenuItem("创建群聊");
        JMenuItem item2 = new JMenuItem("设置");
        JMenuItem item3 = new JMenuItem("退出");

        item1.setUI(new RCMainOperationMenuItemUI());
        item1.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                showCreateGroupDialog();
            }
        });
        ImageIcon icon1 = IconUtil.getIcon(this, "/image/chat.png", 20, 20, false);
        item1.setIcon(icon1);
        item1.setIconTextGap(5);


        item2.setUI(new RCMainOperationMenuItemUI());
        item2.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //System.out.println("系统设置");
                SystemConfigDialog.display();
            }
        });
        ImageIcon icon2 = IconUtil.getIcon(this, "/image/setting.png",20, 20, false);
        item2.setIcon(icon2);
        item2.setIconTextGap(5);


        item3.setUI(new RCMainOperationMenuItemUI());
        item3.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int ret = JOptionPane.showConfirmDialog(MainFrame.getContext(), "确认退出"+APP_NAME+"？", "确认退出", JOptionPane.YES_NO_OPTION);
                if (ret == JOptionPane.YES_OPTION)
                {
                    MainFrame.getContext().exitApp();
                }
            }
        });
        ImageIcon icon3 = IconUtil.getIcon(this, "/image/shutdown.png", 18, 20, false);
        item3.setIcon(icon3);
        item3.setIconTextGap(5);


        this.add(item1);
        this.add(item2);
        this.add(item3);

        setBorder(new LineBorder(Colors.SCROLL_BAR_TRACK_LIGHT));
        setBackground(Colors.FONT_WHITE);
    }

    /**
     * 弹出创建群聊窗口
     */
    private void showCreateGroupDialog()
    {
        CreateGroupDialog dialog = new CreateGroupDialog(null, true);
        dialog.setVisible(true);

        /*ShadowBorderDialog shadowBorderDialog = new ShadowBorderDialog(MainFrame.getContext(), true, dialog);
        shadowBorderDialog.setVisible(true);*/
    }
}

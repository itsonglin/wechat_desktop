package com.rc.panels;


import com.rc.app.Launcher;
import com.rc.components.Colors;
import com.rc.components.GBC;
import com.rc.components.RCButton;
import com.rc.components.VerticalFlowLayout;
import com.rc.db.model.ContactsUser;
import com.rc.db.model.Room;
import com.rc.db.service.ContactsUserService;
import com.rc.db.service.RoomService;
import com.rc.frames.MainFrame;
import com.rc.utils.AvatarUtil;
import com.rc.utils.FontUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by song on 2017/6/15.
 */
public class UserInfoPanel extends ParentAvailablePanel
{
    private JPanel contentPanel;
    private JLabel imageLabel;
    private JLabel nameLabel;
    private RCButton button;

    private String username;
    private RoomService roomService = Launcher.roomService;
    private ContactsUserService contactsUserService = Launcher.contactsUserService;

    public UserInfoPanel(JPanel parent)
    {
        super(parent);
        initComponents();
        initView();
        setListeners();
    }

    private void initComponents()
    {
        contentPanel = new JPanel();
        contentPanel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.CENTER, 0, 20, true, false));

        imageLabel = new JLabel();
        ImageIcon icon = new ImageIcon(AvatarUtil.createOrLoadUserAvatar("song").getScaledInstance(100,100, Image.SCALE_SMOOTH));
        imageLabel.setIcon(icon);

        nameLabel = new JLabel();
        nameLabel.setText("Song");
        nameLabel.setFont(FontUtil.getDefaultFont(20));

        button = new RCButton("发消息", Colors.MAIN_COLOR, Colors.MAIN_COLOR_DARKER, Colors.MAIN_COLOR_DARKER);
        button.setBackground(Colors.PROGRESS_BAR_START);
        button.setPreferredSize(new Dimension(200, 40));
        button.setFont(FontUtil.getDefaultFont(16));

    }

    private void initView()
    {
        this.setLayout(new GridBagLayout());

        JPanel avatarNamePanel = new JPanel();
        avatarNamePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        avatarNamePanel.add(imageLabel, BorderLayout.WEST);
        avatarNamePanel.add(nameLabel, BorderLayout.CENTER);

        //add(avatarNamePanel, new GBC(0,0).setAnchor(GBC.CENTER).setWeight(1,1).setInsets(0,0,0,0));
        //add(button, new GBC(0,1).setAnchor(GBC.CENTER).setWeight(1,1).setInsets(0,0,0,0));
        contentPanel.add(avatarNamePanel);
        contentPanel.add(button);

        add(contentPanel, new GBC(0,0).setWeight(1,1).setAnchor(GBC.CENTER).setInsets(0,0,250,0));
    }

    public void setUsername(String username)
    {
        this.username = username;
        nameLabel.setText(username);

        ImageIcon icon = new ImageIcon(AvatarUtil.createOrLoadUserAvatar(username).getScaledInstance(100,100, Image.SCALE_SMOOTH));
        imageLabel.setIcon(icon);
    }

    private void setListeners()
    {
        button.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {

                openOrCreateDirectChat();
                super.mouseClicked(e);
            }
        });
    }

    private void openOrCreateDirectChat()
    {
        ContactsUser user  = contactsUserService.find("username", username).get(0);
        String userId = user.getUserId();
        Room room = roomService.findRelativeRoomIdByUserId(userId);

        // 房间已存在，直接打开，否则发送请求创建房间
        if (room != null)
        {
            ChatPanel.getContext().enterRoom(room.getRoomId());
        }else
        {
            createDirectChat(user.getName());
        }
    }

    /**
     * 创建直接聊天
     *
     * @param username
     */
    private void createDirectChat(String username)
    {
        JOptionPane.showMessageDialog(MainFrame.getContext(), "发起聊天", "发起聊天", JOptionPane.INFORMATION_MESSAGE);
    }



}

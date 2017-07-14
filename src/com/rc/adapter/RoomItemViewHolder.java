package com.rc.adapter;

import com.rc.components.Colors;
import com.rc.components.GBC;
import com.rc.components.RCBorder;
import com.rc.utils.AvatarUtil;
import com.rc.utils.FontUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Created by song on 17-5-30.
 */
public class RoomItemViewHolder extends ViewHolder
{
    public JLabel avatar = new JLabel();
    public JLabel roomName = new JLabel();
    public JLabel brief = new JLabel();
    public JPanel nameBrief = new JPanel();
    public JLabel time = new JLabel();
    public JLabel unreadCount = new JLabel();
    public JPanel timeUnread = new JPanel();
    private Object tag;

    public RoomItemViewHolder()
    {
        initComponents();
        initView();

    }

    private void initComponents()
    {
        setPreferredSize(new Dimension(100, 64));
        setBackground(Colors.DARK);
        setBorder(new RCBorder(RCBorder.BOTTOM));
        setOpaque(true);
        setForeground(Colors.FONT_WHITE);

//        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/image/avatar.jpg"));
//        imageIcon.setImage(imageIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
//        avatar.setIcon(imageIcon);


        roomName.setFont(FontUtil.getDefaultFont(14));
        roomName.setForeground(Colors.FONT_WHITE);

        brief.setForeground(Colors.FONT_GRAY);
        brief.setFont(FontUtil.getDefaultFont(12));

        nameBrief.setLayout(new BorderLayout());
        nameBrief.setBackground(Colors.DARK);
        nameBrief.add(roomName, BorderLayout.NORTH);
        nameBrief.add(brief, BorderLayout.CENTER);

        time.setForeground(Colors.FONT_GRAY);
        time.setFont(FontUtil.getDefaultFont(12));

        unreadCount.setIcon(new ImageIcon(getClass().getResource("/image/count_bg.png")));
        unreadCount.setFont(FontUtil.getDefaultFont(12));
        unreadCount.setPreferredSize(new Dimension(10, 10));
        unreadCount.setForeground(Colors.FONT_WHITE);
        unreadCount.setHorizontalTextPosition(SwingConstants.CENTER);
        unreadCount.setHorizontalAlignment(SwingConstants.CENTER);
        unreadCount.setVerticalAlignment(SwingConstants.CENTER);
        unreadCount.setVerticalTextPosition(SwingConstants.CENTER);

        timeUnread = new JPanel();
        timeUnread.setLayout(new BorderLayout());
        timeUnread.setBackground(Colors.DARK);
        timeUnread.add(time, BorderLayout.NORTH);
        timeUnread.add(unreadCount, BorderLayout.CENTER);

    }

    private void initView()
    {
        setLayout(new GridBagLayout());
        add(avatar, new GBC(0, 0).setWeight(2, 1).setFill(GBC.BOTH).setInsets(0, 5, 0, 0));
        add(nameBrief, new GBC(1, 0).setWeight(100, 1).setFill(GBC.BOTH).setInsets(5, 5, 0, 0));
        add(timeUnread, new GBC(2, 0).setWeight(1, 1).setFill(GBC.BOTH).setInsets(5, 0, 0, 0));

    }


    public Object getTag()
    {
        return tag;
    }

    public void setTag(Object tag)
    {
        this.tag = tag;
    }
}

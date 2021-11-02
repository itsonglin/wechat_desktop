package com.rc.adapter;

import com.rc.components.*;
import com.rc.utils.FontUtil;
import com.rc.utils.IconUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Created by song on 17-5-30.
 */
public class RoomItemViewHolder extends ViewHolder
{
    private final FontMetrics fontMetrics;
    public JPanel avatarNewMsg = new JPanel();
    public JPanel nameBrief = new JPanel();
    public JPanel timeUnread = new JPanel();

    public JLabel avatar = new JLabel();
    public JLabel newMsgIcon = new JLabel();
    public JLabel roomName = new RCLabel();
    public NotificationTextArea brief = new NotificationTextArea(120);
    public JLabel time = new JLabel();
    public JLabel bell = new JLabel();
    //public JLabel unreadCount = new JLabel();
    private Object tag;

    public RoomItemViewHolder()
    {
        super();
        initComponents();
        initView();
        fontMetrics = getFontMetrics(getFont());
    }

    private void initComponents()
    {
        setPreferredSize(new Dimension(100, 65));
        setBackground(Colors.BG_GRAY);
        //setBorder(new RCBorder(RCBorder.BOTTOM));
        setOpaque(true);
        setForeground(Colors.FONT_BLACK);

        newMsgIcon.setIcon(IconUtil.getIcon(this, "/image/count_bg.png", 10, 10, true));
        newMsgIcon.setPreferredSize(new Dimension(10, 10));
        newMsgIcon.setFont(FontUtil.getDefaultFont(12));
        newMsgIcon.setForeground(Colors.FONT_WHITE);
        newMsgIcon.setHorizontalTextPosition(SwingConstants.CENTER);
        newMsgIcon.setHorizontalAlignment(SwingConstants.CENTER);
        newMsgIcon.setVerticalAlignment(SwingConstants.CENTER);
        newMsgIcon.setVerticalTextPosition(SwingConstants.CENTER);

        avatarNewMsg.setLayout(new GridBagLayout());
        avatarNewMsg.setBackground(Colors.BG_GRAY);
        avatarNewMsg.add(newMsgIcon, new GBC(0, 0).setWeight(1, 1).setFill(GBC.VERTICAL).setAnchor(GBC.EAST).setInsets(0, 0, 16, 0));
        avatarNewMsg.add(avatar, new GBC(0, 1).setWeight(1, 1).setFill(GBC.BOTH).setInsets(-45, 0, 0, 0));

        roomName.setFont(FontUtil.getDefaultFont(15,Font.BOLD));
        roomName.setForeground(Colors.FONT_BLACK);

        brief.setForeground(Colors.FONT_GRAY);
        brief.setFont(FontUtil.getDefaultFont(13));
        brief.setBackground(Colors.BG_GRAY);

        bell.setIcon(IconUtil.getIcon(this, "/image/bell.png", 12, 12, true));
        bell.setPreferredSize(new Dimension(15, 15));

        nameBrief.setLayout(new GridBagLayout());
        nameBrief.setBackground(Colors.BG_GRAY);
        nameBrief.add(roomName, new GBC(0, 0).setWeight(4, 1).setFill(GBC.BOTH).setInsets(0, 5, 0, 0));
        nameBrief.add(brief, new GBC(0, 1).setWeight(1, 1).setFill(GBC.BOTH).setInsets(0, 5, 8, 0));

        time.setForeground(Colors.FONT_GRAY);
        time.setFont(FontUtil.getDefaultFont(12));

        /*unreadCount.setIcon(new ImageIcon(getClass().getResource("/image/count_bg.png")));
        unreadCount.setFont(FontUtil.getDefaultFont(12));
        unreadCount.setPreferredSize(new Dimension(10, 10));
        unreadCount.setForeground(Colors.FONT_WHITE);
        unreadCount.setHorizontalTextPosition(SwingConstants.CENTER);
        unreadCount.setHorizontalAlignment(SwingConstants.CENTER);
        unreadCount.setVerticalAlignment(SwingConstants.CENTER);
        unreadCount.setVerticalTextPosition(SwingConstants.CENTER);*/

        timeUnread = new JPanel();
        timeUnread.setLayout(new GridBagLayout());
        timeUnread.setBackground(Colors.BG_GRAY);
        timeUnread.add(time, new GBC(0, 0).setWeight(4, 1).setFill(GBC.BOTH).setInsets(0, 5, 0, 10));
        timeUnread.add(bell,new GBC(0, 1).setWeight(1, 1).setAnchor(GBC.EAST).setInsets(0, 0, 10, 8));

    }

    private void initView()
    {
        setLayout(new GridBagLayout());
        add(avatarNewMsg, new GBC(0, 0).setWeight(2, 1).setFill(GBC.BOTH).setInsets(0, 10, 0, 0).setIpad(3, 0));
        add(nameBrief, new GBC(1, 0).setWeight(100, 1).setFill(GBC.BOTH).setInsets(10, 2, 0, 0));
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

    public FontMetrics getFontMetrics()
    {
        return fontMetrics;
    }
}

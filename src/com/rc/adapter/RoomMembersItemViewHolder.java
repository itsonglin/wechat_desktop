package com.rc.adapter;


import com.rc.components.Colors;
import com.rc.components.RCBorder;
import com.rc.utils.FontUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Created by song on 07/06/2017.
 */
public class RoomMembersItemViewHolder extends ViewHolder
{
    public JLabel avatar = new JLabel();
    public JLabel roomName = new JLabel();

    public RoomMembersItemViewHolder()
    {
        initView();
    }

    private void initView()
    {
        setPreferredSize(new Dimension(40, 45));
        setBackground(Colors.WINDOW_BACKGROUND_LIGHT);
        setBorder(new RCBorder(RCBorder.BOTTOM, new Color(235, 235, 235)));
        setOpaque(true);

        // 名字
        roomName = new JLabel();
        roomName.setFont(FontUtil.getDefaultFont(13));
        roomName.setForeground(Colors.FONT_BLACK);

        /*setLayout(new GridBagLayout());
        add(avatar, new GBC(0, 0).setWeight(1, 1).setFill(GBC.BOTH).setInsets(0,5,0,0).setAnchor(GBC.CENTER));
        add(username, new GBC(1, 0).setWeight(10, 1).setFill(GBC.BOTH).setInsets(0,0,0,5));*/

        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 6));
        JPanel avatarPanel = new JPanel();
        avatarPanel.add(avatar);
        add(avatar);
        add(roomName);
    }

}

package com.rc.adapter;

import com.rc.components.Colors;
import com.rc.components.GBC;
import com.rc.components.RCBorder;
import com.rc.components.RCLabel;
import com.rc.utils.FontUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Created by song on 17-5-30.
 */
public class ContactsItemViewHolder extends ViewHolder
{
    public JLabel avatar = new JLabel();
    public RCLabel roomName = new RCLabel();

    public ContactsItemViewHolder()
    {
        initComponents();
        initView();
    }

    private void initComponents()
    {
        setPreferredSize(new Dimension(100, 50));
        setBackground(Colors.BG_GRAY);
        setOpaque(true);
        setForeground(Colors.FONT_BLACK);
        roomName.setFont(FontUtil.getDefaultFont(14));
        roomName.setForeground(Colors.FONT_BLACK);
    }

    private void initView()
    {
        setLayout(new GridBagLayout());
        add(avatar, new GBC(0, 0).setWeight(1, 1).setFill(GBC.BOTH).setInsets(0, 5, 0, 0));
        add(roomName, new GBC(1, 0).setWeight(20, 1).setFill(GBC.BOTH).setInsets(10, 0, 0, 0));
    }
}

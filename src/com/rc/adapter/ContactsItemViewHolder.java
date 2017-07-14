package com.rc.adapter;

import com.rc.components.Colors;
import com.rc.components.GBC;
import com.rc.components.RCBorder;
import com.rc.utils.FontUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Created by song on 17-5-30.
 */
public class ContactsItemViewHolder extends ViewHolder
{
    public JLabel avatar = new JLabel();
    public JLabel roomName = new JLabel();

    public ContactsItemViewHolder()
    {
        initComponents();
        initView();
    }

    private void initComponents()
    {
        setPreferredSize(new Dimension(100, 50));
        setBackground(Colors.DARK);
        setBorder(new RCBorder(RCBorder.BOTTOM));
        setOpaque(true);
        setForeground(Colors.FONT_WHITE);

       roomName.setFont(FontUtil.getDefaultFont(13));
       roomName.setForeground(Colors.FONT_WHITE);
    }

    private void initView()
    {
        setLayout(new GridBagLayout());
        add(avatar, new GBC(0, 0).setWeight(1, 1).setFill(GBC.BOTH).setInsets(0,5,0,0));
        add(roomName, new GBC(1, 0).setWeight(10, 1).setFill(GBC.BOTH));
    }
}

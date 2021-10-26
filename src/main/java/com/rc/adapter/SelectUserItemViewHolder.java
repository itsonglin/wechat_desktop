package com.rc.adapter;

import com.rc.components.Colors;
import com.rc.components.GBC;
import com.rc.components.RCBorder;
import com.rc.frames.CreateGroupDialog;
import com.rc.utils.FontUtil;
import com.rc.utils.IconUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Created by song on 17-5-30.
 */
public class SelectUserItemViewHolder extends ViewHolder
{
    public JLabel avatar = new JLabel();
    public JLabel username = new JLabel();
    public JLabel icon = new JLabel();

    public SelectUserItemViewHolder()
    {
        initComponents();
        initView();
    }

    private void initComponents()
    {
        //panelItem = new JPanel();
        setPreferredSize(new Dimension(CreateGroupDialog.DIALOG_WIDTH / 2 - 20, 50));
        setBorder(new RCBorder(RCBorder.BOTTOM, Colors.LIGHT_GRAY));
        setOpaque(true);
        setForeground(Colors.FONT_BLACK);

        username.setFont(FontUtil.getDefaultFont(13));
        username.setForeground(Colors.FONT_BLACK);

        icon.setIcon(IconUtil.getIcon(this, "/image/uncheck.png"));

        setLayout(new GridBagLayout());
        add(avatar, new GBC(0, 0).setWeight(1, 1).setFill(GBC.BOTH).setInsets(0,5,0,0));
        add(username, new GBC(1, 0).setWeight(100, 1).setFill(GBC.BOTH).setInsets(0,5,0,0));
        add(icon, new GBC(2, 0).setWeight(1, 1).setFill(GBC.BOTH).setInsets(0,0,0,5));
    }

    private void initView()
    {
        
    }
}

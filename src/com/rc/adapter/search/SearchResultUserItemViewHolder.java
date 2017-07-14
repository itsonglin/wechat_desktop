package com.rc.adapter.search;

import com.rc.components.Colors;
import com.rc.components.GBC;
import com.rc.components.HighLightLabel;
import com.rc.components.RCBorder;
import com.rc.utils.FontUtil;

import javax.swing.*;
import java.awt.*;

/**
 * 搜索结果每一个通讯录、房间项目
 * Created by song on 17-5-30.
 */
public class SearchResultUserItemViewHolder extends SearchResultItemViewHolder
{
    public JLabel avatar = new JLabel();
    public HighLightLabel name = new HighLightLabel();

    public SearchResultUserItemViewHolder()
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


        name.setFont(FontUtil.getDefaultFont(14));
        name.setForeground(Colors.FONT_WHITE);

    }

    private void initView()
    {
        setLayout(new GridBagLayout());
        add(avatar, new GBC(0, 0).setWeight(2, 1).setFill(GBC.BOTH).setInsets(0, 5, 0, 0));
        add(name, new GBC(1, 0).setWeight(100, 1).setFill(GBC.BOTH).setInsets(3, 5, 0, 0));

    }
}

package com.rc.adapter.search;

import com.rc.res.Colors;
import com.rc.components.GBC;
import com.rc.components.HighLightLabel;
import com.rc.utils.FontUtil;

import javax.swing.*;
import java.awt.*;

/**
 * 搜索结果中的每一个消息项目
 * Created by song on 17-6-22.
 */
public class SearchResultMessageItemViewHolder extends SearchResultItemViewHolder
{
    public JLabel avatar = new JLabel();
    public JLabel roomName = new JLabel();
    public HighLightLabel brief = new HighLightLabel();
    public JPanel nameBrief = new JPanel();
    public JLabel time = new JLabel();

    public SearchResultMessageItemViewHolder()
    {
        initComponents();
        initView();

    }

    private void initComponents()
    {
        setPreferredSize(new Dimension(100, 64));
        setBackground(Colors.BG_GRAY);
        //setBorder(new RCBorder(RCBorder.BOTTOM));
        setOpaque(true);
        setForeground(Colors.FONT_BLACK);


        roomName.setFont(FontUtil.getDefaultFont(14));
        roomName.setForeground(Colors.FONT_BLACK);

        brief.setForeground(Colors.FONT_GRAY);
        brief.setFont(FontUtil.getDefaultFont(12));

        nameBrief.setLayout(new BorderLayout());
        nameBrief.setBackground(Colors.BG_GRAY);
        nameBrief.add(roomName, BorderLayout.NORTH);
        nameBrief.add(brief, BorderLayout.CENTER);

        time.setForeground(Colors.FONT_GRAY);
        time.setFont(FontUtil.getDefaultFont(12));
    }

    private void initView()
    {
        setLayout(new GridBagLayout());
        add(avatar, new GBC(0, 0).setWeight(2, 1).setFill(GBC.BOTH).setInsets(0, 5, 0, 0));
        add(nameBrief, new GBC(1, 0).setWeight(100, 1).setFill(GBC.BOTH).setInsets(10, 5, 0, 0));
        add(time, new GBC(2, 0).setWeight(1, 1).setFill(GBC.BOTH).setInsets(5, 0, 5, 0).setAnchor(GBC.NORTH));

    }


}

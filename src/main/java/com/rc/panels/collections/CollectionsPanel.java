package com.rc.panels.collections;

import com.rc.res.Colors;
import com.rc.components.GBC;
import com.rc.panels.ParentAvailablePanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by song on 17-5-30.
 *
 * <P>下图 #CollectionsPanel# 对应的位置</P>
 *
 * 显示收藏列表
 *
 */
public class CollectionsPanel extends ParentAvailablePanel
{
    private JLabel tipLabel;


    public CollectionsPanel(JPanel parent)
    {
        super(parent);

        initComponents();
        initView();
    }

    private void initComponents()
    {
        tipLabel = new JLabel();
        tipLabel.setText("暂无收藏");
    }

    private void initView()
    {
        this.setBackground(Colors.BG_GRAY);
        setLayout(new GridBagLayout());

        add(tipLabel, new GBC(0, 0).setFill(GBC.HORIZONTAL).setAnchor(GBC.NORTH).setWeight(1,1).setInsets(5, 100, 0, 0));
    }
}

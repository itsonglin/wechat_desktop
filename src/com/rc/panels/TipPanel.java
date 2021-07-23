package com.rc.panels;


import com.rc.components.GBC;
import com.rc.utils.IconUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Created by song on 2017/6/15.
 *
 * <p>下图 #TipPanel# 对应的位置</p>
 *
 * 当没有在ListPanel中选定项目时，则右侧TipPanel会显示出来，TipPanel目前只包含一个灰色的Logo
 *
 * <P>推荐使用Menlo或Consolas字体</P>
 * ┌────────────────────────┬────────────────────────────────────────────────────────┐
 * │ ┌─────┐                │  Room Title                                         ≡  │
 * │ │     │ name         ≡ ├────────────────────────────────────────────────────────┤
 * │ └─────┘                │                                                        │
 * ├────────────────────────┤                                                        │
 * │    search              │                                                        │
 * ├────────────────────────┤                                                        │
 * │  ▆    │    ▆   │   ▆   │                                                        │
 * ├────────────────────────┤                                                        │
 * │ ┌──┐ name         14:01│                      #TipPanel#                        │
 * │ └──┘ message        99+│                                                        │
 * ├────────────────────────┤                                                        │
 * │                        │                                                        │
 * │                        │                                                        │
 * │                        │                                                        │
 * │          Room          │                                                        │
 * │                        │                                                        │
 * │                        │                                                        │
 * │          List          │                                                        │
 * │                        │                                                        │
 * │                        │                                                        │
 * │                        │                                                        │
 * └────────────────────────┴────────────────────────────────────────────────────────┘
 */
public class TipPanel extends ParentAvailablePanel
{
    private JLabel imageLabel;

    public TipPanel(JPanel parent)
    {
        super(parent);
        initComponents();
        initView();
    }

    private void initComponents()
    {
        imageLabel = new JLabel();
        imageLabel.setIcon(IconUtil.getIcon(this, "/image/bg.png", 140, 140));
    }

    private void initView()
    {
        setLayout(new GridBagLayout());
        add(imageLabel, new GBC(0,0).setAnchor(GBC.CENTER).setInsets(0,0,50,0));
    }

}

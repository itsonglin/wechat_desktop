package com.rc.adapter.search;

import com.rc.components.*;
import com.rc.utils.FontUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * 搜索结果中的每一个文件项目
 * Created by song on 17-6-22.
 */
public class SearchResultFileItemViewHolder extends SearchResultItemViewHolder
{
    public JLabel avatar = new JLabel();
    public HighLightLabel name = new HighLightLabel();
    public JLabel size = new JLabel();
    public RCProgressBar progressBar = new RCProgressBar(); // 进度条
    public JPanel nameProgressPanel =  new JPanel();

    public SearchResultFileItemViewHolder()
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

        name.setFont(FontUtil.getDefaultFont(12));
        name.setForeground(Colors.FONT_WHITE);

        //brief.setForeground(Colors.FONT_GRAY);
       // brief.setFont(FontUtil.getDefaultFont(12));

        name.setBorder(new LineBorder(Color.red));
        //nameBrief.add(brief, BorderLayout.CENTER);
        name.setPreferredSize(new Dimension(100, 35));

        size.setForeground(Colors.FONT_GRAY);
        size.setFont(FontUtil.getDefaultFont(12));
        //name.setPreferredSize(new Dimension(40, 50));

        progressBar.setMaximum(100);
        progressBar.setMinimum(0);
        progressBar.setValue(50);
        progressBar.setUI(new GradientProgressBarUI());
        progressBar.setPreferredSize(new Dimension(100, 3));
        progressBar.setVisible(false);
    }

    private void initView()
    {
        /*setLayout(new GridBagLayout());
        add(avatar, new GBC(0, 0).setWeight(2, 1).setFill(GBC.BOTH).setInsets(0, 5, 0, 0));
        add(name, new GBC(1, 0).setWeight(100, 1).setFill(GBC.BOTH).setInsets(5, 5, 0, 0));
        add(size, new GBC(2, 0).setWeight(1, 1).setFill(GBC.BOTH).setInsets(5, 0, 0, 0).setAnchor(GBC.NORTH));*/

        nameProgressPanel.setBackground(Colors.DARK);

        nameProgressPanel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0,0,true, false));
        nameProgressPanel.add(name);
        nameProgressPanel.add(progressBar);
        /*namePanel.setLayout(new GridBagLayout());
        namePanel.add(name, new GBC(0,0).setWeight(1,1000).setFill(GBC.BOTH));
        namePanel.add(progressBar, new GBC(0, 1).setWeight(1, 0).setFill(GBC.HORIZONTAL).setAnchor(GBC.SOUTH));*/


        setLayout(new GridBagLayout());

        add(avatar, new GBC(0, 0).setWeight(2, 1).setFill(GBC.BOTH).setInsets(0, 5, 0, 0));
        add(nameProgressPanel, new GBC(1, 0).setWeight(100, 1).setFill(GBC.BOTH).setInsets(0, 5, 0, 0));
        add(size, new GBC(2, 0).setWeight(1, 1).setFill(GBC.BOTH).setInsets(5, 3, 0, 0).setAnchor(GBC.NORTH));

        //add(size);

    }


}

package com.rc.panels.collections;

import com.rc.app.Launcher;
import com.rc.components.Colors;
import com.rc.components.GBC;
import com.rc.components.RCListView;
import com.rc.db.service.CollectionService;
import com.rc.frames.SystemConfigDialog;
import com.rc.panels.ParentAvailablePanel;
import com.rc.utils.FontUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * @author song
 * @date 19-11-14 10:59
 * @description
 * @since
 */
public abstract class CollectionListBasePanel extends ParentAvailablePanel
{
    protected JTextPane tipLabel;
    protected RCListView listView;
    protected String name;
    protected String code;


    protected CollectionService collectionService = Launcher.collectionService;

    public CollectionListBasePanel(JPanel parent, String name, String code)
    {
        super(parent);
        this.name = name;
        this.code = code;
        initComponents();
        initView();
        setListeners();
    }

    private void initComponents()
    {
        tipLabel = new JTextPane();
        tipLabel.setText("暂无" + name);
        tipLabel.setFont(FontUtil.getDefaultFont(14));
        tipLabel.setBackground(Colors.WINDOW_BACKGROUND);
        tipLabel.setEditable(false);

        tipLabel.setForeground(Colors.FONT_GRAY);
        listView = new RCListView();
    }

    private void initView()
    {
        this.setBackground(Colors.WINDOW_BACKGROUND);
        setLayout(new GridBagLayout());
        add(tipLabel, new GBC(0, 0).setFill(GBC.HORIZONTAL).setAnchor(GBC.CENTER).setWeight(1,1).setInsets(5, 100, 0, 0));
        add(listView, new GBC(0, 1).setFill(GBC.BOTH).setAnchor(GBC.NORTH).setWeight(1,300));
    }

    /**
     * 重绘整个列表
     */
    protected void notifyDataSetChanged(boolean keepSize)
    {
        listView.notifyDataSetChanged(keepSize);
    }

    /**
     * 重绘整个列表
     */
    public void notifyDataSetChanged()
    {
        initData();
    }

    protected void initData()
    {

    }

    public void hideTipLabel()
    {
        this.tipLabel.setVisible(false);
    }


    private void setListeners()
    {

    }
}

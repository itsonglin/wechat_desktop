package com.rc.components;

import com.rc.panels.BasePanel;
import com.rc.utils.FontUtil;
import com.rc.utils.IconUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.Document;
import java.awt.*;


/**
 * 搜索板块
 * @author song
 * @date 21-10-29 16:56
 * @description
 * @since
 */
public class SearchPanel extends BasePanel
{
    private JLabel iconLabel;
    private JTextField textField;

    public SearchPanel(JPanel parent)
    {
        super(parent);
    }

    @Override
    protected void initComponents()
    {
        textField = new JTextField();
        textField.setBorder(null);
        textField.setFont(FontUtil.getDefaultFont(14));

        iconLabel = new JLabel();
        ImageIcon icon = IconUtil.getIcon(this, "/image/search.png", false);
        iconLabel.setIcon(icon);
        iconLabel.setBackground(Color.RED);
    }

    @Override
    protected void initView()
    {
        this.setLayout(new GridBagLayout());
        this.add(iconLabel, new GBC(0, 0).setWeight(1, 1).setFill(GBC.HORIZONTAL).setInsets(2, 3, 0, 0));
        this.add(textField, new GBC(1, 0).setWeight(1000, 1).setFill(GBC.BOTH).setInsets(2, 4, 2, 5));

        Color bgColor = new Color(214, 214, 214, 255);
        textField.setBackground(bgColor);
        this.setBorder(new RCRoundBorder(this, bgColor, 1));
    }

    public Document getDocument()
    {
        return textField.getDocument();
    }

    public String getText()
    {
        return textField.getText();
    }

    public void setText(String s)
    {
        textField.setText(s);
    }
}

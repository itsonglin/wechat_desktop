package com.rc.components;

import com.rc.panels.BasePanel;
import com.rc.utils.FontUtil;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;


/**
 * @author song
 * @date 21-10-29 16:56
 * @description
 * @since
 */
public class RCSearchPanel extends BasePanel
{
    private JTextField textField;

    public RCSearchPanel(JPanel parent)
    {
        super(parent);
    }

    @Override
    protected void initComponents()
    {

        textField = new JTextField();
        textField.setBorder(null);
        textField.setFont(FontUtil.getDefaultFont(14));
    }

    @Override
    protected void initView()
    {
        this.setLayout(new GridBagLayout());
        this.add(textField, new GBC(0, 0).setWeight(1, 1).setFill(GBC.BOTH).setInsets(2, 10, 2, 5));

        Color bgColor = new Color(214, 214, 214, 255);
        this.setBorder(new RCRoundBorder(bgColor, 1));
        textField.setBackground(bgColor);
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

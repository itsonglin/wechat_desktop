package com.rc.components;

import com.rc.panels.BasePanel;
import com.rc.panels.SearchAndCreateGroupPanel;
import com.rc.res.Colors;
import com.rc.res.Cursors;
import com.rc.utils.FontUtil;
import com.rc.utils.IconUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * 搜索板块
 *
 * @author song
 * @date 21-10-29 16:56
 * @description
 * @since
 */
public class SearchPanel extends BasePanel
{
    private JLabel iconLabel;
    private JLabel placeholderLabel;
    private JTextField textField;
    private JLabel clearLabel;

    private Color normalBgColor ;
    private Border normalBorder;
    private Border activeBorder;

    public SearchPanel(JPanel parent)
    {
        super(parent);
    }

    @Override
    protected void initComponents()
    {
        normalBgColor = new Color(214, 214, 214, 255);
        normalBorder = new RCRoundBorder(this, normalBgColor, 1);
        activeBorder = new RCRoundBorder(this, Colors.WINDOW_BACKGROUND_LIGHT, 1);

        textField = new JTextField();
        textField.setBorder(null);
        textField.setFont(FontUtil.getDefaultFont(14));

        iconLabel = new JLabel();
        iconLabel.setIcon(IconUtil.getIcon(this, "/image/search.png", false));

        placeholderLabel = new JLabel("搜索");
        placeholderLabel.setFont(FontUtil.getDefaultFont(14));
        placeholderLabel.setForeground(Colors.FONT_GRAY);
        placeholderLabel.setCursor(Cursors.TEXT_CURSOR);


        clearLabel = new JLabel();
        clearLabel.setIcon(IconUtil.getIcon(this, "/image/clear_text.png", true));

        this.setCursor(Cursors.TEXT_CURSOR);
        clearLabel.setCursor(Cursors.HAND_CURSOR);
    }

    @Override
    protected void initView()
    {
        this.setLayout(new GridBagLayout());
        this.add(iconLabel, new GBC(0, 0).setWeight(1, 1).setFill(GBC.HORIZONTAL).setInsets(2, 3, 0, 0));
        this.add(placeholderLabel, new GBC(1, 0).setWeight(1, 1).setFill(GBC.HORIZONTAL).setInsets(0, 3, 0, 0));
        this.add(textField, new GBC(2, 0).setWeight(1000, 1).setFill(GBC.BOTH).setInsets(2, 4, 2, 5));
        this.add(clearLabel, new GBC(3, 0).setWeight(1, 1).setFill(GBC.BOTH).setInsets(2, 4, 2, 5));

        textField.setBackground(normalBgColor);
        this.setBorder(normalBorder);
    }

    @Override
    protected void setListeners()
    {
        textField.addFocusListener(new FocusListener()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
                textField.setBackground(Colors.WINDOW_BACKGROUND_LIGHT);
                SearchPanel.this.setBorder(activeBorder);

                placeholderLabel.setVisible(false);
                clearLabel.setVisible(true);
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                textField.setBackground(normalBgColor);
                SearchPanel.this.setBorder(normalBorder);

                placeholderLabel.setVisible(true);
                clearLabel.setVisible(false);
            }
        });

        placeholderLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                textField.requestFocus();
                super.mouseClicked(e);
            }
        });

        clearLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                super.mouseClicked(e);
                releaseFocus();
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                super.mouseEntered(e);
                clearLabel.setIcon(IconUtil.getIcon(this, "/image/clear_text_active.png", true));
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                clearLabel.setIcon(IconUtil.getIcon(this, "/image/clear_text.png", true));
            }
        });


    }

    /**
     * 释放输入焦点
     */
    private void releaseFocus()
    {
        SearchAndCreateGroupPanel parent = (SearchAndCreateGroupPanel) getParentPanel();
        parent.getCreateGroupButton().requestFocus();
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

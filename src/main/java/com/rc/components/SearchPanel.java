package com.rc.components;

import com.rc.panels.BasePanel;
import com.rc.res.Colors;
import com.rc.res.Cursors;
import com.rc.utils.FontUtil;
import com.rc.utils.IconUtil;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


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
    private JLabel placeholderLabel;
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

        placeholderLabel = new JLabel("搜索");
        placeholderLabel.setFont(FontUtil.getDefaultFont(14));
        placeholderLabel.setForeground(Colors.FONT_GRAY);
        placeholderLabel.setCursor(Cursors.TEXT_CURSOR);

        this.setCursor(Cursors.TEXT_CURSOR);
    }

    @Override
    protected void initView()
    {
        this.setLayout(new GridBagLayout());
        this.add(iconLabel, new GBC(0, 0).setWeight(1, 1).setFill(GBC.HORIZONTAL).setInsets(2, 3, 0, 0));
        this.add(placeholderLabel, new GBC(1, 0).setWeight(1, 1).setFill(GBC.HORIZONTAL).setInsets(0, 3, 0, 0));
        this.add(textField, new GBC(2, 0).setWeight(1000, 1).setFill(GBC.BOTH).setInsets(2, 4, 2, 5));

        Color bgColor = new Color(214, 214, 214, 255);
        textField.setBackground(bgColor);
        this.setBorder(new RCRoundBorder(this, bgColor, 1));
    }

    @Override
    protected void setListeners()
    {
        textField.addFocusListener(new FocusListener()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
                placeholderLabel.setVisible(false);
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                placeholderLabel.setVisible(true);
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

package com.rc.panels;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

/**
 * Created by song on 04/07/2017.
 */
public class ExpressionItem extends JPanel
{
    protected String code;
    protected ImageIcon icon;
    protected String displayName;

    protected Dimension size;

    private JLabel iconLabel;

    public ExpressionItem(String code, ImageIcon icon, String displayName, Dimension expressionSize, Dimension iconSize)
    {
        this.code = code;
        this.icon = icon;
        this.displayName = displayName;

        setPreferredSize(expressionSize);
        iconLabel = new JLabel();


        setIconPreferredSize(iconSize);
        iconLabel.setIcon(icon);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        add(iconLabel);

        this.setToolTipText(displayName);
    }

    public void setImage(ImageIcon icon)
    {
        this.iconLabel.setIcon(icon);
    }

    public void setIconPreferredSize(Dimension size)
    {
        iconLabel.setPreferredSize(size);
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public ImageIcon getIcon()
    {
        return icon;
    }

    public void setIcon(ImageIcon icon)
    {
        this.icon = icon;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    @Override
    public Dimension getSize()
    {
        return size;
    }

    @Override
    public void setSize(Dimension size)
    {
        this.size = size;
    }
}

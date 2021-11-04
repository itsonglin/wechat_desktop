package com.rc.components.message;

import com.rc.app.ShadowBorder;
import com.rc.frames.MainFrame;
import com.rc.utils.OSUtil;
import com.sun.awt.AWTUtilities;

import javax.swing.*;
import java.awt.*;

/**
 * Created by song on 2017/6/19.
 */
public class ShadowBorderDialog extends JDialog
{
    private JDialog actualDialog;
    public static final int DIALOG_WIDTH = 600;
    public static final int DIALOG_HEIGHT = 500;

    public ShadowBorderDialog(Frame owner, boolean modal, JDialog actualDialog)
    {
        super(owner, modal);

        this.actualDialog = actualDialog;

        int posX = MainFrame.getContext().getX();
        int posY = MainFrame.getContext().getY();

        posX = posX + (MainFrame.getContext().currentWindowWidth - DIALOG_WIDTH) / 2;
        posY = posY + (MainFrame.getContext().currentWindowHeight - DIALOG_HEIGHT) / 2;
        setBounds(posX, posY, DIALOG_WIDTH  +40, DIALOG_HEIGHT + 40);
        setUndecorated(true);

        if (OSUtil.getOsType() != OSUtil.MacOS)
        {
            AWTUtilities.setWindowOpaque(this, false);
            getRootPane().setBorder(ShadowBorder.newInstance());
        }

        actualDialog.setUndecorated(true);
        actualDialog.setBounds(getX() + 8, getY() + 8, getWidth() - 16, getHeight() - 16);
        actualDialog.setVisible(true);
    }

}

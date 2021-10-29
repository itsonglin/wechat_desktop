package com.rc.panels;

import javax.swing.*;

/**
 * @author song
 * @date 21-10-29 16:57
 * @description
 * @since
 */
public abstract class BasePanel extends JPanel
{
    public BasePanel()
    {
        this.initComponents();
        this.initView();
    }
    protected abstract void initComponents();
    protected abstract void initView();
}

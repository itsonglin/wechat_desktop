package com.rc.panels;

import javax.swing.*;
import java.util.Map;

/**
 * @author song
 * @date 21-10-29 16:57
 * @description
 * @since
 */
public abstract class BasePanel extends ParentAvailablePanel
{

    public BasePanel(JPanel parent)
    {
        super(parent);
        this.initComponents();
        this.initView();
        this.setListeners();
    }

    protected abstract void initComponents();

    protected abstract void initView();

    protected void setListeners()
    {
    }
}

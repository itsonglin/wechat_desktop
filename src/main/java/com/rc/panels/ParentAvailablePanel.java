package com.rc.panels;

import javax.swing.*;

/**
 * Created by song on 17-5-30.
 */
public class ParentAvailablePanel extends JPanel
{
    private JPanel parent;

    public ParentAvailablePanel(JPanel parent)
    {
        this.parent = parent;
    }

    public JPanel getParentPanel()
    {
        return parent;
    }
}

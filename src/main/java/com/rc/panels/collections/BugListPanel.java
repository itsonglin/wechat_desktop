package com.rc.panels.collections;

import javax.swing.*;

/**
 * @author song
 * @date 19-11-11 17:19
 * @description
 * @since
 */
public class BugListPanel extends CollectionListBasePanel
{
    private static BugListPanel context;

    public BugListPanel(JPanel parent)
    {
        super(parent, "Bug", "B");
        context = this;
    }

    public static BugListPanel getContext()
    {
        return context;
    }
}

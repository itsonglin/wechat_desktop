package com.rc.panels.collections;

import javax.swing.*;

/**
 * @author song
 * @date 19-11-11 17:19
 * @description
 * @since
 */
public class TaskListPanel extends CollectionListBasePanel
{
    private static TaskListPanel context;

    public TaskListPanel(JPanel parent)
    {
        super(parent, "任务", "T");
        context = this;

        notifyDataSetChanged();
    }


    public static TaskListPanel getContext()
    {
        return context;
    }
}

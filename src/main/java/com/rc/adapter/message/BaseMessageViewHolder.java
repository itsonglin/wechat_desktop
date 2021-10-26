package com.rc.adapter.message;

import com.rc.adapter.ViewHolder;
import com.rc.components.Colors;
import com.rc.panels.MessageTimePanel;

import javax.swing.*;

/**
 * Created by song on 13/06/2017.
 */
public class BaseMessageViewHolder extends ViewHolder
{
    public JLabel avatar = new JLabel();
    public MessageTimePanel time = new MessageTimePanel();
}

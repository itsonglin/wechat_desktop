package com.rc.panels;

import com.rc.components.InitComponent;

import javax.swing.*;
import java.util.Map;

/**
 * @author song
 * @date 21-10-29 16:57
 * @description
 * @since
 */
public abstract class BasePanel extends ParentAvailablePanel implements InitComponent
{
    public BasePanel(JPanel parent)
    {
        super(parent);
    }
}

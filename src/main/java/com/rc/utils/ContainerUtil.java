package com.rc.utils;

import com.rc.panels.BasePanel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author song
 * @date 21-11-1 17:47
 * @description
 * @since
 */
public class ContainerUtil
{
    private static Map<Class, BasePanel> containers = new HashMap<>();

    public static void addContainer(BasePanel panel)
    {
        containers.put(panel.getClass(), panel);
    }

    public static <T extends BasePanel> T getContainer(Class clazz)
    {
        return (T) containers.get(clazz);
    }
}

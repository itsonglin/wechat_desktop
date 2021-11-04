package com.rc.events;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局事件处理器
 * @author song
 * @date 21-11-4 15:44
 * @description
 * @since
 */
public class GlobalEventHandler
{
    private static List<GlobalEventListener> leftButtonClickedListeners = new ArrayList<>();


    public static void registerLeftButtonClickedListener(GlobalEventListener listener)
    {
        leftButtonClickedListeners.add(listener);
    }

    /**
     * 调用所有 鼠标左键点击 事件监听器
     * @param source 被点击对象
     */
    public static void callLeftButtonClickedListeners(Object source)
    {
        for (GlobalEventListener listener : leftButtonClickedListeners)
        {
            listener.doEvent(source);
        }
    }
}

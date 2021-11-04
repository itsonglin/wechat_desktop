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
    /**
     * 鼠标左键单击事件监听
     */
    private static List<GlobalEventListener> leftButtonClickedListeners = new ArrayList<>();

    /**
     * 主界面载入后事件监听
     */
    private static List<GlobalEventListener> mainFrameLoadedListeners = new ArrayList<>();


    /**
     * 注册 鼠标左键单击事件监听器
     */
    public static void registerLeftButtonClickedListener(GlobalEventListener listener)
    {
        leftButtonClickedListeners.add(listener);
    }

    /**
     * 注册 主界面载入后事件监听器
     */
    public static void registermainFrameLoadedListener(GlobalEventListener listener)
    {
        mainFrameLoadedListeners.add(listener);
    }

    /**
     * 调用所有 鼠标左键单击 事件监听器
     *
     * @param source 被点击对象
     */
    public static void callLeftButtonClickedListeners(Object source)
    {
        for (GlobalEventListener listener : leftButtonClickedListeners)
        {
            listener.doEvent(source);
        }
    }

    /**
     * 调用所有 主界面载入后 事件监听器
     *
     * @param source 主界面
     */
    public static void callMainFrameLoadedListeners(Object source)
    {
        for (GlobalEventListener listener : mainFrameLoadedListeners)
        {
            listener.doEvent(source);
        }

    }
}

package com.rc.helper;

import com.sun.istack.internal.Nullable;
import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * 注册全局热键
 *
 * Created by song on 01/08/2017.
 */
public class HotKeyHelper
{
    private static HotKeyHelper instance;
    private static Provider provider;
    private static Map<KeyStroke, String> keyStrokeStringMap;

    static
    {
        provider = Provider.getCurrentProvider(true);
        keyStrokeStringMap = new HashMap<>(10);
    }


    public static HotKeyHelper getInstance()
    {
        if (instance == null)
        {
            instance = new HotKeyHelper();
        }

        return instance;
    }

    public void register(KeyStroke keyStroke, HotKeyListener listener)
    {
        register(keyStroke, listener, "");
    }

    public void register(KeyStroke keyStroke, HotKeyListener listener, @Nullable String description)
    {
        provider.register(keyStroke, listener);
        keyStrokeStringMap.put(keyStroke, description);
    }

    public static void reset()
    {
        provider.reset();
    }

    public static void stop()
    {
        provider.stop();
    }

}

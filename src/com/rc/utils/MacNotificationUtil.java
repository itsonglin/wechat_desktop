package com.rc.utils;

import com.rc.app.Launcher;
import com.sun.jna.Library;
import com.sun.jna.Native;

import java.io.File;

/**
 * Created by song on 27/07/2017.
 */
public class MacNotificationUtil
{
    public static void sendNotification(String title, String subtitle, String text, int timeffset)
    {
        NsUserNotificationsBridge.instance.sendNotification(title, subtitle, text, timeffset);
    }
}

interface NsUserNotificationsBridge extends Library
{
    NsUserNotificationsBridge instance =
            (NsUserNotificationsBridge) Native.loadLibrary(
                    Launcher.appFilesBasePath + File.separator + "libs" + File.separator + "NsUserNotificationsBridge.dylib",
                    NsUserNotificationsBridge.class);

    int sendNotification(String title, String subtitle, String text, int timeffset);
}


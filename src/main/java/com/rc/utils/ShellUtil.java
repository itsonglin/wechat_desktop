package com.rc.utils;

import com.rc.app.Launcher;

import java.io.IOException;

public class ShellUtil
{
    public static String ICON_PATH =  Launcher.appFilesBasePath + "/ic_launcher.png";

    public static void executeShell(String shellCommand) throws IOException
    {
        String[] cmd = {"/bin/sh", "-c", shellCommand};
        Runtime.getRuntime().exec(cmd);
    }
}
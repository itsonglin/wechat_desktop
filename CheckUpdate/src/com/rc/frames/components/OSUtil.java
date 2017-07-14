package com.rc.frames.components;

/**
 * Created by song on 05/06/2017.
 */
public class OSUtil
{
    public static final int  Windows = 0;
    public static final int  Linux = 1;
    public static final int  Mac_OS = 2;
    public static final int  Others = -1;

    public static int getOsType()
    {
        String os = System.getProperty("os.name");

        if (os.indexOf("Windows") > -1)
        {
            return Windows;
        }
        else if (os.indexOf("Linux") > -1)
        {
            return Linux;
        }
        else if (os.indexOf("Mac OS") > -1)
        {
            return Mac_OS;
        }
        else
        {
            return Others;
        }

    }

}

package com.rc.utils;

/**
 * Created by song on 05/06/2017.
 */
public class OSUtil
{
    public static final int  Windows = 0;
    public static final int  Linux = 1;
    public static final int  Mac_OS = 2;
    public static final int  Others = 3;

    public static int currentOS = -1;

    public static int getOsType()
    {
        if (currentOS == -1)
        {
            String os = System.getProperty("os.name");

            if (os.indexOf("Windows") > -1)
            {
                currentOS = Windows;
            }
            else if (os.indexOf("Linux") > -1)
            {
                currentOS = Linux;
            }
            else if (os.indexOf("Mac OS") > -1)
            {
                currentOS = Mac_OS;
            }
            else
            {
                currentOS = Others;
            }
        }

        return currentOS;
    }
}

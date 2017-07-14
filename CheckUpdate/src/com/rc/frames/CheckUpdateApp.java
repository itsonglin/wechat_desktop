package com.rc.frames;

import java.io.File;
import java.io.IOException;

/**
 * Created by song on 28/06/2017.
 */
public class CheckUpdateApp
{
    private static String userHome;
    private static String appFilesBasePath;

    private static final String START_CMD = "java -jar wechat.jar";
    private static File updateSignalFile;

    public static void main(String[] args)
    {
        config();

        updateSignalFile = new File(appFilesBasePath + System.getProperty("file.separator") + "update.dat");
        // 文件存在，说有需要更新
        if (updateSignalFile.exists())
        {
            update();
        }
        else
        {
            System.out.println("不需要更新");
            exeCmd(START_CMD);
        }

    }

    /**
     * 打开更新窗口下载更新
     */
    private static void update()
    {
        UpdateFrame frame = new UpdateFrame();
        frame.setUpdateResultListener(new UpdateResultListener()
        {
            @Override
            public void onSuccess()
            {
                frame.setVisible(false);
                frame.dispose();
                exeCmd(START_CMD);
                updateSignalFile.delete();
                System.exit(1);
            }

            @Override
            public void onFailed()
            {
                System.out.println("更新失败，放弃更新，直接启动APP");
                exeCmd(START_CMD);
                System.exit(1);
            }
        });
        frame.setVisible(true);
    }

    private static void config()
    {
        userHome = System.getProperty("user.home");
        appFilesBasePath = userHome + System.getProperty("file.separator") + "wechat";
    }

    public static void exeCmd(String commandStr)
    {
        try
        {
            Runtime.getRuntime().exec(commandStr);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

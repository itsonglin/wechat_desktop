package com.rc.tasks;

import com.rc.utils.HttpUtil;

import java.io.IOException;

/**
 * Created by song on 15/06/2017.
 */
public class UploadTask
{
    UploadTaskCallback listener ;

    public UploadTask(UploadTaskCallback listener)
    {
        this.listener = listener;
    }

    public void execute(String url, String type, byte[] part)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    if (HttpUtil.upload(url, type, part))
                    {
                        if (listener != null)
                        {
                            listener.onTaskSuccess();
                        }
                    }
                    else
                    {
                        if (listener != null)
                        {
                            listener.onTaskError();
                        }
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

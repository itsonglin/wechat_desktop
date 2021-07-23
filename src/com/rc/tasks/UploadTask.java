package com.rc.tasks;

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
        // todo 上传文件

        new Thread(()->{
            try
            {
                Thread.sleep(500);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            listener.onTaskSuccess();
        }).start();
    }
}

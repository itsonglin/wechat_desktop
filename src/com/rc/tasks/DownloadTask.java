package com.rc.tasks;

import com.rc.utils.HttpUtil;

/**
 * Created by song on 16/06/2017.
 */
public class DownloadTask extends HttpTask
{
    HttpUtil.ProgressListener progressListener;

    public DownloadTask(HttpUtil.ProgressListener progressListener)
    {
        this.progressListener = progressListener;
    }

    public void execute(String url)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    byte[] data = HttpUtil.download(url, null, null, progressListener);
                    if (listener != null)
                    {
                        listener.onSuccess(data);
                    }
                } catch (Exception e)
                {
                    if (listener != null)
                    {
                        listener.onFailed();
                    }
                }
            }
        }).start();
    }
}

package com.rc.tasks;

/**
 * Created by song on 14/06/2017.
 */
public class MessageResendTask
{
    ResendTaskCallback listener;

    public void setListener(ResendTaskCallback listener)
    {
        this.listener = listener;
    }

    public void execute(String messageId)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(listener.getTime());
                    listener.onNeedResend(messageId);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}

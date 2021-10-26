package com.rc.tasks;

import com.rc.utils.HttpUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by song on 08/06/2017.
 */
public class HttpGetTask extends HttpTask
{
    @Override
    public void execute(String url)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String ret = null;
                try
                {
                    ret = HttpUtil.get(url, headers, requestParams);
                }  catch (Exception e)
                {
                    System.out.println("请求出错:" + url + ", " + e.getMessage());
                    if (listener != null)
                    {
                        listener.onFailed(e.getMessage());
                    }
                }

                if (ret != null && listener != null)
                {
                    try
                    {
                        JSONObject retJson = new JSONObject(ret);
                        listener.onSuccess(retJson);
                    }
                    catch (JSONException e)
                    {
                        listener.onFailed(ret == null ? e.getMessage() : ret);
                    }
                }
            }
        }).start();

    }
}

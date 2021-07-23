package com.rc.tasks;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by song on 08/06/2017.
 */
public abstract  class HttpTask
{
    protected HttpResponseListener listener;

    protected Map<String, String> headers = new HashMap<>();
    protected Map<String, String> requestParams = new HashMap<>();
    protected String url;

    public void addHeader(String name, String value)
    {
        headers.put(name, value);
    }

    public void addRequestParam(String name, String value)
    {
        requestParams.put(name, value);
    }

    public abstract void execute(String url);

    public void setListener(HttpResponseListener listener)
    {
        this.listener = listener;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }
}

package com.rc.tasks;


/**
 * Created by song on 08/06/2017.
 */
public interface HttpResponseListener<T extends Object>
{
    void onSuccess(T ret);

    void onFailed();
}

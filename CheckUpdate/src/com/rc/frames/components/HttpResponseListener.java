package com.rc.frames.components;


/**
 * Created by song on 08/06/2017.
 */
public interface HttpResponseListener<T extends Object>
{
    void onResult(T ret);


}

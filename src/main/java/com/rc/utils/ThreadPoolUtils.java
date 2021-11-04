package com.rc.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author song
 * @date 20-5-21 17:36
 * @description
 * @since
 */
public class ThreadPoolUtils
{
    public static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void execute(Runnable runnable)
    {
        executorService.execute(runnable);
    }
}

package com.rc.utils;

import com.rc.app.Launcher;
import java.io.*;
import java.text.DecimalFormat;

/**
 * Created by song on 2017/6/11.
 */
public class FileCache
{

    public static String FILE_CACHE_ROOT_PATH;
    private static DecimalFormat decimalFormat = new DecimalFormat("#.0");

    private FileCache()
    {

    }

    static
    {
        try
        {
            FILE_CACHE_ROOT_PATH =  Launcher.appFilesBasePath + "/cache/file";
            createCacheDir();
        }
        catch (Throwable e)
        {
            FILE_CACHE_ROOT_PATH = "./";
            createCacheDir();
        }
    }

    private static void createCacheDir()
    {
        File file = new File(FILE_CACHE_ROOT_PATH);
        if (!file.exists())
        {
            file.mkdirs();
            System.out.println("创建文件缓存目录：" + file.getAbsolutePath());
        }
    }

    public static String tryGetFileCache(String identify, String name)
    {
        File cacheFile = new File(FILE_CACHE_ROOT_PATH + "/" + identify + "_" + name);
        if (cacheFile.exists())
        {
            return cacheFile.getAbsolutePath();
        }

        return null;
    }

    public static String cacheFile(String identify, String name, byte[] data)
    {
        if (data == null || data.length < 1)
        {
            return null;
        }

        File cacheFile = new File(FILE_CACHE_ROOT_PATH + "/" + identify + "_" + name);
        try
        {
            FileOutputStream outputStream = new FileOutputStream(cacheFile);
            outputStream.write(data);
            outputStream.close();
            return cacheFile.getAbsolutePath();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static String fileSizeString(String path)
    {
        File file = new File(path);
        if (file == null)
        {
            return null;
        }

        long size = file.length();
        String retString = prettySizeString(size);

        return retString;
    }

    public static String prettySizeString(long size)
    {
        String retString;
        if (size < 1024)
        {
            retString = size + " 字节";
        }
        else if (size < 1024 * 1024)
        {
            retString = decimalFormat.format(size * 1.0F / 1024) + " KB";
        }
        else
        {
            retString = decimalFormat.format(size * 1.0F / 1024 / 1024) + " MB";
        }

        return retString;
    }


}

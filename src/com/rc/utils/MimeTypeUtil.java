package com.rc.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by song on 05/04/2017.
 */

public class MimeTypeUtil
{
    public static Map<String, String> MimeMap;

    static
    {
        MimeMap = new HashMap<>();
        MimeMap.put(".3gp", "video/3gpp");
        MimeMap.put(".apk", "application/vnd.android.package-archive");
        MimeMap.put(".asf", "video/x-ms-asf");
        MimeMap.put(".avi", "video/x-msvideo");
        MimeMap.put(".bin", "application/octet-stream");
        MimeMap.put(".bmp", "image/bmp");
        MimeMap.put(".c", "text/plain");
        MimeMap.put(".class", "application/octet-stream");
        MimeMap.put(".conf", "text/plain");
        MimeMap.put(".cpp", "text/plain");
        MimeMap.put(".doc", "application/msword");
        MimeMap.put(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        MimeMap.put(".xls", "application/vnd.ms-excel");
        MimeMap.put(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        MimeMap.put(".exe", "application/octet-stream");
        MimeMap.put(".gif", "image/gif");
        MimeMap.put(".gtar", "application/x-gtar");
        MimeMap.put(".gz", "application/x-gzip");
        MimeMap.put(".h", "text/plain");
        MimeMap.put(".htm", "text/html");
        MimeMap.put(".html", "text/html");
        MimeMap.put(".jar", "application/java-archive");
        MimeMap.put(".java", "text/plain");
        MimeMap.put(".jpeg", "image/jpeg");
        MimeMap.put(".jpg", "image/jpeg");
        MimeMap.put(".js", "application/x-javascript");
        MimeMap.put(".log", "text/plain");
        MimeMap.put(".m3u", "audio/x-mpegurl");
        MimeMap.put(".m4a", "audio/mp4a-latm");
        MimeMap.put(".m4b", "audio/mp4a-latm");
        MimeMap.put(".m4p", "audio/mp4a-latm");
        MimeMap.put(".m4u", "video/vnd.mpegurl");
        MimeMap.put(".m4v", "video/x-m4v");
        MimeMap.put(".mov", "video/quicktime");
        MimeMap.put(".mp2", "audio/x-mpeg");
        MimeMap.put(".mp3", "audio/x-mpeg");
        MimeMap.put(".mp4", "video/mp4");
        MimeMap.put(".mpc", "application/vnd.mpohun.certificate");
        MimeMap.put(".mpe", "video/mpeg");
        MimeMap.put(".mpeg", "video/mpeg");
        MimeMap.put(".mpg", "video/mpeg");
        MimeMap.put(".mpg4", "video/mp4");
        MimeMap.put(".mpga", "audio/mpeg");
        MimeMap.put(".msg", "application/vnd.ms-outlook");
        MimeMap.put(".ogg", "audio/ogg");
        MimeMap.put(".pdf", "application/pdf");
        MimeMap.put(".png", "image/png");
        MimeMap.put(".pps", "application/vnd.ms-powerpoint");
        MimeMap.put(".ppt", "application/vnd.ms-powerpoint");
        MimeMap.put(".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        MimeMap.put(".prop", "text/plain");
        MimeMap.put(".rc", "text/plain");
        MimeMap.put(".rmvb", "audio/x-pn-realaudio");
        MimeMap.put(".rtf", "application/rtf");
        MimeMap.put(".sh", "text/plain");
        MimeMap.put(".tar", "application/x-tar");
        MimeMap.put(".tgz", "application/x-compressed");
        MimeMap.put(".txt", "text/plain");
        MimeMap.put(".wav", "audio/x-wav");
        MimeMap.put(".wma", "audio/x-ms-wma");
        MimeMap.put(".wmv", "audio/x-ms-wmv");
        MimeMap.put(".wps", "application/vnd.ms-works");
        MimeMap.put(".xml", "text/plain");
        MimeMap.put(".z", "application/x-compress");
        MimeMap.put(".rar", "application/x-compress");
        MimeMap.put(".7z", "application/x-compress");
        MimeMap.put(".zip", "application/x-zip-compressed");
       // MimeMap.put("", "*/*");
    }

    public static String getMime(String suffix)
    {
        if (!suffix.startsWith("."))
        {
            suffix = "." + suffix;
        }
        return MimeMap.get(suffix);
    }

}

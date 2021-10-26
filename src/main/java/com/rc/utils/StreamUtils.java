package com.rc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author song
 * @date 19-9-19 17:55
 * @description
 * @since
 */
public class StreamUtils
{
    public static void copy(byte[] in, OutputStream out) throws IOException
    {
        out.write(in);
        out.flush();
    }

    public static int copy(InputStream in, OutputStream out) throws IOException {
        int byteCount = 0;

        int bytesRead;
        for(byte[] buffer = new byte[4096]; (bytesRead = in.read(buffer)) != -1; byteCount += bytesRead) {
            out.write(buffer, 0, bytesRead);
        }

        out.flush();
        return byteCount;
    }
}

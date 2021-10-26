package com.rc.helper;

import com.rc.utils.IconUtil;
import com.rc.utils.MimeTypeUtil;

import javax.swing.*;

/**
 * Created by song on 17-6-4.
 */
public class AttachmentIconHelper
{
    public ImageIcon getImageIcon(String filename)
    {
        return getImageIcon(filename, -1, -1);
    }

    public ImageIcon getImageIcon(String filename, int width, int height)
    {
        String suffix = filename.substring(filename.lastIndexOf(".") + 1);

        if (null == suffix || suffix.length() < 1)
        {
            return unknownMimeIcon(width, height);
        }
        else
        {
            String mime = MimeTypeUtil.getMime(suffix);
            if (mime == null)
            {
                return unknownMimeIcon(width, height);

            }
            else
            {
                mime = parseMimeType(mime);
                if (mime == null)
                {
                    return unknownMimeIcon(width, height);

                }
                else
                {
                    //return new ImageIcon(getClass().getResource("/image/" + mime + ".png"));
                    return IconUtil.getIcon(this, "/image/" + mime + ".png", width, height);

                }
            }

        }
    }

    private ImageIcon unknownMimeIcon(int width, int height)
    {
        return IconUtil.getIcon(this, "/image/unknown.png", width, height);
    }

    private String parseMimeType(String mime)
    {
        String type = mime.substring(0, mime.indexOf("/"));
        String value;
        switch (type)
        {
            case "application":
            {
                value = parseApplicationMimeType(mime);
                break;
            }
            case "video":
            {
                value = "video";
                break;
            }
            case "image":
            {
                value = "image";
                break;
            }
            case "text":
            {
                value = "text";
                break;
            }
            case "audio":
            {
                value = "audio";
                break;
            }
            default:
            {
                return null;
            }
        }

        return value;
    }

    /**
     * 进一步解析MIME类型为application时的具体类型
     *
     * @param type
     * @return
     */
    private String parseApplicationMimeType(String type)
    {
        String subType = type.substring(type.indexOf("/") + 1);
        String value;
        switch (subType)
        {
            case "msword":
            case "vnd.ms-works":
            case "rtf":
            case "vnd.openxmlformats-officedocument.wordprocessingml.document":
            {
                value = "ms_word";
                break;
            }
            case "vnd.ms-excel":
            case "vnd.openxmlformats-officedocument.spreadsheetml.sheet":
            {
                value = "ms_excel";
                break;
            }
            case "vnd.ms-powerpoint":
            case "vnd.openxmlformats-officedocument.presentationml.presentation":
            {
                value = "ms_powerpoint";
                break;
            }
            case "x-gzip":
            case "java-archive":
            case "x-tar":
            case "x-compressed":
            case "x-zip-compressed":
            case "x-compress":
            {
                value = "compress";
                break;
            }
            case "pdf":
            {
                value = "pdf";
                break;
            }
            case "vnd.android.package-archive":
            {
                value = "app";
                break;
            }

            default:
            {
                value = "unknown";
                break;
            }
        }
        return value;
    }
}

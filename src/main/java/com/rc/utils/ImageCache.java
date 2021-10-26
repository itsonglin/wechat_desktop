package com.rc.utils;

import com.rc.app.Launcher;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by song on 2017/6/11.
 */
public class ImageCache
{
    public static final int THUMB = 0;
    public static final int ORIGINAL = 1;

    public static String IMAGE_CACHE_ROOT_PATH;
    private static Logger logger = LoggerFactory.getLogger(ImageCache.class);

    private ImageCache()
    {

    }

    static
    {
        try
        {
            IMAGE_CACHE_ROOT_PATH = Launcher.appFilesBasePath + "/cache/image";
            createCacheDir();
        }
        catch (Exception e)
        {
            IMAGE_CACHE_ROOT_PATH = "./";
            createCacheDir();
        }
    }

    private static void createCacheDir()
    {
        File file = new File(IMAGE_CACHE_ROOT_PATH);
        if (!file.exists())
        {
            file.mkdirs();
            System.out.println("创建图片缓存目录：" + file.getAbsolutePath());
        }
    }

    public static ImageIcon tryGetThumbCache(String identify)
    {
        File cacheFile = new File(IMAGE_CACHE_ROOT_PATH + "/" + identify + "_thumb");
        if (cacheFile.exists())
        {
            ImageIcon icon = new ImageIcon(cacheFile.getAbsolutePath());
            return icon;
        }

        return null;
    }


    /**
     * 异步获取图像缩略图
     *
     * @param identify
     * @param url
     * @param listener
     */
    public static void requestThumbAsynchronously(String identify, String url, ImageCacheRequestListener listener)
    {
        requestImage(THUMB, identify, url, listener);

    }

    /**
     * 异步获取图像原图
     *
     * @param identify
     * @param url
     * @param listener
     */
    public static void requestOriginalAsynchronously(String identify, String url, ImageCacheRequestListener listener)
    {
        requestImage(ORIGINAL, identify, url, listener);
    }


    private static void requestImage(int requestType, String identify, String url, ImageCacheRequestListener listener)
    {
        String suffix = "";
        int startPos = url.lastIndexOf(".");
        if (startPos > -1)
        {
            int endPos = url.lastIndexOf("?");
            endPos = endPos == -1 ? url.length() : endPos;
            suffix = url.substring(startPos,endPos);
            if (suffix.startsWith(".com"))
            {
                suffix = "";
            }
        }

        String finalSuffix = suffix;


        new Thread(() ->
        {
            File cacheFile;
            if (requestType == THUMB)
            {
                cacheFile = new File(IMAGE_CACHE_ROOT_PATH + "/" + identify + "_thumb");
            }
            else
            {
                cacheFile = new File(IMAGE_CACHE_ROOT_PATH + "/" + identify + finalSuffix);
            }

            if (cacheFile.exists())
            {
                System.out.println("本地缓存获取图片：" + cacheFile.getAbsolutePath());
                ImageIcon icon = new ImageIcon(cacheFile.getAbsolutePath());
                listener.onSuccess(icon, cacheFile.getAbsolutePath());
            }
            else
            {
                try
                {
                    byte[] data;

                    String reqUrl = buildRemoteImageUrl(url);

                    // 本地上传的文件，则从原上传路径复制一份到缓存目录
                    if (reqUrl.startsWith("file://"))
                    {
                        //String originUrl = reqUrl.substring(7);
                       try( FileInputStream fileInputStream = new FileInputStream(url))
                       {
                           data = new byte[fileInputStream.available()];
                           fileInputStream.read(data);
                       }

                    }
                    // 接收的图像，从服务器获取并缓存
                    else
                    {
                        System.out.println("服务器获取图片：" + reqUrl);
                        data = HttpUtil.download(reqUrl);
                    }


                    if (data == null)
                    {
                        logger.debug("图像获取失败");
                    }

                    Image image = ImageIO.read(new ByteArrayInputStream(data));

                    // 生成缩略图并缓存
                    createThumb(image, identify);

                    if (requestType == THUMB)
                    {
                        ImageIcon icon = new ImageIcon(cacheFile.getAbsolutePath());
                        listener.onSuccess(icon, cacheFile.getAbsolutePath());
                    }

                    // 缓存原图
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(IMAGE_CACHE_ROOT_PATH + "/" + identify + finalSuffix));
                    fileOutputStream.write(data);


                    if (requestType == ORIGINAL)
                    {
                        ImageIcon icon = new ImageIcon(cacheFile.getAbsolutePath());
                        listener.onSuccess(icon, cacheFile.getAbsolutePath());
                    }
                }
                catch (IOException e)
                {
                    listener.onFailed("文件不存在");
                    //e.printStackTrace();
                }
            }
        }).start();
    }

    private static String buildRemoteImageUrl(String imageUrl)
    {
        String url;
        // 服务上的图片
        if (imageUrl.startsWith("/file-upload"))
        {
            url = "";
        }
        // 本地的图片
        else
        {
            url = "file://" + imageUrl;
        }

        return url;
    }

    /**
     * 生成图片缩略图
     *
     * @param image
     * @param identify
     */
    public static void createThumb(Image image, String identify)
    {
        try
        {
            int[] imageSize = getImageSize(image);
            int destWidth = imageSize[0];
            int destHeight = imageSize[1];

            float scale = imageSize[0] * 1.0F / imageSize[1];

            if (imageSize[0] > imageSize[1] && imageSize[0] > 200)
            {
                destWidth = 200;
                destHeight = (int) (destWidth / scale);
            }
            else if (imageSize[0] < imageSize[1] && imageSize[1] > 200)
            {
                destHeight = 200;
                destWidth = (int) (destHeight * scale);
            }

            // 开始读取文件并进行压缩
            BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);

            tag.getGraphics().drawImage(image.getScaledInstance(destWidth, destHeight, Image.SCALE_SMOOTH), 0, 0, null);

            File cacheFile = new File(IMAGE_CACHE_ROOT_PATH + "/" + identify + "_thumb");
            FileOutputStream out = new FileOutputStream(cacheFile);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(tag);
            out.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public static int[] getImageSize(Image image)
    {

        if (image == null)
        {
            return new int[]{10, 10};
        }
        int result[] = {0, 0};
        try
        {
            result[0] = image.getWidth(null); // 得到源图宽
            result[1] = image.getHeight(null); // 得到源图高
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }


    public interface ImageCacheRequestListener
    {
        void onSuccess(ImageIcon icon, String path);

        void onFailed(String why);
    }


}

package com.rc.utils;

import com.rc.app.Launcher;
import com.rc.db.model.CurrentUser;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.log4j.Logger;

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

    public String IMAGE_CACHE_ROOT_PATH;
    Logger logger = Logger.getLogger(this.getClass());
    private CurrentUser currentUser;


    public ImageCache()
    {
        try
        {
            //IMAGE_CACHE_ROOT_PATH = getClass().getResource("/cache").getPath() + "/image";
            IMAGE_CACHE_ROOT_PATH = Launcher.appFilesBasePath + "/cache/image";

            File file = new File(IMAGE_CACHE_ROOT_PATH);
            if (!file.exists())
            {
                file.mkdirs();
                System.out.println("创建图片缓存目录：" + file.getAbsolutePath());
            }
        }
        catch (Exception e)
        {
            IMAGE_CACHE_ROOT_PATH = "./";
        }

        currentUser = Launcher.currentUserService.findAll().get(0);
    }

    public ImageIcon tryGetThumbCache(String identify)
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
    public void requestThumbAsynchronously(String identify, String url, ImageCacheRequestListener listener)
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
    public void requestOriginalAsynchronously(String identify, String url, ImageCacheRequestListener listener)
    {
        requestImage(ORIGINAL, identify, url, listener);
    }


    private void requestImage(int requestType, String identify, String url, ImageCacheRequestListener listener)
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


        new Thread(new Runnable()
        {
            @Override
            public void run()
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
                            FileInputStream fileInputStream = new FileInputStream(url);
                            data = new byte[fileInputStream.available()];
                            fileInputStream.read(data);
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
            }
        }).start();
    }

    private String buildRemoteImageUrl(String imageUrl)
    {
        String url;
        // 服务上的图片
        if (imageUrl.startsWith("/file-upload"))
        {
            url = Launcher.HOSTNAME + imageUrl + "?rc_uid=" + currentUser.getUserId() + "&rc_token=" + currentUser.getAuthToken();
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
    public void createThumb(Image image, String identify)
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

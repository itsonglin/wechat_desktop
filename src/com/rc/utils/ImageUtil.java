package com.rc.utils;

import com.gif4j.GifDecoder;
import com.gif4j.GifEncoder;
import com.gif4j.GifImage;
import com.gif4j.GifTransformer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 图像处理工具类
 * <p>
 * Created by song on 2017/6/24.
 */
public class ImageUtil
{
    /**
     * 图片设置圆角
     *
     * @param srcImage
     * @param radius
     * @return
     * @throws IOException
     */
    public static BufferedImage setRadius(Image srcImage, int width, int height, int radius) throws IOException
    {

        if (srcImage.getWidth(null) > width || srcImage.getHeight(null) > height)
        {
            // 图片过大，进行缩放
            ImageIcon imageIcon = new ImageIcon();
            imageIcon.setImage(srcImage.getScaledInstance(width, height, Image.SCALE_SMOOTH));
            srcImage = imageIcon.getImage();
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gs = image.createGraphics();
        gs.setComposite(AlphaComposite.Src);
        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gs.setColor(Color.WHITE);
        gs.fill(new RoundRectangle2D.Float(0, 0, width, height, radius, radius));
        gs.setComposite(AlphaComposite.SrcAtop);
        gs.drawImage(srcImage, 0, 0, null);
        gs.dispose();
        return image;
    }

    public static boolean isGif(String imagePath)
    {

        String suffix = "";
        int pos = imagePath.lastIndexOf(".");
        if (pos >= 0)
        {
            suffix = imagePath.substring(pos + 1).toLowerCase();
        }

        return suffix.equals("gif");
    }

    public static ImageIcon scaleGif(String path, float wScale, float hScale)
    {
        GifImage srcImage = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream())
        {
            srcImage = GifDecoder.decode(new File(path));
            GifImage scaleImg = GifTransformer.scale(srcImage, wScale, hScale, true);
            GifEncoder.encode(scaleImg, bos);

            return new ImageIcon(bos.toByteArray());
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 取GIF图片的第0帧
     *
     * @param src
     * @return
     */
    public static BufferedImage getFirstFrameInGif(InputStream src)
    {
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream())
        {

            byte buffer[] = new byte[1024];
            int len = 0;
            while ((len = src.read(buffer)) != -1)
            {
                bos.write(buffer, 0, len);
            }

            try(ByteArrayInputStream bin  = new ByteArrayInputStream(bos.toByteArray(), 0, bos.size()))
            {
                //截取第一张图
                BufferedImage bimage = ImageIO.read(bin);
                return bimage;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}

package com.rc.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
}

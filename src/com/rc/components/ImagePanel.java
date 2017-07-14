package com.rc.components;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by song on 17-5-29.
 */
public class ImagePanel extends JPanel
{
    private GeneralPath path = new GeneralPath();
    private Image image;

    public ImagePanel(Image image)
    {
        this.image = image;
    }

    public ImagePanel()
    {

    }

    public void setImage(Image image)
    {
        this.image = image;
    }


    @Override
    public void paint(Graphics g)
    {
        int width = this.getWidth(), height = this.getHeight();
        // 图像缩放到容器宽高
        //ImageIcon imageIcon = new ImageIcon();
        //imageIcon.setImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));

        Graphics2D g2 = (Graphics2D) g;
        //RoundRectangle2D rect = new RoundRectangle2D.Float(0, 0, width, height, 8, 8);
        //path.append(rect, false);
        //g2.setClip(path);

        try
        {
            Image img = setRadius(this.image, width, height, 10);
            g2.drawImage(img, 0, 0, null);
            g2.dispose();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * 图片设置圆角
     * @param srcImage
     * @param radius
     * @return
     * @throws IOException
     */
    public static BufferedImage setRadius(Image srcImage,int width, int height, int radius) throws IOException{

        /*if (srcImage.getWidth(null) > width || srcImage.getHeight(null) > height)
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

        return image;*/

        return null;

    }
}

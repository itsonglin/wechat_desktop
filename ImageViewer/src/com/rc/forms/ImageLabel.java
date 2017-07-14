package com.rc.forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.font.LineMetrics;
import java.util.*;

/**
 * Created by song on 26/06/2017.
 */
public class ImageLabel extends JLabel
{
    private Image image;
    private Image lastImage;
    private int xDistance = 0;
    private int yDistance = 0;

    int x = -1;
    int y = -1;
    int minX;
    int maxX;
    int minY;
    int maxY;

    private boolean firstDraw = true;
    private boolean scaleImage = false;

    public ImageLabel()
    {
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setFont(getFont());
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int currentWidth = image.getWidth(null);
        int currentHeight = image.getHeight(null);

        if (firstDraw)
        {
            // 图片于容器垂直居中
            x = (getWidth() - currentWidth) / 2;
            y = (getHeight() - currentHeight) / 2;
            firstDraw = false;
        }
        else if (scaleImage)
        {
            int lastWidth = 0;
            int lastHeight = 0;
            int xOffset = 0;
            int yOffset = 0;

            if (lastImage != null)
            {
                lastWidth = lastImage.getWidth(null);
                lastHeight = lastImage.getHeight(null);
                xOffset = (lastWidth - currentWidth) / 2;
                yOffset = (lastHeight - currentHeight) / 2;
            }

            // 图片于容器垂直居中
            x += xOffset;
            y += yOffset;
            scaleImage = false;
        }
        else
        {
            x += xDistance;
            y += yDistance;
        }



       /* x = x > 0 ? 0 : x;
        y = y > 0 ? 0 : y;*/


        //System.out.println("x = " + x + ", y = " + y + ",   width = " + getWidth() + ", height = " + getHeight());
        minX = (int) (-getWidth() * 0.7);
        maxX = (int) (getWidth() * 0.7);
        minY = (int) (-getHeight() * 0.7);
        maxY = (int) (getHeight() * 0.7);

        if (x < minX)
        {
            x = minX;
        }
        else if (x > maxX)
        {
            x = maxX;
        }

        if (y < minY)
        {
            y = minY;
        }
        else if (y > maxY)
        {
            y = maxY;
        }

        g2d.drawImage(image, x, y, null);

        g2d.dispose();
    }

    public Image getImage()
    {
        return image;
    }

    public void setImage(Image image)
    {
        lastImage = this.image;
        this.image = image;
        firstDraw = true;
    }

    public void scaleImage(Image image)
    {
        lastImage = this.image;
        this.image = image;
        scaleImage = true;
        repaint();
    }

    public void moveImage(int xDistance, int yDistance)
    {
        this.xDistance = xDistance;
        this.yDistance = yDistance;

        this.repaint();
    }
}

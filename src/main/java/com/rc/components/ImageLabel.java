package com.rc.components;

import com.rc.utils.IconUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by song on 26/06/2017.
 */
public class ImageLabel extends JLabel
{
    private Image sourceImage;
    private Image drawImage;
    private Image lastImage;
    private int xDistance = 0;
    private int yDistance = 0;

    int x = -1;
    int y = -1;

    int drawX = -1;
    int drawY = -1;


    private boolean firstDraw = true;
    private boolean scaleImage = false;

    float maxScale = 3.0F;
    float minScale = 0.2F;
    // 图片缩放比例
    private float scale = 1.0F;

    private boolean isGif;
    private String sourceGifImage;

    public ImageLabel(boolean isGif)
    {
        this.isGif = isGif;
        if (!isGif)
        {
            setListeners();
        }
    }

    private void setListeners()
    {
        addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                firstDraw = true;
                repaint();
                super.componentResized(e);
            }
        });

        MouseAdapter listener = new MouseAdapter()
        {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                // 是否向上滚动
                boolean up = e.getWheelRotation() < 0;
                float increment;
                if (up)
                {
                    increment = 0.15F;
                } else
                {
                    increment = -0.15F;
                }
                scaleImage(increment);

                super.mouseWheelMoved(e);
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
                x = e.getX();
                y = e.getY();
                super.mousePressed(e);
            }

            @Override
            public void mouseDragged(MouseEvent e)
            {

                if (e.getModifiers() == InputEvent.BUTTON1_MASK)
                {
                    xDistance = e.getX() - x;
                    yDistance = e.getY() - y;

                    x = e.getX();
                    y = e.getY();

                    repaint();
                    //moveImage();
                }

                super.mouseDragged(e);
            }
        };

        addMouseWheelListener(listener);
        addMouseMotionListener(listener);
        addMouseListener(listener);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        if (isGif)
        {
            super.paintComponent(g);
            return;
        }

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setFont(getFont());
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int currentWidth = drawImage.getWidth(null);
        int currentHeight = drawImage.getHeight(null);

        int width = getWidth();
        int height = getHeight();

        if (firstDraw)
        {
            // 图片于容器垂直居中
            drawX = (width - currentWidth) / 2;
            drawY = (height - currentHeight) / 2;
            firstDraw = false;
        } else if (scaleImage)
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
            drawX += xOffset;
            drawY += yOffset;


            drawY = drawY < (height - currentHeight) ? (height - currentHeight) : drawY;
            drawX = drawX < (width - currentWidth) ? (width - currentWidth) : drawX;

            if (drawX > 0)
            {
                drawX = (width - currentWidth) / 2;
            }
            if (drawY > 0)
            {
                drawY = (height - currentHeight) / 2;
            }

            scaleImage = false;
        } else
        {


            if (currentWidth > width && currentHeight > height)
            {
                // 移动图像
                drawX += xDistance;
                drawY += yDistance;

                /*
                 * [1] + [3] 确保当图像尺寸大于窗口尺寸时，图像在水平方向上拖动时不会脱离窗口左右边缘
                 * [2] + [4] 确保当图像尺寸大于窗口尺寸时，图像在垂直方向上拖动时不会脱离窗口上下边缘
                 * */
                drawX = width - drawX > currentWidth ? width - currentWidth : drawX; // [1]
                drawY = height - drawY > currentHeight ? height - currentHeight : drawY; // [2]
                drawX = drawX > 0 ? 0 : drawX; // [3]
                drawY = drawY > 0 ? 0 : drawY; // [4]
            } else if (currentWidth > width && currentHeight <= height)
            {
                drawX += xDistance;
                drawX = width - drawX > currentWidth ? width - currentWidth : drawX; // [1]
                drawX = drawX > 0 ? 0 : drawX; // [3]

            } else if (currentHeight > height && currentWidth <= width)
            {
                drawY += yDistance;
                drawY = height - drawY > currentHeight ? height - currentHeight : drawY; // [2]
                drawY = drawY > 0 ? 0 : drawY; // [4]
            }
        }

        /*System.out.println("x = " + x + ", y = " + y + ",   width = " + getWidth() + ", height = " + getHeight()
                + ",   currentWidth = " + currentWidth + ", currentHeight = " + currentHeight
                + ", height - currentHeight = " + (height - currentHeight));*/

        g2d.drawImage(drawImage, drawX, drawY, null);
        g2d.dispose();
    }


    public void setSourceImage(Image sourceImage)
    {
        this.sourceImage = lastImage = drawImage = sourceImage;
        firstDraw = true;
    }

    public void scaleImage(float increment)
    {
        scale = scale + increment;

        if (scale > maxScale)
        {
            scale = maxScale;
        } else if (scale < minScale)
        {
            scale = minScale;
        } else
        {
            if (Math.abs(scale - 1.0F) < 0.1F)
            {
                scale = 1.0F;
            }
            Image scaledImage = getScaledImage(scale);

            lastImage = this.drawImage;
            this.drawImage = scaledImage;
            scaleImage = true;
            repaint();
        }
    }

    private Image getScaledImage(float scale)
    {
        int scaledWidth = (int) (sourceImage.getWidth(null) * scale);
        int scaledHeight = (int) (sourceImage.getHeight(null) * scale);

        Image scaledimage = sourceImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_FAST);
        return scaledimage;
    }


    public void setSourceGifImage(String sourceGifImage)
    {
        this.sourceGifImage = sourceGifImage;
        this.setIcon(new ImageIcon(sourceGifImage));
    }

    public String getSourceGifImage()
    {
        return sourceGifImage;
    }
}

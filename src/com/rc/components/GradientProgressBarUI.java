package com.rc.components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by song on 17-6-4.
 */
public class GradientProgressBarUI extends BasicProgressBarUI
{
    @Override
    protected void paintDeterminate(Graphics g, JComponent c)
    {
        Graphics2D graphics2d = (Graphics2D) g;

        Insets b = progressBar.getInsets();
        // JProgressBar的边界区域

        int width = progressBar.getWidth();
        int height = progressBar.getHeight();


        int barRectWidth = width - (b.right + b.left);
        int barRectHeight = height - (b.top + b.bottom);
        //int arcSize = height / 2 - 1;
        int arcSize = 0;

        int amountFull = getAmountFull(b, barRectWidth, barRectHeight);
        //已完成的进度

        graphics2d.setColor(Color.WHITE);
        graphics2d.fillRoundRect(0, 0, width - 1, height, arcSize,
                arcSize);
        //绘制JProgressBar的背景

        //用GradientPaint类来实现渐变色显示进度
        //设置了开始点和终止点，并设置好这两个点的颜色，系统会自动在这两个点中用渐变色来填充
        Point2D start = new Point2D.Float(0, 0);
        Point2D end = new Point2D.Float(amountFull - 1, barRectHeight - 1);
        //这里设置的终止点是当前已经完成的进度的那个点

        GradientPaint gradientPaint = new GradientPaint(
                start, Colors.PROGRESS_BAR_START, end, Colors.PROGRESS_BAR_END);

        graphics2d.setPaint(gradientPaint);

        graphics2d.fillRoundRect(b.left, b.top, amountFull - 1,
                barRectHeight, arcSize, arcSize);//这里实现的是圆角的效果将arcSize调成0即可实现矩形效果
    }
}
package com.rc.listener;

import com.rc.frames.MainFrame;
import com.rc.panels.TitlePanel;
import com.rc.utils.OSUtil;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

/**
 * @author song
 * @date 19-9-25 13:58
 * @description
 * @since
 */
public class WindowMouseListener
        implements MouseInputListener
{

    Point origin;
    JFrame frame;
    private long lastClickTime;

    Robot robot;


    public WindowMouseListener(JFrame frame)
    {
        this.frame = frame;
        origin = new Point();

        try
        {
            robot = new Robot();
        } catch (AWTException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (OSUtil.getOsType() != OSUtil.Linux)
        {
            if (System.currentTimeMillis() - lastClickTime < 500)
            {
                TitlePanel.getContext().maxOrRestoreWindow();
            }

            lastClickTime = System.currentTimeMillis();
        }
    }

    /**
     * 记录鼠标按下时的点
     */
    @Override
    public void mousePressed(MouseEvent e)
    {
        origin.x = e.getX();
        origin.y = e.getY();

       /* Point mp = MouseInfo.getPointerInfo().getLocation();
        mouseDisX = mp.x - origin.x;
        mouseDisY = mp.y - origin.y;

        System.out.print("鼠标距离:" + mouseDisX + "," + mouseDisY);*/
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
    }

    /**
     * 鼠标移进标题栏时，设置鼠标图标为移动图标
     */
    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    /**
     * 鼠标移出标题栏时，设置鼠标图标为默认指针
     */
    @Override
    public void mouseExited(MouseEvent e)
    {
    }

    /**
     * 鼠标在标题栏拖拽时，设置窗口的坐标位置
     * 窗口新的坐标位置  = 移动前坐标位置+（鼠标指针当前坐标-鼠标按下时指针的位置）
     */
    @Override
    public void mouseDragged(MouseEvent e)
    {
        Point p = this.frame.getLocation();
        int x = p.x + e.getX() - origin.x;
        int y = p.y + e.getY() - origin.y;
        if (Math.abs(p.x - x) > 180 || Math.abs(p.y - y) > 180)
        {
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            return;
        }

        frame.setLocation(x, y);
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
    }

    public void change(int type, int x, int y)
    {
       /* Point p = MouseInfo.getPointerInfo().getLocation();
        System.out.print("窗口位置:" + x + "," + y);
        System.out.println(",  鼠标位置:" + p.x + "," + p.y);*/
        /*int width = (int) p.getX() + x;
        int heigh = (int) p.getY() + y;
        if (type == 0)
        {
            width = x;
            heigh = y;
        }
        Robot robot;
        try
        {
            robot = new Robot();
            robot.mouseMove(width, heigh);
        } catch (AWTException e)
        {
            e.printStackTrace();
        }*/

        System.out.println("鼠标位置:" + x + "," + y);
        Robot robot;
        try
        {
            robot = new Robot();
            robot.mouseMove(x, y);
        } catch (AWTException e)
        {
            e.printStackTrace();
        }
    }
}

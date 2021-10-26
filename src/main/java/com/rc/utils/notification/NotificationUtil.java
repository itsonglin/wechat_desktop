package com.rc.utils.notification;

import com.rc.panels.NotificationPopup;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * @author song
 * @date 19-9-29 09:25
 * @description
 * @since
 */
public class NotificationUtil
{
    private static Stack<NotificationPopup> idles = new Stack<>();

    private static Stack<NotificationPopup> showns = new Stack<>();

    private static NotificationEventListener eventListener;

    private static Dimension windowDimension = Toolkit.getDefaultToolkit().getScreenSize();


    /**
     * 等待中的消息队列
     */
    public static Queue<MsgItem> waitingItems = new ArrayDeque<>();

    private static int initCapacity = 5;


    static
    {
        eventListener = new NotificationEventListener()
        {
            @Override
            public void onShown(NotificationPopup src)
            {

            }

            @Override
            public void onClosed(NotificationPopup src)
            {
                idles.push(src);
                showns.remove(src);

                adjustAfterPopup();

                // 从等待队列获取一条消息
                MsgItem item = waitingItems.poll();
                if (item != null)
                {
                    show(item.roomId, item.icon, item.title, item.brief, item.message);
                }
            }
        };

        for (int i = 0; i < initCapacity; i++)
        {
            NotificationPopup pop = new NotificationPopup();
            pop.setListener(eventListener);
            idles.push(pop);
        }


    }


    public static synchronized void show(String roomId, ImageIcon icon, String title, String brief, String message)
    {
        try
        {
            NotificationPopup popup = idles.pop();
            popup.show(roomId, icon, title, brief, message, calcLocation());
            showns.push(popup);
        } catch (EmptyStackException e)
        {
            waitingItems.add(new MsgItem(roomId, icon, title, brief, message));
            System.out.println("通知队列已满");
        }
    }

    private static Point calcLocation()
    {
        Point location;
        int gap = 50;
        int height = NotificationPopup.HEIGHT + 20;
        if (showns.size() == 0)
        {
            location = new Point((int) (windowDimension.getWidth() - 320), gap);
        } else
        {
            location = new Point((int) (windowDimension.getWidth() - 320), gap + height * showns.size());
        }
        return location;
    }

    private static void adjustAfterPopup()
    {
        int height = NotificationPopup.HEIGHT + 20;
        int gap = 50;

        for (int i = 0; i < showns.size(); i++)
        {
            NotificationPopup pop = showns.get(i);
            Point p = pop.getLocation();

           /* int y = p.y - height;
            if (showns.size() == 1)
            {
                y = gap;
            }*/

            int y;
            if (i == 0)
            {
                y = gap;
            } else
            {
                y = gap + height * i;
            }

            int destY = y;
            /*Point newP = null;
            newP = new Point(p.x, destY);
            pop.setLocation(newP);
            System.out.println(newP);*/

            new Thread(() ->
            {
                Point newP = null;
                int cy = p.y;
                while (cy >= destY)
                {
                    newP = new Point(p.x, cy);
                    pop.setLocation(newP);
                    cy -= 10;

                    try
                    {
                        Thread.sleep(10);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

}


class MsgItem
{
    public String roomId;
    public ImageIcon icon;
    public String title;
    public String brief;
    public String message;

    public MsgItem()
    {
    }

    public MsgItem(String roomId, ImageIcon icon, String title, String brief, String message)
    {
        this.roomId = roomId;
        this.icon = icon;
        this.title = title;
        this.brief = brief;
        this.message = message;
    }
}

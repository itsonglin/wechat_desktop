package com.rc.utils;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by song on 01/08/2017.
 */
public class WindowUtil
{
    public static List<WindowInfo> listWindow()
    {
        final List<WindowInfo> windowInfoList = new ArrayList<>();
        final List<Integer> order = new ArrayList<>();

        User32.instance.EnumWindows(new WndEnumProc()
        {
            public boolean callback(int hWnd, int lParam)
            {
                if (User32.instance.IsWindowVisible(hWnd))
                {
                    RECT r = new RECT();
                    User32.instance.GetWindowRect(hWnd, r);
                    if (r.left > -32000)
                    {     // minimized
                        byte[] buffer = new byte[1024];
                        User32.instance.GetWindowTextA(hWnd, buffer, buffer.length);
                        String title = Native.toString(buffer);
                        windowInfoList.add(new WindowInfo(hWnd, r, title));
                    }
                }
                return true;
            }
        }, 0);

        Collections.sort(windowInfoList, new Comparator<WindowInfo>()
        {
            public int compare(WindowInfo o1, WindowInfo o2)
            {
                return order.indexOf(o1.hwnd) - order.indexOf(o2.hwnd);
            }
        });

        return windowInfoList;
    }

    /**
     * 获取系统当前激活的窗口
     * @return
     */
    public static WindowInfo getForegroundWindow()
    {
        int hwnd = User32.instance.GetForegroundWindow();
        RECT r = new RECT();

        User32.instance.GetWindowRect(hwnd, r);
        if (r.left > -32000)
        {
            byte[] buffer = new byte[1024];
            User32.instance.GetWindowTextA(hwnd, buffer, buffer.length);
            String title = Native.toString(buffer);
            WindowInfo info = new WindowInfo(hwnd, r, title);
            return info;
        }

        return null;
    }

    public interface WndEnumProc extends StdCallLibrary.StdCallCallback
    {
        boolean callback(int hWnd, int lParam);
    }

    public  interface User32 extends StdCallLibrary
    {
        User32 instance = (User32) Native.loadLibrary("user32", User32.class);

        boolean EnumWindows(WndEnumProc wndenumproc, int lParam);

        boolean IsWindowVisible(int hWnd);

        int GetWindowRect(int hWnd, RECT r);

        void GetWindowTextA(int hWnd, byte[] buffer, int buflen);

        int GetTopWindow(int hWnd);

        int GetForegroundWindow();

        int GetWindow(int hWnd, int flag);

        final int GW_HWNDNEXT = 2;
    }

    public static class RECT extends Structure
    {
        public int left, top, right, bottom;

        @Override
        protected List<String> getFieldOrder()
        {
            List list = new ArrayList();
            list.add("left");
            list.add("top");
            list.add("right");
            list.add("bottom");

            return list;
        }

        public int getLeft()
        {
            return left;
        }

        public int getTop()
        {
            return top;
        }

        public int getRight()
        {
            return right;
        }

        public int getBottom()
        {
            return bottom;
        }

        public void setLeft(int left)
        {
            this.left = left;
        }

        public void setTop(int top)
        {
            this.top = top;
        }

        public void setRight(int right)
        {
            this.right = right;
        }

        public void setBottom(int bottom)
        {
            this.bottom = bottom;
        }
    }

    public static class WindowInfo
    {
        private int hwnd;
        private RECT rect;
        private String title;

        public WindowInfo(int hwnd, RECT rect, String title)
        {
            this.hwnd = hwnd;
            this.rect = rect;
            this.title = title;
        }


        public String toString()
        {
            return String.format("(%d,%d)-(%d,%d) : \"%s\"",
                    rect.left, rect.top, rect.right, rect.bottom, title);
        }

        public int getHwnd()
        {
            return hwnd;
        }

        public RECT getRect()
        {
            return rect;
        }

        public String getTitle()
        {
            return title;
        }
    }
}

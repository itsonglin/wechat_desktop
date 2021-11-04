package com.rc.frames;


import com.apple.eawt.AppEvent;
import com.apple.eawt.AppForegroundListener;
import com.apple.eawt.AppReOpenedListener;
import com.rc.components.InitComponent;
import com.rc.events.GlobalEventHandler;
import com.rc.res.Colors;
import com.rc.panels.*;
import com.rc.utils.*;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.util.concurrent.ThreadPoolExecutor;

import static com.rc.app.Launcher.APP_NAME;

/**
 * Created by song on 17-5-28.
 * <p>
 * 程序主窗体
 * <p>
 * 实例化时初始化系统托盘以及WebSocket客户端。
 */
public class MainFrame extends JFrame implements InitComponent
{
    public static int DEFAULT_WIDTH = 800;
    public static int DEFAULT_HEIGHT = 666;

    public int currentWindowWidth = DEFAULT_WIDTH;
    public int currentWindowHeight = DEFAULT_HEIGHT;

    private LeftPanel leftPanel;
    private RightPanel rightPanel;

    private static MainFrame context;
    private Image normalTrayIcon; // 正常时的任务栏图标
    private Image emptyTrayIcon; // 闪动时的任务栏图标
    private TrayIcon trayIcon;
    private boolean trayFlashing = false;
    private AudioStream messageSound; //消息到来时候的提示间

    private boolean firstShown = true;

    public MainFrame()
    {
        context = this;
        initResource();
        initialize();
    }

    public void initComponents()
    {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // 任务栏图标
        if (!OSUtil.isMacOS())
        {
            setIconImage(IconUtil.getIcon(this, "/image/ic_launcher.png", true).getImage());
        }

        UIManager.put("Label.font", FontUtil.getDefaultFont());
        UIManager.put("Panel.font", FontUtil.getDefaultFont());
        UIManager.put("TextArea.font", FontUtil.getDefaultFont());

        UIManager.put("Panel.background", Colors.WINDOW_BACKGROUND);
        UIManager.put("CheckBox.background", Colors.WINDOW_BACKGROUND);

        leftPanel = new LeftPanel();
        rightPanel = new RightPanel();
    }

    public void initView()
    {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setMinimumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

        if (OSUtil.isWindows())
        {
            // 隐藏标题栏
            setUndecorated(true);

            String windows = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
            try
            {
                UIManager.setLookAndFeel(windows);
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            //getRootPane().setBorder(new LineBorder(Color.GRAY, 1, true));
        } else if (OSUtil.isLinux())
        {
            // 隐藏标题栏
            setUndecorated(true);
        }

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        centerScreen();
    }

    public void setListeners()
    {
        trayIcon.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                // 显示主窗口
                setToFront();

                // 任务栏图标停止闪动
                if (trayFlashing)
                {
                    trayFlashing = false;
                    trayIcon.setImage(normalTrayIcon);
                }
                super.mouseClicked(e);
            }
        });

        addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                currentWindowWidth = (int) e.getComponent().getBounds().getWidth();
                currentWindowHeight = (int) e.getComponent().getBounds().getHeight();
            }

            @Override
            public void componentShown(ComponentEvent e)
            {
                if (firstShown)
                {
                    firstShown = false;
                    GlobalEventHandler.callMainFrameLoadedListeners(this);
                }
                super.componentShown(e);
            }
        });

        if (OSUtil.isMacOS())
        {
            com.apple.eawt.Application app = com.apple.eawt.Application.getApplication();
            app.addAppEventListener(new AppForegroundListener()
            {
                @Override
                public void appMovedToBackground(AppEvent.AppForegroundEvent arg0)
                {
                }

                @Override
                public void appRaisedToForeground(AppEvent.AppForegroundEvent arg0)
                {
                    setVisible(true);
                }

            });

            app.addAppEventListener((AppReOpenedListener) arg0 -> setVisible(true));
        }


        //全局事件处理器
        Toolkit.getDefaultToolkit().addAWTEventListener(event ->
        {
            MouseEvent me = (MouseEvent) event;
            if (me.getID() == MouseEvent.MOUSE_PRESSED)
            {
                if (me.getButton() == MouseEvent.BUTTON1)
                {
                    Object conn = me.getSource();
                    GlobalEventHandler.callLeftButtonClickedListeners(conn);
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK);

    }

    private void initResource()
    {
        ThreadPoolUtils.execute(() -> initTray());
    }


    /**
     * 播放消息提示间
     */
    public void playMessageSound()
    {
        try
        {
            InputStream inputStream = getClass().getResourceAsStream("/wav/msg.wav");
            messageSound = new AudioStream(inputStream);
            AudioPlayer.player.start(messageSound);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    /**
     * 初始化系统托盘图标
     */
    private void initTray()
    {
        SystemTray systemTray = SystemTray.getSystemTray();//获取系统托盘
        try
        {
            if (OSUtil.isMacOS())
            {
                normalTrayIcon = IconUtil.getIcon(this, "/image/ic_launcher_dark.png", 20, 20, false).getImage();
                trayIcon = new TrayIcon(normalTrayIcon, APP_NAME);
                trayIcon.setImageAutoSize(true);

                trayIcon.addMouseListener(new MouseAdapter()
                {
                    @Override
                    public void mousePressed(MouseEvent e)
                    {
                        // 显示主窗口
                        setToFront();
                        super.mousePressed(e);
                    }
                });
            } else
            {
                normalTrayIcon = IconUtil.getIcon(this, "/image/ic_launcher.png", 20, 20, true).getImage();
                emptyTrayIcon = IconUtil.getIcon(this, "/image/ic_launcher_empty.png", 20, 20, true).getImage();

                trayIcon = new TrayIcon(normalTrayIcon, APP_NAME);
                trayIcon.setImageAutoSize(true);
                PopupMenu menu = new PopupMenu();


                MenuItem exitItem = new MenuItem("退出");
                exitItem.addActionListener(e -> exitApp());

                MenuItem showItem = new MenuItem("打开" + APP_NAME);
                showItem.addActionListener(e -> setToFront());
                menu.add(showItem);
                menu.add(exitItem);
                trayIcon.setPopupMenu(menu);
            }

            systemTray.add(trayIcon);

        } catch (AWTException e)
        {
            e.printStackTrace();
        }
    }

    public void exitApp()
    {
        clearClipboardCache();
        System.exit(1);
    }

    /**
     * 清除剪切板缓存文件
     */
    private void clearClipboardCache()
    {
        ClipboardUtil.clearCache();
    }

    /**
     * 设置任务栏图标闪动
     */
    public void setTrayFlashing()
    {
        trayFlashing = true;
        new Thread(() ->
        {
            while (trayFlashing)
            {
                try
                {
                    trayIcon.setImage(emptyTrayIcon);
                    Thread.sleep(800);

                    trayIcon.setImage(normalTrayIcon);
                    Thread.sleep(800);

                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    public boolean isTrayFlashing()
    {
        return trayFlashing;
    }


    public static MainFrame getContext()
    {
        return context;
    }


    /**
     * 使窗口在屏幕中央显示
     */
    private void centerScreen()
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        this.setLocation((tk.getScreenSize().width - currentWindowWidth) / 2,
                (tk.getScreenSize().height - currentWindowHeight) / 2);
    }


    @Override
    public void dispose()
    {
        // 移除托盘图标
        SystemTray.getSystemTray().remove(trayIcon);
        super.dispose();
    }

    /**
     * 使主窗口在最前面
     */
    public void setToFront()
    {
        setVisible(true);
        setAlwaysOnTop(true);
        setExtendedState(Frame.NORMAL);
        new Thread(() ->
        {
            try
            {
                Thread.sleep(500);
            } catch (InterruptedException e1)
            {
                e1.printStackTrace();
            }

            setAlwaysOnTop(false);
        }).start();
    }

}


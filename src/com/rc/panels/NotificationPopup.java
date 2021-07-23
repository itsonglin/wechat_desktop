package com.rc.panels;

import com.rc.components.Colors;
import com.rc.components.GBC;
import com.rc.components.NotificationTextArea;
import com.rc.components.RCLabel;
import com.rc.frames.MainFrame;
import com.rc.utils.FontUtil;
import com.rc.utils.IconUtil;
import com.rc.utils.notification.NotificationEventListener;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by song on 04/07/2017.
 * <p>
 * 表情弹窗
 */
public class NotificationPopup extends JWindow
{
    public static final int WIDTH = 300;
    public static final int HEIGHT = 80;


    public static NotificationPopup context;

    private JLabel avatarLabel;

    private JPanel contentPanel;


    private RCLabel roomNameLabel;
    private JLabel briefLabel;
    private NotificationTextArea messageLabel;

    private JLabel closeLabel;

    private NotificationEventListener listener;

    /**
     * 开始计时时间
     */
    private volatile long showTime = 0;

    /**
     * 倒计时时间
     */
    public static int SHOW_INTERVAL = 15000;

    private String roomId;

    public NotificationPopup()
    {
        //super(MainFrame.getContext(), false);
        context = this;
        initComponents();
        initView();
        setListeners();
    }

    private void setTimer()
    {
        new Thread(() ->
        {
            while (System.currentTimeMillis() - showTime < SHOW_INTERVAL)
            {
            }

            float op = 1.0F;
            Point p;
            while (op > 0.2F)
            {
                op -= 0.1F;

                NotificationPopup.this.setOpacity(op);
                try
                {
                    Thread.sleep(30);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                p = NotificationPopup.this.getLocation();
                NotificationPopup.this.setLocation(p.x, p.y - 5);
            }

            close();
        }).start();
    }

    private void initComponents()
    {
        avatarLabel = new JLabel();
        avatarLabel.setPreferredSize(new Dimension(50, 50));
        //avatarLabel.setSize(50, 50);
        //avatarLabel.setMinimumSize(new Dimension(50, 50));
        avatarLabel.setIcon(IconUtil.getIcon(this, "/image/ic_launcher.png", 50, 50));

        roomNameLabel = new RCLabel();
        roomNameLabel.setFont(FontUtil.getDefaultFont(16, Font.BOLD));

        briefLabel = new JLabel("admin");
        briefLabel.setFont(FontUtil.getDefaultFont(13, Font.PLAIN));
        briefLabel.setForeground(Colors.FONT_GRAY);

        messageLabel = new NotificationTextArea();
        messageLabel.setFont(FontUtil.getDefaultFont(14, Font.PLAIN));

        contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setPreferredSize(new Dimension(80, 30));
        contentPanel.add(roomNameLabel, new GBC(0, 0).setAnchor(GBC.WEST).setWeight(0, 0).setInsets(10, 0, 0, 0));
        contentPanel.add(briefLabel, new GBC(0, 1).setAnchor(GBC.WEST).setWeight(0, 0).setInsets(-3, 0, 0, 0));
        contentPanel.add(messageLabel, new GBC(0, 2).setFill(GBC.BOTH).setWeight(1, 0).setInsets(-4, 0, 5, 0));
        contentPanel.setPreferredSize(new Dimension(190, 20));

        closeLabel = new JLabel();
        closeLabel.setIcon(new ImageIcon(getClass().getResource("/image/close.png")));
        closeLabel.setHorizontalAlignment(JLabel.CENTER);
        closeLabel.setOpaque(true);
        closeLabel.setPreferredSize(new Dimension(20, 20));
        closeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeLabel.setBackground(Colors.WINDOW_BACKGROUND);

        getRootPane().setBorder(new LineBorder(Colors.LIGHT_GRAY));
    }

    private void initView()
    {
        this.setLayout(new GridBagLayout());
        this.add(avatarLabel, new GBC(0, 0).setWeight(1, 1).setInsets(0, 10, 0, 0));
        this.add(contentPanel, new GBC(1, 0).setFill(GBC.BOTH).setWeight(10, 1).setInsets(0, 8, 0, 0));
        this.add(closeLabel, new GBC(2, 0).setWeight(1, 1).setInsets(8, 0, 0, 0).setAnchor(GBC.NORTH));


        setBackground(Colors.WINDOW_BACKGROUND);
        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        //this.setUndecorated(true);
        //this.setResizable(false);
    }

    private void setListeners()
    {
        closeLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                ((JLabel) e.getSource()).setBackground(Colors.LIGHT_GRAY);
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                ((JLabel) e.getSource()).setBackground(Colors.WINDOW_BACKGROUND);
                super.mouseExited(e);
            }

            @Override
            public void mouseClicked(MouseEvent e)
            {
                close();
                super.mouseClicked(e);
            }
        });

        MouseAdapter adapter = new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                showTime = Long.MAX_VALUE;
                NotificationPopup.this.setOpacity(1.0F);
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                showTime = System.currentTimeMillis();
                super.mouseExited(e);
            }

            @Override
            public void mouseClicked(MouseEvent e)
            {
                MainFrame.getContext().setToFront();
                NavPanel.getContext().show(NavPanel.CHAT);
                RoomsPanel.getContext().openRoom(roomId);
                super.mouseClicked(e);
            }
        };
        this.addMouseListener(adapter);
        this.messageLabel.addMouseListener(adapter);
    }


    public void show(String roomId, ImageIcon ic, String name, String brief, String message, Point location)
    {
        new Thread(() ->
        {
            this.roomId = roomId;
            showTime = System.currentTimeMillis();
            this.roomNameLabel.setText(name);
            this.messageLabel.setText(message);
            if (brief == null || brief.isEmpty())
            {
                this.briefLabel.setText(" ");
            } else
            {
                this.briefLabel.setText(brief);
            }


            this.setAlwaysOnTop(true);

            ImageIcon icon = ic;
            if (icon != null)
            {
                if (icon.getIconHeight() > 50 || icon.getIconWidth() > 50)
                {
                    icon = new ImageIcon(icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
                }

                this.avatarLabel.setIcon(icon);
            }

            float op = 0.1F;
            while (op <= 1.0F)
            {
                if (op > 0.9F)
                {
                    op = 1.0F;
                }

                setOpacity(op);
                op += 0.1F;
                sleep(30);
            }

            setLocation(location);
            setVisible(true);
            setTimer();

            if (listener != null)
            {
                listener.onShown(this);
            }
        }).start();
    }

    private void sleep(int t)
    {
        try
        {
            Thread.sleep(t);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private void close()
    {
        this.setAlwaysOnTop(false);
        NotificationPopup.this.setVisible(false);
        if (listener != null)
        {
            listener.onClosed(this);
        }
    }

    public NotificationEventListener getListener()
    {
        return listener;
    }

    public void setListener(NotificationEventListener listener)
    {
        this.listener = listener;
    }

    public String getText()
    {
        return messageLabel.getText();
    }
}

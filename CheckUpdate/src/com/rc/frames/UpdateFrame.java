package com.rc.frames;

import com.rc.frames.components.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by song on 2017/6/27.
 */
public class UpdateFrame extends JFrame
{
    private static final int FRAME_WIDTH = 650;
    private static final int FRAME_HEIGHT = 210;
    private static Point origin = new Point();

    private JPanel logoPanel;
    private JPanel progressBarPanel;
    private JLabel logoLabel;
    private JLabel messageLabel;
    private RCProgressBar progressBar;
    private static final String updateServerPath = "https://www.baidu.com/app.jar";

    private UpdateResultListener updateResultListener;

    int count = 0;

    public UpdateFrame()
    {
        initComponents();
        initView();
        setListeners();

        updateTitle();
        download();
    }

    private void download()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                DownloadTask task = new DownloadTask(new HttpUtil.ProgressListener()
                {
                    @Override
                    public void onProgress(int progress)
                    {
                       /* try
                        {
                            Thread.sleep(1);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }*/
                        progressBar.setValue(progress);
                    }
                });

                task.setListener(new HttpResponseListener<byte[]>()
                {
                    @Override
                    public void onResult(byte[] ret)
                    {
                        saveFile(ret);
                    }
                });

                task.execute(updateServerPath);
            }
        }).start();
    }

    /**
     * 保存下载文件
     *
     * @param ret
     */
    private void saveFile(byte[] ret)
    {
        if (ret == null)
        {
            updateResultListener.onFailed();
            return;
        }

        File oldFile = new File("wechat.jar");
        if (oldFile.exists())
        {
            oldFile.renameTo(new File("wechat_old.jar"));
        }

        File file = new File("wechat.jar");
        try
        {
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(ret);
            System.out.println("文件保存在：" + file.getAbsolutePath());

            if (updateResultListener != null)
            {
                updateResultListener.onSuccess();
            }
        }
        catch (Exception e)
        {
            File oFile = new File("wechat_old.jar");
            oFile.renameTo(new File("wechat.jar"));

            JOptionPane.showMessageDialog(null, "更新失败，正在还原...", "更新失败", JOptionPane.ERROR_MESSAGE);
            if (updateResultListener != null)
            {
                updateResultListener.onFailed();
            }

            e.printStackTrace();
        }
    }

    private void updateTitle()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    String dot = "";
                    switch (count++ % 4)
                    {
                        case 0:
                            dot = "";
                            break;
                        case 1:
                            dot = "·";
                            break;
                        case 2:
                            dot = "··";
                            break;
                        case 3:
                            dot = "···";
                            break;
                    }

                    messageLabel.setText("微信 正在更新中" + dot);

                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }


    private void initComponents()
    {
        logoPanel = new JPanel();
        logoPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 0));

        logoLabel = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource("ic_launcher.png"));
        icon.setImage(icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH));
        logoLabel.setIcon(icon);
        setIconImage(icon.getImage());

        messageLabel = new JLabel("微信 正在更新中...");
        messageLabel.setFont(FontUtil.getDefaultFont(32));
        messageLabel.setForeground(Colors.FONT_GRAY);


        progressBarPanel = new JPanel();
        progressBarPanel.setLayout(new GridBagLayout());

        progressBar = new RCProgressBar();
        progressBar.setMaximum(100);
        progressBar.setMinimum(0);
        progressBar.setValue(1);
        progressBar.setUI(new GradientProgressBarUI());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initView()
    {
        this.setSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        centerScreen();

        logoPanel.add(logoLabel);
        logoPanel.add(messageLabel);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(FRAME_WIDTH, 10));
        panel.add(progressBar, new GBC(0, 0).setFill(GBC.BOTH).setWeight(1, 1));
        progressBarPanel.add(panel, new GBC(0, 0).setFill(GBC.HORIZONTAL).setWeight(1, 1));


        this.setLayout(new GridBagLayout());
        add(logoPanel, new GBC(0, 0).setWeight(1, 1).setFill(GBC.BOTH).setInsets(40, 120, 0, 0));
        add(progressBarPanel, new GBC(0, 1).setWeight(1, 1).setFill(GBC.BOTH).setInsets(0, 0, 20, 0));
    }

    private void setListeners()
    {
        if (OSUtil.getOsType() != OSUtil.Mac_OS)
        {
            setUndecorated(true);

            addMouseListener(new MouseAdapter()
            {
                public void mousePressed(MouseEvent e)
                {
                    origin.x = e.getX();
                    origin.y = e.getY();
                }
            });

            addMouseMotionListener(new MouseMotionAdapter()
            {
                public void mouseDragged(MouseEvent e)
                {
                    Point p = UpdateFrame.this.getLocation();
                    // 设置窗口的位置
                    UpdateFrame.this.setLocation(p.x + e.getX() - origin.x, p.y + e.getY()
                            - origin.y);
                }
            });
        }
    }

    /**
     * 使窗口在屏幕中央显示
     */
    private void centerScreen()
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        this.setLocation((tk.getScreenSize().width - FRAME_WIDTH) / 2,
                (tk.getScreenSize().height - FRAME_HEIGHT) / 2);
    }

    public void setUpdateResultListener(UpdateResultListener updateResultListener)
    {
        this.updateResultListener = updateResultListener;
    }
}

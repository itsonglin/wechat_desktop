package com.rc.panels;

import com.rc.components.Colors;
import com.rc.components.GBC;
import com.rc.components.RCButton;
import com.rc.utils.FileCache;
import com.rc.utils.IconUtil;
import com.rc.utils.ImageCache;

import javax.swing.*;
import javax.swing.tree.ExpandVetoException;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by song on 10/07/2017.
 */
public class ClearCachePanel extends JPanel
{
    private JLabel infoLabel;
    private RCButton clearButton;
    private String fileCachePath;
    private String imageCachePath;

    public ClearCachePanel()
    {
        initComponents();
        initView();
        setListeners();
    }

    private void setListeners()
    {
        clearButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (clearButton.isEnabled())
                {
                    clearButton.setText("清除中...");
                    clearButton.setIcon(IconUtil.getIcon(this, "/image/loading_small.gif"));
                    clearButton.setEnabled(false);
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                deleteAllFiles(fileCachePath);
                                deleteAllFiles(imageCachePath);

                                clearButton.setText("缓存清理完成！");
                                clearButton.setIcon(IconUtil.getIcon(this, "/image/check.png"));
                                infoLabel.setText("当前缓存占用磁盘空间：0 字节");
                            }
                            catch (Exception e)
                            {
                                clearButton.setText("清除失败");
                            }

                            clearButton.setEnabled(true);
                        }
                    }).start();
                }

                super.mouseClicked(e);
            }
        });
    }

    private void initComponents()
    {
        infoLabel = new JLabel("当前缓存占用磁盘空间：计算中...");
        clearButton = new RCButton("清除缓存", Colors.MAIN_COLOR, Colors.MAIN_COLOR_DARKER, Colors.MAIN_COLOR_DARKER);
        clearButton.setPreferredSize(new Dimension(150, 35));
        ToolTipManager.sharedInstance().setDismissDelay(10000);
        clearButton.setToolTipText("清除已缓存的聊天图片及文件，清除后下次使用相应的图片或文件时会重新从服务器获取。");

        calculateCacheSize();
    }

    private void calculateCacheSize()
    {

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                fileCachePath = new FileCache().FILE_CACHE_ROOT_PATH;
                imageCachePath = new ImageCache().IMAGE_CACHE_ROOT_PATH;

                long size = getDirectorySize(fileCachePath);
                size += getDirectorySize(imageCachePath);

                infoLabel.setText("当前缓存占用磁盘空间：" + fileSizeString(size));
            }
        }).start();
    }

    private String fileSizeString(long size)
    {
        DecimalFormat decimalFormat = new DecimalFormat("#.0");

        String retString = "";
        if (size < 1024)
        {
            retString = size + " 字节";
        }
        else if (size < 1024 * 1024)
        {
            retString = decimalFormat.format(size * 1.0F / 1024) + " KB";
        }
        else
        {
            retString = decimalFormat.format(size * 1.0F / 1024 / 1024) + " MB";
        }

        return retString;
    }

    /**
     * 获取指定文件夹的总文件大小
     *
     * @param fileCachePath
     * @return
     */
    private long getDirectorySize(String fileCachePath)
    {
        long size = 0;
        File file = new File(fileCachePath);

        if (file.exists() && file.isDirectory())
        {
            File[] files = file.listFiles();
            for (File f : files)
            {
                if (f.isDirectory())
                {
                    size += getDirectorySize(f.getAbsolutePath());
                }
                else
                {
                    size += f.length();
                }
            }
        }
        else
        {
            throw new RuntimeException("文件不存在或非文件夹");
        }

        return size;
    }

    private void deleteAllFiles(String fileCachePath)
    {
        File file = new File(fileCachePath);

        if (file.exists() && file.isDirectory())
        {
            File[] files = file.listFiles();
            for (File f : files)
            {
                if (f.isDirectory())
                {
                    deleteAllFiles(f.getAbsolutePath());
                }
                else
                {
                    f.delete();
                }
            }
        }
        else
        {
            throw new RuntimeException("文件不存在或非文件夹");
        }
    }

    private void initView()
    {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(300, 150));
        panel.add(infoLabel, BorderLayout.NORTH);
        panel.add(clearButton, BorderLayout.CENTER);

        this.setLayout(new GridBagLayout());
        add(panel, new GBC(0, 0).setAnchor(GBC.NORTH).setFill(GBC.HORIZONTAL).setInsets(-200, 0, 0, 0));
    }


}

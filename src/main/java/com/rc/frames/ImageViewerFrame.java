package com.rc.frames;

import com.rc.res.Colors;
import com.rc.components.ImageLabel;
import com.rc.components.RCMenuItemUI;
import com.rc.res.Cursors;
import com.rc.utils.IconUtil;
import com.rc.utils.OSUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import static com.rc.utils.ImageUtil.isGif;

/**
 * Created by song on 2017/6/25.
 */
public class ImageViewerFrame extends JFrame
{
    private int minWidth;
    private int minHeight;

    private int maxWidth;
    private int maxHeight;

    private ImageLabel imageLabel;
    private String imagePath;
    private Toolkit tooKit;

    private Image image;

    private JPopupMenu popupMenu;
    private JMenuItem saveAsItem;
    private JMenuItem enlargeItem;
    private JMenuItem narrowItem;

    private JPanel controlPanel;
    private static final int controlPanelHeight = 30;
    private JLabel closeLabel;
    private JLabel maxLabel;
    private JLabel minLabel;

    private ImageIcon maxIcon;
    private ImageIcon restoreIcon;

    private boolean windowMax; // 当前窗口是否已最大化
    private Rectangle desktopBounds; // 去除任务栏后窗口的大小
    private Rectangle normalBounds;
    private int actualWidth;
    private int actualHeight;

    private static Point origin = new Point();

    private boolean isGif;

    public ImageViewerFrame(String imagePath)
    {
        this(imagePath, null);
    }

    public ImageViewerFrame(Image image)
    {
        this(null, image);
    }

    private ImageViewerFrame(String imagePath, Image image)
    {
        this.imagePath = imagePath;
        this.image = image;

        tooKit = Toolkit.getDefaultToolkit();
        isGif = imagePath != null && isGif(imagePath);

        initComponents();
        initView();
        initSize();


        try
        {
            initImageAndFrameBounds();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        initBounds();

        setListeners();
    }

    private void initComponents()
    {
        popupMenu = new JPopupMenu();
        enlargeItem = new JMenuItem("放大");
        narrowItem = new JMenuItem("缩小");
        saveAsItem = new JMenuItem("另存为");

        saveAsItem.setUI(new RCMenuItemUI());
        narrowItem.setUI(new RCMenuItemUI());
        enlargeItem.setUI(new RCMenuItemUI());

        popupMenu.add(saveAsItem);

        if (isGif)
        {
            this.setResizable(false);
        } else
        {
            popupMenu.add(enlargeItem);
            popupMenu.add(narrowItem);
        }

        imageLabel = new ImageLabel(isGif);
        setIconImage(IconUtil.getIcon(this, "/image/ic_launcher.png", true).getImage());

        if (OSUtil.getOsType() == OSUtil.Windows)
        {
            setUndecorated(true);
            getRootPane().setBorder(new LineBorder(new Color(204, 204, 204)));

            Dimension controlLabelSize = new Dimension(30, 30);
            maxIcon = new ImageIcon(getClass().getResource("/image/window_max.png"));
            restoreIcon = new ImageIcon(getClass().getResource("/image/window_restore.png"));
            ControlLabelMouseListener listener = new ControlLabelMouseListener();

            closeLabel = new JLabel();
            closeLabel.setIcon(new ImageIcon(getClass().getResource("/image/close.png")));
            closeLabel.setHorizontalAlignment(JLabel.CENTER);
            closeLabel.setOpaque(true);
            closeLabel.addMouseListener(listener);
            closeLabel.setPreferredSize(controlLabelSize);
            closeLabel.setCursor(Cursors.HAND_CURSOR);

            maxLabel = new JLabel();
            maxLabel.setIcon(maxIcon);
            maxLabel.setHorizontalAlignment(JLabel.CENTER);
            maxLabel.setOpaque(true);
            maxLabel.addMouseListener(listener);
            maxLabel.setPreferredSize(controlLabelSize);
            maxLabel.setCursor(Cursors.HAND_CURSOR);

            minLabel = new JLabel();
            minLabel.setIcon(new ImageIcon(getClass().getResource("/image/window_min.png")));
            minLabel.setHorizontalAlignment(JLabel.CENTER);
            minLabel.setOpaque(true);
            minLabel.addMouseListener(listener);
            minLabel.setPreferredSize(controlLabelSize);
            minLabel.setCursor(Cursors.HAND_CURSOR);

            controlPanel = new JPanel();
            controlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            controlPanel.add(minLabel);
            controlPanel.add(maxLabel);
            controlPanel.add(closeLabel);
        }
    }

    private void initBounds()
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        //上面这种方式获取的是整个显示屏幕的大小，包含了任务栏的高度。
        Insets screenInsets = Toolkit.getDefaultToolkit()
                .getScreenInsets(getGraphicsConfiguration());
        desktopBounds = new Rectangle(
                screenInsets.left, screenInsets.top,
                screenSize.width - screenInsets.left - screenInsets.right,
                screenSize.height - screenInsets.top - screenInsets.bottom);

        normalBounds = new Rectangle(this.getLocation(), new Dimension(actualWidth, actualHeight));

    }

    private void initView()
    {
        if (OSUtil.getOsType() == OSUtil.Windows)
        {
            controlPanel.setPreferredSize(new Dimension(100, controlPanelHeight));
            add(controlPanel, BorderLayout.NORTH);
        }
        add(imageLabel);
    }


    private void initSize()
    {
        // 窗口最小宽度、高度
        minWidth = 200;
        minHeight = 200;

        // 窗口最大宽度、高度
        Dimension screenSize = tooKit.getScreenSize();
        int screenSizeWidth = (int) screenSize.getWidth();
        int screenSizeHeight = (int) screenSize.getHeight();

        maxWidth = (int) (screenSizeWidth * 0.6);
        maxHeight = (int) (screenSizeHeight * 0.8);
    }


    private void initImageAndFrameBounds() throws IOException
    {
        if (imagePath != null)
        {
            image = ImageIO.read(new File(imagePath));
        } else
        {
            if (image == null)
            {
                throw new RuntimeException("必须至少提供imagePath 或 image");
            }
        }

        scaleImage();

        if (OSUtil.getOsType() == OSUtil.Windows)
        {
            this.setSize(new Dimension(actualWidth, actualHeight + controlPanelHeight));
        }
        else
        {
            this.setSize(new Dimension(actualWidth, actualHeight));
        }

        this.setLocation((tooKit.getScreenSize().width - actualWidth) / 2, (tooKit.getScreenSize().height - actualHeight) / 2);
    }


    private void scaleImage()
    {
        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);
        float imageScale = imageWidth * 1.0F / imageHeight; // 图像宽高比

        actualWidth = imageWidth;
        actualHeight = imageHeight;

        if (imageWidth >= imageHeight)
        {
            if (imageWidth > maxWidth)
            {
                actualWidth = maxWidth;
                actualHeight = (int) (actualWidth / imageScale);
            } else if (imageWidth < minWidth)
            {
                actualWidth = minWidth;
                actualHeight = (int) (actualWidth / imageScale);

            }
        } else
        {
            if (imageHeight > maxHeight)
            {
                actualHeight = maxHeight;
                actualWidth = (int) (actualHeight * imageScale);
            } else if (imageHeight < minHeight)
            {
                actualHeight = minHeight;
                actualWidth = (int) (actualHeight * imageScale);
            }
        }


        // 如果图片宽度大于最小显示宽度，需要调整图片大小， 使图片布满整个窗口
        if (imageWidth > minWidth || imageHeight > minHeight)
        {
            // 图片的实际大小
            int h = actualHeight;
            int w = actualWidth;
            if (OSUtil.getOsType() == OSUtil.Windows)
            {
                h = actualHeight - 2/* - controlPanelHeight - 2*/;
                w -= 2; // 减去边框
            } else if (OSUtil.getOsType() == OSUtil.MacOS)
            {
                h = actualHeight - 22;
            }

            if (!isGif)
            {
                image = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            }
        }

        if (!isGif)
        {
            imageLabel.setSourceImage(image);
        } else
        {
            imageLabel.setSourceGifImage(imagePath);
        }
    }


    private void setListeners()
    {

        imageLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON3)
                {
                    popupMenu.show(imageLabel, e.getX(), e.getY());
                }
                super.mouseClicked(e);
            }
        });

        enlargeItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                imageLabel.scaleImage(0.15F);
            }
        });

        narrowItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                imageLabel.scaleImage(-0.15F);
            }
        });

        saveAsItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setDialogType(JFileChooser.SAVE_DIALOG);
                chooser.showDialog(ImageViewerFrame.this, "保存");
                File file = chooser.getSelectedFile();

                if (file != null)
                {
                    // 如果用户没有输入扩展名，自动加上扩展名
                    String path = file.getAbsolutePath();
                    int startPos = imagePath.lastIndexOf(".");
                    String suffix = "";
                    if (startPos > -1)
                    {
                        suffix = imagePath.substring(startPos);
                    }

                    if (path.indexOf(".") < 0)
                    {
                        path += suffix;
                    }

                    try (FileOutputStream outputStream = new FileOutputStream(path);
                         FileInputStream inputStream = new FileInputStream(imagePath))
                    {

                        byte[] buffer = new byte[2048];
                        int len = -1;
                        while ((len = inputStream.read(buffer)) > -1)
                        {
                            outputStream.write(buffer, 0, len);
                        }
                    } catch (FileNotFoundException e1)
                    {
                        e1.printStackTrace();
                    } catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                }
            }
        });

        addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    setVisible(false);
                    dispose();
                }
                super.keyPressed(e);
            }
        });

        if (OSUtil.getOsType() == OSUtil.Windows)
        {
            MouseAdapter mouseAdapter = new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    if (e.getClickCount() >= 2)
                    {
                        maxOrRestoreWindow();
                    }

                    super.mouseClicked(e);
                }

                public void mousePressed(MouseEvent e)
                {
                    // 当鼠标按下的时候获得窗口当前的位置
                    origin.x = e.getX();
                    origin.y = e.getY();
                }
            };

            MouseMotionListener mouseMotionListener = new MouseMotionAdapter()
            {
                public void mouseDragged(MouseEvent e)
                {
                    // 当鼠标拖动时获取窗口当前位置
                    Point p = getLocation();
                    // 设置窗口的位置
                    setLocation(p.x + e.getX() - origin.x, p.y + e.getY()
                            - origin.y);
                }
            };

            controlPanel.addMouseListener(mouseAdapter);
            controlPanel.addMouseMotionListener(mouseMotionListener);

            this.addMouseListener(mouseAdapter);
            this.addMouseMotionListener(mouseMotionListener);
        }
    }

    private class ControlLabelMouseListener extends MouseAdapter
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            if (e.getComponent() == closeLabel)
            {
                ImageViewerFrame.this.setVisible(false);
                ImageViewerFrame.this.dispose();

            } else if (e.getComponent() == maxLabel)
            {
                maxOrRestoreWindow();
            } else if (e.getComponent() == minLabel)
            {
                ImageViewerFrame.this.setExtendedState(JFrame.ICONIFIED);
            }
        }

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
    }

    private void maxOrRestoreWindow()
    {
        if (windowMax)
        {
            ImageViewerFrame.this.setBounds(normalBounds);
            maxLabel.setIcon(maxIcon);
            windowMax = false;
        } else
        {
            ImageViewerFrame.this.setBounds(desktopBounds);
            maxLabel.setIcon(restoreIcon);
            windowMax = true;
        }
    }
}



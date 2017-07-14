package com.rc.forms;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by song on 2017/6/25.
 */
public class ImageViewerFrame extends JDialog
{
    private int minWidth;
    private int minHeight;

    private int maxWidth;
    private int maxHeight;

    float maxScale = 3.0F;
    float minScale = 0.2F;

    private ImageLabel imageLabel;
    private String imagePath;
    private Toolkit tooKit;


    // 图片缩放比例
    private float scale = 1.0F;

    private Image image;

    private int xDistance;
    private int yDistance;
    private int x;
    private int y;

    private JPopupMenu popupMenu;
    private JMenuItem saveAsItem;
    private JMenuItem enlargeItem;
    private JMenuItem narrowItem;

    public ImageViewerFrame(String imagePath)
    {
        this(imagePath, null);
    }

    public ImageViewerFrame(Image image)
    {
        this(null, image);
    }

    private  ImageViewerFrame(String imagePath, Image image)
    {
        tooKit = Toolkit.getDefaultToolkit();

        initComponents();
        initView();
        initSize();

        this.imagePath = imagePath;
        this.image = image;


        try
        {
            initImageAndFrameBounds();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

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

        popupMenu.add(enlargeItem);
        popupMenu.add(narrowItem);
        popupMenu.add(saveAsItem);

        imageLabel = new ImageLabel();

        setIconImage(new ImageIcon(getClass().getResource("ic_launcher.png")).getImage());

    }

    private void initView()
    {
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
        }
        else
        {
            if (image == null)
            {
                throw new RuntimeException("必须至少提供imagePath 或 image");
            }
        }


        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);
        float imageScale = imageWidth * 1.0F / imageHeight; // 图像宽高比

        int actualWidth = imageWidth;
        int actualHeight = imageHeight;

        boolean needScale = false; // 是否需要对图片进行缩放
        if (imageWidth >= imageHeight)
        {
            if (imageWidth > maxWidth)
            {
                actualWidth = maxWidth;
                actualHeight = (int) (actualWidth / imageScale);
                needScale = true;
            }
            else if (imageWidth < minWidth)
            {
                actualWidth = minWidth;
                actualHeight = (int) (actualWidth / imageScale);

            }
        }
        else
        {
            if (imageHeight > maxHeight)
            {
                actualHeight = maxHeight;
                actualWidth = (int) (actualHeight * imageScale);
                needScale = true;
            }
            else if (imageHeight < minHeight)
            {
                actualHeight = minHeight;
                actualWidth = (int) (actualHeight * imageScale);
            }
        }

        if (needScale)
        {
            image = image.getScaledInstance(actualWidth, actualHeight, Image.SCALE_SMOOTH);
        }

        //imageLabel.setIcon(imageIcon);
        imageLabel.setImage(image);

        this.setSize(new Dimension(actualWidth, actualHeight + 22));
        this.setLocation((tooKit.getScreenSize().width - actualWidth) / 2, (tooKit.getScreenSize().height - actualHeight) / 2);
    }

    public Image scaledImage(float scale)
    {
        int scaledWidth = (int) (image.getWidth(null) * scale);
        int scaledHeight = (int) (image.getHeight(null) * scale);

        Image scaledimage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_FAST);
        return scaledimage;
    }

    private void setListeners()
    {
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
                }
                else
                {
                    increment = -0.15F;
                }
                doScale(increment);

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
                    imageLabel.moveImage(xDistance, yDistance);
                }

                super.mouseDragged(e);
            }

            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON3)
                {
                    popupMenu.show(imageLabel, e.getX(), e.getY());
                }
                super.mouseClicked(e);
            }
        };

        imageLabel.addMouseWheelListener(listener);
        imageLabel.addMouseMotionListener(listener);
        imageLabel.addMouseListener(listener);

        enlargeItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doScale(0.15F);
            }
        });

        narrowItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                doScale(-0.15F);
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
                        path  += suffix;
                    }

                    try
                    {
                        FileOutputStream outputStream = new FileOutputStream(path);
                        FileInputStream inputStream = new FileInputStream(imagePath);

                        byte[] buffer = new byte[2048];
                        int len = -1;
                        while ((len = inputStream.read(buffer)) > -1)
                        {
                            outputStream.write(buffer, 0, len);
                        }

                        outputStream.close();
                        inputStream.close();
                    }
                    catch (FileNotFoundException e1)
                    {
                        e1.printStackTrace();
                    }
                    catch (IOException e1)
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
    }

    private void doScale(float increment)
    {
        scale = scale + increment;
        //System.out.println("倍率：" + scale);

        if (scale > maxScale)
        {
            scale = maxScale;
        }
        else if (scale < minScale)
        {
            scale = minScale;
        }
        else
        {
            if (Math.abs(scale - 1.0F) < 0.1F)
            {
                scale = 1.0F;
            }
            Image scaledImage = scaledImage(scale);
            imageLabel.scaleImage(scaledImage);
        }
    }
}

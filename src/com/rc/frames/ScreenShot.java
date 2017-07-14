package com.rc.frames;

import com.rc.components.Colors;
import com.rc.panels.ChatPanel;
import com.rc.utils.ClipboardUtil;
import com.rc.utils.IconUtil;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenShot extends JFrame
{
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    private BufferedImage image = null;
    private BufferedImage tempImage = null;
    private BufferedImage saveImage = null;
    private JDialog controlDialog = new JDialog();
    private int selectedWidth;
    private int selectedHeight;
    private int drawX;
    private int drawY;
    private boolean mouseDragged = false;
    private int maxWidth;
    private int maxHeight;
    boolean isShown = false;

    private static final int OUTSIDE_SELECTED = -1;
    private static final int IN_SELECTED_AREA = 0;
    private static final int LEFT_TOP = 1;
    private static final int LEFT_BOTTOM = 2;
    private static final int RIGHT_TOP = 3;
    private static final int RIGHT_BOTTOM = 4;

    private Cursor crossCursor;
    private Cursor moveCursor;
    private Cursor NWresizeCursor;
    private Cursor SWresizeCursor;
    private Cursor NEresizeCursor;
    private Cursor SEresizeCursor;


    private int mouseDownArea = OUTSIDE_SELECTED;

    public ScreenShot() throws AWTException
    {
        setUndecorated(true);
        setBackground(Colors.DARK);

        setOpacity(0); //初始时设置窗口为透明，防止窗口闪烁

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        crossCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
        moveCursor = new Cursor(Cursor.MOVE_CURSOR);
        NWresizeCursor = new Cursor(Cursor.NW_RESIZE_CURSOR);
        SWresizeCursor = new Cursor(Cursor.SW_RESIZE_CURSOR);
        NEresizeCursor = new Cursor(Cursor.NE_RESIZE_CURSOR);
        SEresizeCursor = new Cursor(Cursor.SE_RESIZE_CURSOR);


        setCursor(crossCursor);

        screenShot();
        initControlDialog();
        setListeners();
    }

    private void setListeners()
    {
        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                mouseDownArea = getMousePosition(e);

                startX = e.getX();
                startY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (mouseDragged)
                {
                    mouseDragged = false;

                    controlDialog.setBounds(drawX + 8, drawY + selectedHeight + 10, 200, 50);
                    controlDialog.setVisible(true);
                }
            }


            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() >= 2)
                {
                    close();
                    ClipboardUtil.copyImage(saveImage);
                    ChatPanel.getContext().paste();
                }

                super.mouseClicked(e);
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                mouseDragged = true;
                endX = e.getX();
                endY = e.getY();

                Image tempImage2 = createImage(ScreenShot.this.getWidth(), ScreenShot.this.getHeight());
                Graphics g = tempImage2.getGraphics();
                g.drawImage(tempImage, 0, 0, null);

                // 如果鼠标落在选定区域内，则鼠标移动时移动选定区域
                if (mouseDownArea == IN_SELECTED_AREA)
                {
                    int xDistance = e.getX() - startX;
                    int yDistance = e.getY() - startY;

                    drawX += xDistance;
                    drawY += yDistance;

                    // 保证不会越界
                    drawX = drawX < 0 ? 0 : drawX;
                    drawY = drawY < 0 ? 0 : drawY;
                    drawX = drawX + selectedWidth > maxWidth ? maxWidth - selectedWidth : drawX;
                    drawY = drawY + selectedHeight > maxHeight ? maxHeight - selectedHeight : drawY;

                    startX = e.getX();
                    startY = e.getY();
                }
                // 选定新的区域
                else if (mouseDownArea == OUTSIDE_SELECTED)
                {
                    drawX = Math.min(startX, endX);
                    drawY = Math.min(startY, endY);
                    selectedWidth = Math.abs(endX - startX) + 1;
                    selectedHeight = Math.abs(endY - startY) + 1;
                }
                // 落在四个角
                else
                {
                    int xDistance = e.getX() - startX;
                    int yDistance = e.getY() - startY;


                    switch (mouseDownArea)
                    {
                        case LEFT_TOP:
                        {
                            drawX += xDistance;
                            drawY += yDistance;
                            selectedWidth -= xDistance;
                            selectedHeight -= yDistance;

                            break;
                        }
                        case LEFT_BOTTOM:
                        {
                            drawX += xDistance;

                            selectedWidth -= xDistance;
                            selectedHeight += yDistance;

                            break;
                        }
                        case RIGHT_TOP:
                        {
                            drawY += yDistance;

                            selectedWidth += xDistance;
                            selectedHeight -= yDistance;

                            break;
                        }
                        case RIGHT_BOTTOM:
                        {

                            selectedWidth += xDistance;
                            selectedHeight += yDistance;

                            break;
                        }

                    }
                    selectedWidth = selectedWidth < 1 ? 1 : selectedWidth;
                    selectedHeight = selectedHeight < 1 ? 1 : selectedHeight;

                    startX = e.getX();
                    startY = e.getY();
                }

                g.setColor(Color.CYAN);

                // 绘制选定区域矩形
                g.drawRect(drawX - 1, drawY - 1, selectedWidth + 1, selectedHeight + 1);

                // 绘制四角锚点
                g.fillRect(drawX - 8, drawY - 8, 8, 8);
                g.fillRect(drawX + selectedWidth, drawY - 8, 8, 8);
                g.fillRect(drawX - 8, drawY + selectedHeight, 8, 8);
                g.fillRect(drawX + selectedWidth, drawY + selectedHeight, 8, 8);

                selectedWidth = selectedWidth > maxWidth ? maxWidth : selectedWidth;
                selectedHeight = selectedHeight > maxHeight ? maxHeight : selectedHeight;

                saveImage = image.getSubimage(drawX, drawY, selectedWidth, selectedHeight);
                g.drawImage(saveImage, drawX, drawY, null);

                ScreenShot.this.getGraphics().drawImage(tempImage2,
                        0, 0, ScreenShot.this);

                if (controlDialog.isVisible())
                {
                    controlDialog.setVisible(false);
                }

            }

            @Override
            public void mouseMoved(MouseEvent e)
            {
                int mousePosition = getMousePosition(e);
                switch (mousePosition)
                {
                    case IN_SELECTED_AREA:
                    {
                        setCursor(moveCursor);
                        break;
                    }
                    case OUTSIDE_SELECTED:
                    {
                        setCursor(crossCursor);
                        break;
                    }
                    case LEFT_TOP:
                    {
                        setCursor(NWresizeCursor);
                        break;
                    }
                    case LEFT_BOTTOM:
                    {
                        setCursor(SWresizeCursor);
                        break;
                    }
                    case RIGHT_TOP:
                    {
                        setCursor(NEresizeCursor);
                        break;
                    }
                    case RIGHT_BOTTOM:
                    {
                        setCursor(SEresizeCursor);
                        break;
                    }
                }

                super.mouseMoved(e);
            }
        });

        KeyListener keyListener = new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    close();
                }
                else if (e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    close();
                    ClipboardUtil.copyImage(saveImage);
                    ChatPanel.getContext().paste();
                }
            }
        };

        addKeyListener(keyListener);
        controlDialog.addKeyListener(keyListener);
    }

    private int getMousePosition(MouseEvent e)
    {
        int x = e.getX();
        int y = e.getY();

        if (x >= drawX && x <= drawX + selectedWidth && y >= drawY && y <= drawY + selectedHeight)
        {
            return IN_SELECTED_AREA;
        }
        else if (x >= drawX - 8 && x <= drawX && y >= drawY - 8 && y <= drawY)
        {
            return LEFT_TOP;
        }
        else if (x >= drawX + selectedWidth && x <= drawX + selectedWidth + 8 && y >= drawY - 8 && y <= drawY)
        {
            return RIGHT_TOP;
        }
        else if (x >= drawX - 8 && x <= drawX && y >= drawY + selectedHeight && y <= drawY + selectedHeight + 8)
        {
            return LEFT_BOTTOM;
        }
        else if (x >= drawX + selectedWidth && x <= drawX + selectedWidth + 8 && y >= drawY + selectedHeight && y <= drawY + selectedHeight + 8)
        {
            return RIGHT_BOTTOM;
        }
        else
        {
            return OUTSIDE_SELECTED;
        }
    }

    private void screenShot() throws AWTException
    {
        //获取默认屏幕设备
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice screen = environment.getDefaultScreenDevice();

        //获取屏幕尺寸
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(0, 0, d.width, d.height);

        //获取屏幕截图
        Robot robot = new Robot(screen);
        image = robot.createScreenCapture(new Rectangle(0, 0, d.width, d.height));

        maxWidth = image.getWidth();
        maxHeight = image.getHeight();
    }

    private void initControlDialog()
    {
        controlDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        controlDialog.setAlwaysOnTop(true);
        controlDialog.setUndecorated(true);
        controlDialog.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        JLabel okLabel = new JLabel(IconUtil.getIcon(this, "/image/ok.png"));
        JLabel cancelLabel = new JLabel(IconUtil.getIcon(this, "/image/cancel.png"));
        JLabel downloadLabel = new JLabel(IconUtil.getIcon(this, "/image/download.png"));
        Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);

        downloadLabel.setHorizontalAlignment(SwingConstants.CENTER);
        downloadLabel.setCursor(handCursor);
        downloadLabel.setToolTipText("保存截图");

        okLabel.setHorizontalAlignment(SwingConstants.CENTER);
        okLabel.setCursor(handCursor);
        okLabel.setToolTipText("确定");

        cancelLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cancelLabel.setCursor(handCursor);
        cancelLabel.setToolTipText("取消");


        downloadLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                close();
                try
                {
                    saveImage();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
                super.mouseClicked(e);
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                downloadLabel.setIcon(IconUtil.getIcon(this, "/image/download_active.png"));
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                downloadLabel.setIcon(IconUtil.getIcon(this, "/image/download.png"));
                super.mouseExited(e);
            }
        });

        okLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                close();
                ClipboardUtil.copyImage(saveImage);
                ChatPanel.getContext().paste();
                super.mouseClicked(e);
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                okLabel.setIcon(IconUtil.getIcon(this, "/image/ok_active.png"));
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                okLabel.setIcon(IconUtil.getIcon(this, "/image/ok.png"));
                super.mouseExited(e);
            }
        });
        cancelLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                close();
                super.mouseClicked(e);
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                cancelLabel.setIcon(IconUtil.getIcon(this, "/image/cancel_active.png"));
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                cancelLabel.setIcon(IconUtil.getIcon(this, "/image/cancel.png"));
                super.mouseExited(e);
            }
        });

        controlDialog.add(okLabel);
        controlDialog.add(cancelLabel);
        controlDialog.add(downloadLabel);
    }

    @Override
    public void paint(Graphics g)
    {
        RescaleOp ro = new RescaleOp(0.6f, 0, null);
        tempImage = ro.filter(image, null);
        g.drawImage(tempImage, 0, 0, this);

        if (!isShown)
        {
            setOpacity(1);
            isShown = true;
        }
    }

    //保存图像到文件
    public void saveImage() throws IOException
    {
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("保存");

        //文件过滤器，用户过滤可选择的文件
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG", "png");
        jfc.setFileFilter(filter);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyymmddHHmmss");
        String filename = "wechat_screen_shot_" + sdf.format(new Date());

        File filePath = FileSystemView.getFileSystemView().getHomeDirectory();
        File defaultFile = new File(filePath + File.separator + filename + ".png");
        jfc.setSelectedFile(defaultFile);

        int flag = jfc.showSaveDialog(this);
        if (flag == JFileChooser.APPROVE_OPTION)
        {
            File file = jfc.getSelectedFile();
            String path = file.getPath();
            if (!(path.endsWith(".png") || path.endsWith("PNG")))
            {
                path += ".png";
            }
            //写入文件
            ImageIO.write(saveImage, "png", new File(path));
        }
    }


    private void close()
    {
        controlDialog.setVisible(false);
        setVisible(false);
        dispose();
    }

}

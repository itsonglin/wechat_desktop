package com.rc.app;


import com.rc.components.message.JIMSendTextPane;
import com.rc.forms.ImageViewerFrame;
import com.rc.frames.ScreenShot;
import com.rc.panels.EmojiPanel;
import com.rc.utils.AvatarUtil;
import com.rc.utils.EmojiUtil;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.vdurmont.emoji.EmojiParser;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by song on 14/06/2017.
 */

class Test
{


    /**
     * @param args
     */
    public static void main(String[] args) throws IOException
    {
        try{
            ScreenShot ssw=new ScreenShot();
            ssw.setVisible(true);
        }catch(AWTException e){
            e.printStackTrace();
        }
    }

    /**
     * 全屏窗口，无标题栏。
     */
    public static void fullWindow1() throws IOException
    {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Robot robot = null;
        try
        {
            robot = new Robot();
        }
        catch (AWTException e)
        {
            e.printStackTrace();
        }
        BufferedImage bufferedImage = robot.createScreenCapture(new Rectangle(d.width, d.height));
        JLabel label = new JLabel();
        label.setIcon(new ImageIcon(bufferedImage));
        ImageIO.write(bufferedImage, "jpg", new File("/Users/song/" + System.currentTimeMillis() + ".jpg"));


        final JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.getGraphicsConfiguration().getDevice().setFullScreenWindow(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(label);
        frame.addMouseListener(new MouseAdapter()
        {
            // 双击退出
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2)
                {
                    frame.dispose();
                }
            }
        });
        frame.setVisible(true);

    }


        /*Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        Robot robot = null;
        try
        {
            robot = new Robot();
        }
        catch (AWTException e)
        {
            e.printStackTrace();
        }
        BufferedImage bufferedImage = robot.createScreenCapture(new Rectangle(d.width, d.height));
        ImageIO.write(bufferedImage, "jpg", new File("/Users/song/" + System.currentTimeMillis() + ".jpg"));
        System.out.println("ok");*/

        /*File file = new File("F:\\emoji");
        File[] imgs = file.listFiles();
        for (File img : imgs)
        {
            BufferedImage bufferedImage = ImageIO.read(img);
            Image scaledImage = bufferedImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);

            BufferedImage outImage = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);

            // 获取Graphics2D
            Graphics2D g2d = outImage.createGraphics();

            g2d.drawImage(scaledImage, 0, 0, null);

            ImageIO.write(outImage, "png", new File("F:\\emoji2\\" + img.getName()));
        }*/
}


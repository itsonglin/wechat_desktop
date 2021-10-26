package com.rc.app;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class DraggedTest extends JFrame {

    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DraggedTest frame = new DraggedTest();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public DraggedTest() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(660, 500);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        add(contentPane);

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createLineBorder(Color.orange, 2));
        panel.setLayout(null);

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(Color.GRAY);
        panel_1.setBounds(82, 85, 130, 130);
        panel.add(panel_1);

        JPanel panel_2 = new JPanel();
        panel_2.setBackground(Color.LIGHT_GRAY);
        panel_2.setBounds(261, 85, 130, 130);
        panel.add(panel_2);

        JPanel panel_3 = new JPanel();
        panel_3.setBackground(Color.MAGENTA);
        panel_3.setBounds(450, 85, 130, 130);
        panel.add(panel_3);

        JPanel panel_4 = new JPanel();
        panel_4.setBackground(Color.ORANGE);
        panel_4.setBounds(261, 285, 130, 130);
        panel.add(panel_4);

        MyListener m = new MyListener();
        panel_1.addMouseListener(m);
        panel_1.addMouseMotionListener(m);

        panel_2.addMouseListener(m);
        panel_2.addMouseMotionListener(m);

        panel_3.addMouseListener(m);
        panel_3.addMouseMotionListener(m);

        panel_4.addMouseListener(m);
        panel_4.addMouseMotionListener(m);
    }
    // 写一个类继承鼠标监听器的适配器，这样就可以免掉不用的方法。
    class MyListener extends MouseAdapter{
        //这两组x和y为鼠标点下时在屏幕的位置和拖动时所在的位置
        int newX,newY,oldX,oldY;
        //这两个坐标为组件当前的坐标
        int startX,startY;

        @Override
        public void mousePressed(MouseEvent e) {
            //此为得到事件源组件
            Component cp = (Component)e.getSource();
            //当鼠标点下的时候记录组件当前的坐标与鼠标当前在屏幕的位置
            startX = cp.getX();
            startY = cp.getY();
            oldX = e.getXOnScreen();
            oldY = e.getYOnScreen();
        }
        @Override
        public void mouseDragged(MouseEvent e) {
            Component cp = (Component)e.getSource();
            //拖动的时候记录新坐标
            newX = e.getXOnScreen();
            newY = e.getYOnScreen();
            //设置bounds,将点下时记录的组件开始坐标与鼠标拖动的距离相加
            cp.setBounds(startX+(newX - oldX), startY+(newY - oldY), cp.getWidth(), cp.getHeight());
        }

    }
}
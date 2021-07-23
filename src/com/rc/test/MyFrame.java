package com.rc.test;

/**
 * @author song
 * @date 19-9-25 13:42
 * @description
 * @since
 */
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.event.MouseInputListener;

/**
 * 自定义程序窗口，鼠标可拖拽移动其位置。
 * @author Jeby Sun
 *
 */
public class MyFrame extends JWindow {

    private static final long serialVersionUID = 1L;

    JLabel titleLbl;

    public MyFrame() {
        //设置背景颜色不能直接调用其setBackground方法，而要设置其ContentPane的背景颜色。
        this.getContentPane().setBackground(new Color(0x1E1E1E));
        this.setBounds(100,100,600,400);
        this.setLayout(null);

        titleLbl = new JLabel("  自定义窗口标题栏");
        titleLbl.setOpaque(true);
        titleLbl.setBackground(new Color(0x1E1E1E));
        titleLbl.setBounds(0, 0, 600, 30);
        this.add(titleLbl);
        //鼠标事件处理类
        MouseEventListener mouseListener = new MouseEventListener(this);
        titleLbl.addMouseListener(mouseListener);
        titleLbl.addMouseMotionListener(mouseListener);

        this.setVisible(true);
    }

    /**
     * 鼠标事件处理
     * @author Jeby Sun
     *
     */
    class MouseEventListener implements MouseInputListener {

        Point origin;
        //鼠标拖拽想要移动的目标组件
        MyFrame frame;

        public MouseEventListener(MyFrame frame) {
            this.frame = frame;
            origin = new Point();
        }

        @Override
        public void mouseClicked(MouseEvent e) {}

        /**
         * 记录鼠标按下时的点
         */
        @Override
        public void mousePressed(MouseEvent e) {
            origin.x = e.getX();
            origin.y = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {}

        /**
         * 鼠标移进标题栏时，设置鼠标图标为移动图标
         */
        @Override
        public void mouseEntered(MouseEvent e) {
            this.frame.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        }

        /**
         * 鼠标移出标题栏时，设置鼠标图标为默认指针
         */
        @Override
        public void mouseExited(MouseEvent e) {
            this.frame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }

        /**
         * 鼠标在标题栏拖拽时，设置窗口的坐标位置
         * 窗口新的坐标位置  = 移动前坐标位置+（鼠标指针当前坐标-鼠标按下时指针的位置）
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            Point p = this.frame.getLocation();
            this.frame.setLocation(
                    p.x + (e.getX() - origin.x),
                    p.y + (e.getY() - origin.y));
        }

        @Override
        public void mouseMoved(MouseEvent e) {}

    }

    public static void main(String[] args) {
        new MyFrame();
    }

}
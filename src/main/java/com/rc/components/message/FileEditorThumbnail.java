package com.rc.components.message;

import com.rc.res.Colors;
import com.rc.components.GBC;
import com.rc.helper.AttachmentIconHelper;
import com.rc.utils.FontUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

/**
 * 文件在输入框中的缩略图，当文件直接被粘贴到输入框时，该文件将会以缩略图的形式显示在输入框中
 * Created by song on 29/06/2017.
 */
public class FileEditorThumbnail extends JPanel
{
    private JLabel icon;
    private JLabel text;

    private String path;
    private AttachmentIconHelper attachmentIconHelper = new AttachmentIconHelper();

    public FileEditorThumbnail(String path)
    {
        this.path = path;

        initComponents();
        initView();

        setListeners();
    }

    private void initComponents()
    {
        setPreferredSize(new Dimension(100, 80));
        setMaximumSize(new Dimension(100, 80));
        setBackground(Colors.FONT_WHITE);
        setBorder(new LineBorder(Colors.LIGHT_GRAY));

        icon = new JLabel();
        icon.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon imageIcon = attachmentIconHelper.getImageIcon(path, 35, 35);
        icon.setIcon(imageIcon);


        text = new JLabel();
        text.setFont(FontUtil.getDefaultFont(12));
        text.setText(path.substring(path.lastIndexOf(File.separator) + 1));
        text.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void initView()
    {
        setLayout(new GridBagLayout());
        add(icon, new GBC(0, 0).setFill(GBC.BOTH).setInsets(5).setWeight(1, 1));
        add(text, new GBC(0, 1).setFill(GBC.BOTH).setInsets(5).setWeight(1, 1));
    }

    private void setListeners()
    {
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                // 双击打开文件
                if (e.getButton() == MouseEvent.BUTTON1 &&  e.getClickCount() == 2)
                {
                    openFileWithDefaultApplication(path);
                }
                super.mouseClicked(e);
            }
        });
    }


    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    /**
     * 使用默认程序打开文件
     *
     * @param path
     */
    private void openFileWithDefaultApplication(String path)
    {
        try
        {
            Desktop.getDesktop().open(new File(path));
        } catch (IOException e1)
        {
            JOptionPane.showMessageDialog(null, "文件打开失败，没有找到关联的应用程序", "打开失败", JOptionPane.ERROR_MESSAGE);
            e1.printStackTrace();
        } catch (IllegalArgumentException e2)
        {
            JOptionPane.showMessageDialog(null, "文件不存在，可能已被删除", "打开失败", JOptionPane.ERROR_MESSAGE);
        }
    }
}

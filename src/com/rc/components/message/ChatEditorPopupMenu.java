package com.rc.components.message;

import com.rc.components.Colors;
import com.rc.components.RCMenuItemUI;
import com.rc.components.RCTextEditor;
import com.rc.panels.ChatPanel;
import com.rc.utils.ClipboardUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

/**
 * Created by song on 2017/6/5.
 */
public class ChatEditorPopupMenu extends JPopupMenu
{
    JMenuItem selectAllItem = new JMenuItem("全选");
    JMenuItem copyItem = new JMenuItem("复制");
    JMenuItem cutItem = new JMenuItem("剪切");
    JMenuItem pasteItem = new JMenuItem("粘贴");
    JMenuItem deleteItem = new JMenuItem("删除");

    public ChatEditorPopupMenu()
    {
        initMenuItem();
    }

    private void initMenuItem()
    {
        selectAllItem.setUI(new RCMenuItemUI(80, 25));
        selectAllItem.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JTextPane textPane = (JTextPane) getInvoker();
                textPane.selectAll();
            }
        });


        copyItem.setUI(new RCMenuItemUI(80, 25));
        copyItem.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JTextPane textPane = (JTextPane) getInvoker();

                String text = textPane.getSelectedText();
                if (text != null)
                {
                    ClipboardUtil.copyString(text);
                }
            }
        });


        cutItem.setUI(new RCMenuItemUI(80, 25));
        cutItem.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JTextPane textPane = (JTextPane) getInvoker();
                String text = textPane.getSelectedText();
                if (text != null)
                {
                    ClipboardUtil.copyString(text);
                    textPane.replaceSelection("");
                }
            }
        });

        pasteItem.setUI(new RCMenuItemUI(80, 25));
        pasteItem.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                RCTextEditor textEditor = (RCTextEditor) getInvoker();
                textEditor.paste();
                //ChatPanel.getContext().paste();
            }
        });

        deleteItem.setUI(new RCMenuItemUI(80, 25));
        deleteItem.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JTextPane textPane = (JTextPane) getInvoker();
                String text = textPane.getSelectedText();
                if (text != null)
                {
                    textPane.replaceSelection("");
                }


                //textPane.insertComponent(new FileEditorThumbnail("/Users/song/使用说明.doc"));

            }
        });

        this.add(selectAllItem);
        this.add(copyItem);
        this.add(cutItem);
        this.add(pasteItem);
        this.add(deleteItem);

        setBorder(new LineBorder(Colors.SCROLL_BAR_TRACK_LIGHT));
        setBackground(Colors.FONT_WHITE);
    }

    @Override
    public void show(Component invoker, int x, int y)
    {
        RCTextEditor editor = (RCTextEditor) invoker;
        if (editor.getSelectedText() == null)
        {
            copyItem.setEnabled(false);
            cutItem.setEnabled(false);
            deleteItem.setEnabled(false);
        }
        else
        {
            copyItem.setEnabled(true);
            cutItem.setEnabled(true);
            deleteItem.setEnabled(true);
        }
        super.show(invoker, x, y);
    }
}

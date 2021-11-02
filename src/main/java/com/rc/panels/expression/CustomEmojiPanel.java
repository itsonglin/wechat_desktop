package com.rc.panels.expression;

import com.rc.res.Colors;
import com.rc.components.ScrollUI;
import com.rc.listener.ExpressionListener;
import com.rc.panels.ExpressionItem;
import com.rc.utils.EmojiUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by song on 04/07/2017.
 * 萌二表情选择列表
 */
public class CustomEmojiPanel extends JPanel
{
    private static CustomEmojiPanel context;
    private ExpressionListener expressionListener;
    private JDialog parentPopup;
    private JScrollPane scrollPane;
    private JPanel contentPanel;

    private boolean inited = false;

    public CustomEmojiPanel()
    {
        context = this;
        initComponents();
    }

    public void initData()
    {
        MouseListener listener = new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                ExpressionItem panel = (ExpressionItem) e.getSource();
                ImageIcon icon = EmojiUtil.getCustomEmoji(panel.getDisplayName(), 50, 50, false);
                panel.setImage(icon);

                panel.setBackground(Colors.SCROLL_BAR_TRACK_LIGHT);
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                ExpressionItem panel = (ExpressionItem) e.getSource();
                panel.setBackground(Colors.WINDOW_BACKGROUND);

                ImageIcon icon = EmojiUtil.getCustomEmojiThumb(panel.getDisplayName(), 50);
                panel.setImage(icon);

                super.mouseExited(e);
            }

            @Override
            public void mouseClicked(MouseEvent e)
            {
                ExpressionItem panel = (ExpressionItem) e.getSource();
                panel.setBackground(Colors.WINDOW_BACKGROUND);

                if (expressionListener != null)
                {
                    expressionListener.onSelected(panel.getCode());
                    if (parentPopup != null)
                    {
                        parentPopup.setVisible(false);
                    }

                }
                super.mouseClicked(e);
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                super.mouseWheelMoved(e);
            }
        };


        contentPanel.removeAll();
        List<String> codeList = new ArrayList<>(EmojiUtil.getRegisteredCustomEmojis());
        for (int i = 0; i < codeList.size(); i++)
        {
            if (i >= codeList.size())
            {
                add(new JPanel());
                return;
            }

            ImageIcon icon = EmojiUtil.getCustomEmojiThumb(codeList.get(i), 50);
            JPanel panel = new ExpressionItem(codeList.get(i), icon, codeList.get(i), new Dimension(60, 60), new Dimension(50, 50));
            panel.addMouseListener(listener);

            contentPanel.add(panel);
        }

    }

    private void initComponents()
    {
        //setPreferredSize(new Dimension(400,300));

        contentPanel = new JPanel();
//        contentPanel.setLayout(new GridLayout(10, 1, 3, 0));
        contentPanel.setLayout(new FlowLayout());
        contentPanel.setPreferredSize(new Dimension(400, 300));

        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUI(new ScrollUI(Colors.SCROLL_BAR_THUMB, Colors.WINDOW_BACKGROUND));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        this.add(scrollPane);

    }

    private void initView()
    {

    }

    public void setExpressionListener(ExpressionListener expressionListener, JDialog parentPopup)
    {
        this.expressionListener = expressionListener;
        this.parentPopup = parentPopup;
    }


    public void notifyDataChange()
    {
        initView();
        initData();
        this.inited = true;
    }

    public boolean isInited()
    {
        return inited;
    }

    public static CustomEmojiPanel getContext()
    {
        return context;
    }
}

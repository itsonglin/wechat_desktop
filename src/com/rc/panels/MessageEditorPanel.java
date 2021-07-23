package com.rc.panels;

import com.rc.app.Launcher;
import com.rc.components.*;
import com.rc.components.message.ChatEditorPopupMenu;
import com.rc.frames.ScreenShotFrame;
import com.rc.helper.HotKeyHelper;
import com.rc.listener.ExpressionListener;
import com.rc.utils.FontUtil;
import com.rc.utils.IconUtil;
import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by song on 17-5-30.
 *
 * <P>下图 #MessageEditorPanel# 对应的位置</P>
 * <p>
 * 消息编辑区域
 *
 * <P>推荐使用Menlo或Consolas字体</P>
 * ┌────────────────────────┬────────────────────────────────────────────────────────┐
 * │ ┌─────┐                │  Room Title                                         ≡  │
 * │ │     │ name         ≡ ├────────────────────────────────────────────────────────┤
 * │ └─────┘                │                                                        │
 * ├────────────────────────┤                     message time                       │
 * │    search              │  ┌──┐ ┌────────────┐                                   │
 * ├────────────────────────┤  └──┘ │  message   │                                   │
 * │  ▆    │    ▆   │   ▆   │       └────────────┘                                   │
 * ├────────────────────────┤                                                        │
 * │ ┌──┐ name         14:01│                                                        │
 * │ └──┘ message        99+│                     message time                       │
 * ├────────────────────────┤                                    ┌────────────┐ ┌──┐ │
 * │                        │                                    │  message   │ └──┘ │
 * │                        │                                    └────────────┘      │
 * │                        │                                                        │
 * │          Room          │                                                        │
 * │                        ├────────────────────────────────────────────────────────┤
 * │                        │                                                        │
 * │          List          │                                                        │
 * │                        │                 #MessageEditorPanel#                   │
 * │                        │                                                        │
 * │                        │                                                        │
 * └────────────────────────┴────────────────────────────────────────────────────────┘
 */
public class MessageEditorPanel extends ParentAvailablePanel
{
    private JPanel controlPanel;
    private JLabel fileLabel;
    private JLabel expressionLabel;
    private JLabel cutLabel;
    private JScrollPane textScrollPane;
    private RCTextEditor textEditor;
    private JPanel sendPanel;
    private RCButton sendButton;
    private ChatEditorPopupMenu chatEditorPopupMenu;

    private ImageIcon fileNormalIcon;
    private ImageIcon fileActiveIcon;

    private ImageIcon emotionNormalIcon;
    private ImageIcon emotionActiveIcon;

    private ImageIcon cutNormalIcon;
    private ImageIcon cutActiveIcon;

    private ExpressionPopup expressionPopup;
    private ScreenShotFrame screenShotFrame;

    public MessageEditorPanel(JPanel parent)
    {
        super(parent);

        initComponents();
        initView();
        setListeners();

        registerHotKey();
    }

    private void registerHotKey()
    {
        HotKeyHelper.getInstance().register(Launcher.hotKeyMap.get(Launcher.HOT_KEY_SCREEN_SHOT), new HotKeyListener()
        {
            @Override
            public void onHotKey(HotKey hotKey)
            {
                screenShot();
            }
        }, "屏幕截图");
    }

    private void initComponents()
    {
        Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 7));

        fileLabel = new JLabel();
        fileNormalIcon = IconUtil.getIcon(this, "/image/file.png");
        fileActiveIcon = IconUtil.getIcon(this, "/image/file_active.png");
        fileLabel.setIcon(fileNormalIcon);
        fileLabel.setCursor(handCursor);
        fileLabel.setToolTipText("发送文件/图片");

        expressionLabel = new JLabel();
        emotionNormalIcon = IconUtil.getIcon(this, "/image/emotion.png");
        emotionActiveIcon = IconUtil.getIcon(this, "/image/emotion_active.png");
        expressionLabel.setIcon(emotionNormalIcon);
        expressionLabel.setCursor(handCursor);
        expressionLabel.setToolTipText("表情");

        cutLabel = new JLabel();
        cutNormalIcon = IconUtil.getIcon(this, "/image/cut.png");
        cutActiveIcon = IconUtil.getIcon(this, "/image/cut_active.png");
        cutLabel.setIcon(cutNormalIcon);
        cutLabel.setCursor(handCursor);
        cutLabel.setToolTipText("截图(Ctrl + Alt + A)");


        textEditor = new RCTextEditor();
        textEditor.setBackground(Colors.WINDOW_BACKGROUND);
        textEditor.setFont(FontUtil.getDefaultFont(14));
        textEditor.setMargin(new Insets(0, 15, 0, 0));
        textScrollPane = new JScrollPane(textEditor);
        textScrollPane.getVerticalScrollBar().setUI(new ScrollUI(Colors.SCROLL_BAR_THUMB, Colors.WINDOW_BACKGROUND));
        textScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        textScrollPane.setBorder(null);

        sendPanel = new JPanel();
        sendPanel.setLayout(new BorderLayout());

        sendButton = new RCButton("发 送");
        sendPanel.add(sendButton, BorderLayout.EAST);
        sendButton.setForeground(Colors.DARKER);
        sendButton.setFont(FontUtil.getDefaultFont(13));
        sendButton.setPreferredSize(new Dimension(75, 29));
        sendButton.setToolTipText("Enter发送消息，Ctrl+Enter换行");

        chatEditorPopupMenu = new ChatEditorPopupMenu();

        expressionPopup = new ExpressionPopup();

        screenShotFrame = new ScreenShotFrame();
    }

    private void initView()
    {
        this.setLayout(new GridBagLayout());

        controlPanel.add(expressionLabel);
        controlPanel.add(fileLabel);
        controlPanel.add(cutLabel);

        add(controlPanel, new GBC(0, 0).setFill(GBC.HORIZONTAL).setWeight(1, 50));
        add(textScrollPane, new GBC(0, 1).setFill(GBC.BOTH).setWeight(1, 750));
        add(sendPanel, new GBC(0, 2).setFill(GBC.BOTH).setWeight(1, 1).setInsets(0, 0, 10, 10));
    }

    private void setListeners()
    {
        fileLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                fileLabel.setIcon(fileActiveIcon);
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                fileLabel.setIcon(fileNormalIcon);
                super.mouseExited(e);
            }
        });

        expressionLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                expressionLabel.setIcon(emotionActiveIcon);
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                expressionLabel.setIcon(emotionNormalIcon);
                super.mouseExited(e);
            }

            @Override
            public void mouseClicked(MouseEvent e)
            {
                //expressionPopup.show((Component) e.getSource(), e.getX() - 200, e.getY() - 320);
                Point location  = e.getLocationOnScreen();
                int x = location.x + e.getX() - 200;
                int y = location.y + e.getY() - 330;
                expressionPopup.setLocation(x, y);
                expressionPopup.setVisible(true);
                super.mouseClicked(e);
            }
        });

        cutLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                cutLabel.setIcon(cutActiveIcon);
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                cutLabel.setIcon(cutNormalIcon);
                super.mouseExited(e);
            }

            @Override
            public void mouseClicked(MouseEvent e)
            {
                screenShot();
                super.mouseClicked(e);
            }
        });

        textEditor.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON3)
                {
                    chatEditorPopupMenu.show((Component) e.getSource(), e.getX(), e.getY());
                }
                super.mouseClicked(e);
            }
        });

        textEditor.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
                textEditor.setBackground(Colors.WINDOW_BACKGROUND_LIGHT);
                controlPanel.setBackground(Colors.WINDOW_BACKGROUND_LIGHT);
                sendPanel.setBackground(Colors.WINDOW_BACKGROUND_LIGHT);
                MessageEditorPanel.this.setBackground(Colors.WINDOW_BACKGROUND_LIGHT);
                super.focusGained(e);
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                textEditor.setBackground(Colors.WINDOW_BACKGROUND);
                controlPanel.setBackground(Colors.WINDOW_BACKGROUND);
                sendPanel.setBackground(Colors.WINDOW_BACKGROUND);
                MessageEditorPanel.this.setBackground(Colors.WINDOW_BACKGROUND);
                super.focusLost(e);
            }

            @Override
            public int hashCode()
            {
                return super.hashCode();
            }

            @Override
            public boolean equals(Object obj)
            {
                return super.equals(obj);
            }

            @Override
            protected Object clone() throws CloneNotSupportedException
            {
                return super.clone();
            }

            @Override
            public String toString()
            {
                return super.toString();
            }

            @Override
            protected void finalize() throws Throwable
            {
                super.finalize();
            }
        });
    }

    private void screenShot()
    {
        if (!ScreenShotFrame.visible)
        {
            screenShotFrame = new ScreenShotFrame();
            screenShotFrame.setVisible(true);
        }
    }

    public void setExpressionListener(ExpressionListener listener)
    {
        expressionPopup.setExpressionListener(listener);
    }

    public RCTextEditor getEditor()
    {
        return textEditor;
    }

    public JButton getSendButton()
    {
        return sendButton;
    }

    public JLabel getUploadFileLabel()
    {
        return fileLabel;
    }


}

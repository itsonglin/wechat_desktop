package com.rc.panels;

import com.rc.components.Colors;
import com.rc.components.GBC;
import com.rc.components.RCBorder;
import com.rc.listener.ExpressionListener;
import com.rc.utils.IconUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Created by song on 04/07/2017.
 */
public class ExpressionPopup extends JPopupMenu
{
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private JPanel listPanel;
    private JPanel tabIconPanel;

    private JPanel emojiTabPanel;
    private JLabel emojiTabLabel;
    private EmojiPanel emojiPanel;


    private CardLayout cardLayout;
    public static final String EMOJI = "EMOJI";


    public ExpressionPopup()
    {
        initComponents();
        initView();

        selectTab(emojiTabPanel);
    }

    private void initComponents()
    {
        listPanel = new JPanel();
        listPanel.setBorder(new RCBorder(RCBorder.BOTTOM, Colors.LIGHT_GRAY));
        cardLayout = new CardLayout();
        listPanel.setLayout(cardLayout);

        tabIconPanel = new JPanel();
        tabIconPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5,0));

        emojiTabPanel = new JPanel();
        emojiTabLabel = new JLabel();
        emojiTabLabel.setIcon(IconUtil.getIcon(this, "/image/smile.png", 23, 23));
        emojiPanel = new EmojiPanel();

        setBackground(Colors.WINDOW_BACKGROUND);
        this.setPopupSize(WIDTH, HEIGHT);
    }

    private void initView()
    {
        emojiTabPanel.add(emojiTabLabel);

        tabIconPanel.add(emojiTabPanel);

        listPanel.add(emojiPanel);


        setLayout(new GridBagLayout());
        add(listPanel, new GBC(0, 0).setWeight(1,1000).setFill(GBC.BOTH));
        add(tabIconPanel, new GBC(0, 1).setWeight(1,1).setFill(GBC.BOTH).setInsets(3,0,0,0));
    }

    public void setExpressionListener(ExpressionListener listener)
    {
        emojiPanel.setExpressionListener(listener, this);
    }


    private void selectTab(JPanel tab)
    {
        tab.setBackground(Colors.SCROLL_BAR_TRACK_LIGHT);
    }


    public void showPanel(String who)
    {
        cardLayout.show(listPanel, who);
    }

}

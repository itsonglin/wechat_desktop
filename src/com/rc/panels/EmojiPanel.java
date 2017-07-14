package com.rc.panels;

import com.rc.components.Colors;
import com.rc.listener.ExpressionListener;
import com.rc.utils.IconUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by song on 04/07/2017.
 */
public class EmojiPanel extends JPanel
{
    private ExpressionListener expressionListener;
    private JPopupMenu parentPopup;
    public EmojiPanel()
    {
        initComponents();
        initView();
        initData();

    }

    private void initData()
    {
        MouseListener listener = new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                JPanel panel = (JPanel) e.getSource();
                panel.setBackground(Colors.SCROLL_BAR_TRACK_LIGHT);
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                JPanel panel = (JPanel) e.getSource();
                panel.setBackground(Colors.WINDOW_BACKGROUND);
                super.mouseExited(e);
            }

            @Override
            public void mouseClicked(MouseEvent e)
            {
                ExpressionItem panel = (ExpressionItem) e.getSource();
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
        };

        String[] codeList = new String[]{
                ":smile:",":blush:",":confused:",":anguished:",":cold_sweat:",":astonished:",":cry:",":joy:",
                ":disappointed_relieved:",":disappointed:",":anguished:",":confounded:",":angry:",":dizzy_face:",":expressionless:",":fearful:",
                ":flushed:",":frowning:",":grin:",":heart_eyes:",":heart_eyes_cat:",":hushed:",":imp:",":innocent:",
                ":kissing_closed_eyes:",":kissing_heart:",":laughing:",":neutral_face:",":no_mouth:",":open_mouth:",":pensive:",":persevere:",
                ":rage:",":relaxed:",":relieved:",":scream:",":sleeping:",":broken_heart:",":smirk:",":sob:",
                ":stuck_out_tongue_closed_eyes:",":sunglasses:",":sweat_smile:",":sweat:",":triumph:",":unamused:",":wink:",":yum:",
                ":cat:",":dog:",":bear:",":chicken:",":cow:",":ghost:",":hear_no_evil:",":koala:",
                ":mouse:",":airplane:",":ambulance:",":bike:",":bullettrain_side:",":bus:",":metro:",":oncoming_taxi:",
                ":walking:",":apple:",":banana:",":beer:",":birthday:",":cake:",":cherries:",":tada:",
                ":clap:",":fist:",":ok_hand:",":pray:",":thumbsup:",":thumbsdown:",":muscle:",":v:"
        };

        String iconPath = "/emoji/";
        for (int i = 0; i < 80; i++)
        {
            String name = codeList[i].substring(1, codeList[i].length() - 1);
            JPanel panel = new ExpressionItem(codeList[i], IconUtil.getIcon(this,
                    iconPath +  name + ".png"), name);
            panel.addMouseListener(listener);

            add(panel);
        }

    }

    private void initComponents()
    {
        //setPreferredSize(new Dimension(400,300));
        this.setLayout(new GridLayout(8,10, 3,0));

    }

    private void initView()
    {

    }

    public void setExpressionListener(ExpressionListener expressionListener, JPopupMenu parentPopup)
    {
        this.expressionListener = expressionListener;
        this.parentPopup = parentPopup;
    }
}

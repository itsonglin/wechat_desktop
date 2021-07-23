package com.rc.components.message;

import com.rc.adapter.ViewHolder;
import com.rc.utils.AvatarUtil;
import com.rc.utils.EmojiUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * 候选表情弹出层
 * Created by song on 21/06/2017.
 */
public class CandidateEmojiPopup extends JPopupMenu
{
    private List<String> emojis;
    private EmojiSelectedCallBack selectedCallBack;
    private String prefix;

    public CandidateEmojiPopup()
    {
    }

    public void show(Component invoker, int x, int y, String prefix)
    {
        emojis = findEmojisByPrefix(prefix);
        this.removeAll();
        this.initComponents();
        this.revalidate();
        this.prefix = prefix;
        super.show(invoker, x, y);
    }

    private List<String> findEmojisByPrefix(String prefix)
    {
        return EmojiUtil.findEmojisByPrefix(prefix);
    }


    private void initComponents()
    {
        if (this.emojis != null)
        {
            this.setAutoscrolls(true);

            JMenuItem item = null;
            for (String emoji : emojis)
            {
                item = new JMenuItem(emoji);
                item.setUI(new RCRemindUserMenuItemUI(120, 25));

                ImageIcon icon = EmojiUtil.getCustomEmojiThumb(emoji, 18);
                if (icon == null)
                {
                    continue;
                }
                item.setIcon(icon);
                item.setIconTextGap(-2);

                item.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        if (selectedCallBack != null)
                        {
                            selectedCallBack.onSelected(((JMenuItem) e.getSource()).getText());
                        }
                    }
                });
                add(item);
            }
        }
    }

    public void setSelectedCallBack(EmojiSelectedCallBack selectedCallBack)
    {
        this.selectedCallBack = selectedCallBack;
    }

    public interface EmojiSelectedCallBack
    {
        void onSelected(String username);
    }
}


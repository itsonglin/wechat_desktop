package com.rc.adapter;

import com.rc.components.Colors;
import com.rc.components.RCBorder;
import com.rc.entity.ContactsItem;
import com.rc.panels.RightPanel;
import com.rc.listener.AbstractMouseListener;
import com.rc.utils.AvatarUtil;
import com.rc.utils.CharacterParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Created by song on 17-5-30.
 */
public class ContactsItemsAdapter extends BaseAdapter<ContactsItemViewHolder>
{
    private List<ContactsItem> contactsItems;
    private List<ContactsItemViewHolder> viewHolders = new ArrayList<>();
    Map<Integer, String> positionMap = new HashMap<>();
    private ContactsItemViewHolder selectedViewHolder;

    public ContactsItemsAdapter(List<ContactsItem> contactsItems)
    {
        this.contactsItems = contactsItems;

        if (contactsItems != null)
        {
            processData();
        }
    }

    @Override
    public int getCount()
    {
        return contactsItems.size();
    }

    @Override
    public ContactsItemViewHolder onCreateViewHolder(int viewType)
    {
        return new ContactsItemViewHolder();
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(int viewType, int position)
    {
        for (int pos : positionMap.keySet())
        {
            if (pos == position)
            {
                String ch = positionMap.get(pos);

                return new ContactsHeaderViewHolder(ch.toUpperCase());
            }
        }

        return null;
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder viewHolder, int position)
    {
        ContactsHeaderViewHolder holder = (ContactsHeaderViewHolder) viewHolder;
        holder.setPreferredSize(new Dimension(100, 25));
        holder.setBackground(Colors.DARKER);
        holder.setBorder(new RCBorder(RCBorder.BOTTOM));
        holder.setOpaque(true);

        holder.letterLabel = new JLabel();
        holder.letterLabel.setText(holder.getLetter());
        holder.letterLabel.setForeground(Colors.FONT_GRAY);

        holder.setLayout(new BorderLayout());
        holder.add(holder.letterLabel, BorderLayout.WEST);
    }

    @Override
    public void onBindViewHolder(ContactsItemViewHolder viewHolder, int position)
    {
        viewHolders.add(position, viewHolder);
        ContactsItem item = contactsItems.get(position);

        ImageIcon icon = new ImageIcon();
        icon.setImage(AvatarUtil.createOrLoadUserAvatar(item.getName())
                .getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        viewHolder.avatar.setIcon(icon);

        viewHolder.roomName.setText(item.getName());

        viewHolder.addMouseListener(new AbstractMouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                RightPanel.getContext().getUserInfoPanel().setUsername(item.getName());
                RightPanel.getContext().showPanel(RightPanel.USER_INFO);

                setBackground(viewHolder, Colors.ITEM_SELECTED);
                selectedViewHolder = viewHolder;

                for (ContactsItemViewHolder holder : viewHolders)
                {
                    if (holder != viewHolder)
                    {
                        setBackground(holder, Colors.DARK);
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                if (selectedViewHolder != viewHolder)
                {
                    setBackground(viewHolder, Colors.ITEM_SELECTED_DARK);
                }
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                if (selectedViewHolder != viewHolder)
                {
                    setBackground(viewHolder, Colors.DARK);
                }
            }
        });
    }

    private void setBackground(ContactsItemViewHolder holder, Color color)
    {
        holder.setBackground(color);
    }

    public void processData()
    {
        Collections.sort(contactsItems);

        int index = 0;
        String lastChara = "";
        for (ContactsItem item : contactsItems)
        {
            String ch = CharacterParser.getSelling(item.getName()).substring(0, 1).toUpperCase();
            if (!ch.equals(lastChara))
            {
                lastChara = ch;
                positionMap.put(index, ch);
            }

            index++;
        }
    }
}

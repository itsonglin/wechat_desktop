package com.rc.adapter;

import com.rc.entity.ContactsItem;
import com.rc.entity.SelectUserData;
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
public class SelectedUserItemsAdapter extends BaseAdapter<SelectedUserItemViewHolder>
{
    private List<SelectUserData> userList;
    Map<Integer, String> positionMap = new HashMap<>();
    private ItemRemoveListener itemRemoveListener;

    public SelectedUserItemsAdapter(List<SelectUserData> userList)
    {
        this.userList = userList;

        if (userList != null)
        {
            processData();
        }
    }

    @Override
    public int getCount()
    {
        return userList.size();
    }

    @Override
    public SelectedUserItemViewHolder onCreateViewHolder(int viewType)
    {
        return new SelectedUserItemViewHolder();
    }

    @Override
    public void onBindViewHolder(SelectedUserItemViewHolder viewHolder, int position)
    {

        SelectUserData user = userList.get(position);

        // 头像
        ImageIcon imageIcon = new ImageIcon(AvatarUtil.createOrLoadUserAvatar(user.getName()).getScaledInstance(30,30,Image.SCALE_SMOOTH));
        viewHolder.avatar.setIcon(imageIcon);

        // 名字
        viewHolder.username.setText(user.getName());

        /*viewHolder.icon.setIcon(IconUtil.getIcon(this, "/image/remove.png", 18, 18));
        viewHolder.icon.setToolTipText("移除");*/

        viewHolder.icon.addMouseListener(new AbstractMouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (itemRemoveListener != null)
                {
                    itemRemoveListener.onRemove(viewHolder.username.getText());
                }
                super.mouseClicked(e);
            }
        });
    }


    private void processData()
    {
        //Collections.sort(userList);
        Collections.sort(userList, new Comparator<SelectUserData>()
        {
            @Override
            public int compare(SelectUserData o1, SelectUserData o2)
            {
                String tc = CharacterParser.getSelling(o1.getName().toUpperCase());
                String oc = CharacterParser.getSelling(o2.getName().toUpperCase());
                return tc.compareTo(oc);
            }
        });

        int index = 0;
        String lastChara = "";
        for (SelectUserData user : userList)
        {
            String ch = CharacterParser.getSelling(user.getName()).substring(0, 1).toUpperCase();
            if (!ch.equals(lastChara))
            {
                lastChara = ch;
                positionMap.put(index, ch);
            }

            index++;
        }
    }

    public void setItemRemoveListener(ItemRemoveListener itemRemoveListener)
    {
        this.itemRemoveListener = itemRemoveListener;
    }


    public interface ItemRemoveListener
    {
        void onRemove(String username);
    }

}

package com.rc.panels;

import com.rc.adapter.SelectUserItemViewHolder;
import com.rc.adapter.SelectUserItemsAdapter;
import com.rc.adapter.SelectedUserItemsAdapter;
import com.rc.components.*;
import com.rc.entity.SelectUserData;
import com.rc.listener.AbstractMouseListener;
import com.rc.utils.IconUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by song on 19/06/2017.
 */
public class SelectUserPanel extends JPanel
{
    private JPanel leftPanel;
    private JPanel rightPanel;
    private RCListView selectUserListView;
    private RCListView selectedUserListView;

    /*private JPanel buttonPanel;
    private JButton cancelButton;
    private JButton okButton;*/

    private int width;
    private int height;

    private List<SelectUserData> leftUserList;
    private List<SelectUserData> selectedUserList = new ArrayList<>();
    private SelectUserItemsAdapter selectUserItemsAdapter;
    private SelectedUserItemsAdapter selectedUserItemsAdapter;
    private ImageIcon checkIcon;
    private ImageIcon uncheckIcon;


    public SelectUserPanel(int width, int height, List<SelectUserData> leftUserList)
    {
        this.width = width;
        this.height = height;
        this.leftUserList = leftUserList;

        initComponents();
        initView();
    }


    private void initComponents()
    {
        checkIcon = IconUtil.getIcon(this, "/image/check.png");
        uncheckIcon = IconUtil.getIcon(this, "/image/uncheck.png");

        leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(width / 2 - 1, height - 10));
        leftPanel.setBorder(new RCBorder(RCBorder.RIGHT, Colors.LIGHT_GRAY));

        rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(width / 2 - 1, height - 10));


        // 选择用户列表
        selectUserListView = new RCListView();

        selectUserItemsAdapter = new SelectUserItemsAdapter(leftUserList);
        selectUserItemsAdapter.setMouseListener(new AbstractMouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                SelectUserItemViewHolder holder = (SelectUserItemViewHolder) e.getSource();

                String username = holder.username.getText();
                if (unSelectUser(username))
                {
                    holder.icon.setIcon(uncheckIcon);
                }
                else
                {
                    selectUser(username);
                    holder.icon.setIcon(checkIcon);
                }


            }
        });
        selectUserListView.setScrollBarColor(Colors.SCROLL_BAR_THUMB, Colors.WINDOW_BACKGROUND);
        selectUserListView.setAdapter(selectUserItemsAdapter);

        // 已选中用户列表
        selectedUserListView = new RCListView();
        selectedUserItemsAdapter = new SelectedUserItemsAdapter(selectedUserList);
        selectedUserItemsAdapter.setItemRemoveListener(new SelectedUserItemsAdapter.ItemRemoveListener()
        {
            @Override
            public void onRemove(String username)
            {
                if (unSelectUser(username))
                {
                    for (Component holder : selectUserListView.getItems())
                    {
                        SelectUserItemViewHolder viewHolder = (SelectUserItemViewHolder) holder;
                        if (viewHolder.username.getText().equals(username))
                        {
                            viewHolder.icon.setIcon(uncheckIcon);
                            break;
                        }
                    }
                }
            }
        });
        selectedUserListView.setScrollBarColor(Colors.SCROLL_BAR_THUMB, Colors.WINDOW_BACKGROUND);
        selectedUserListView.setAdapter(selectedUserItemsAdapter);
    }

    private void initView()
    {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));
        panel.add(leftPanel);
        panel.add(rightPanel);
        add(panel);


        leftPanel.setLayout(new GridBagLayout());
        leftPanel.add(selectUserListView, new GBC(0, 0).setFill(GBC.BOTH).setWeight(1, 1).setInsets(0, 0, 5, 0));

        rightPanel.setLayout(new GridBagLayout());
        rightPanel.add(selectedUserListView, new GBC(0, 0).setFill(GBC.BOTH).setWeight(1, 1));
    }

    /**
     * 选择一位用户
     *
     * @param username
     */
    private void selectUser(String username)
    {
        for (SelectUserData item  : leftUserList)
        {
            if (item.getName().equals(username))
            {
                selectedUserList.add(item);
                selectedUserListView.notifyDataSetChanged(false);
                break;
            }
        }
    }

    private boolean unSelectUser(String username)
    {
        Iterator<SelectUserData> itemIterator = selectedUserList.iterator();
        boolean dataChanged = false;
        while (itemIterator.hasNext())
        {
            SelectUserData user = itemIterator.next();
            if (user.getName().equals(username))
            {
                dataChanged = true;
                itemIterator.remove();
                break;
            }
        }

        if (dataChanged)
        {
            selectedUserListView.notifyDataSetChanged(false);
        }

        return dataChanged;
    }

    public List<SelectUserData> getSelectedUser()
    {
        return selectedUserList;
    }

    public void notifyDataSetChanged(List<SelectUserData> users)
    {
        leftUserList = users;
        selectUserItemsAdapter.setUserList(leftUserList);
        selectUserListView.notifyDataSetChanged(false);
    }
}

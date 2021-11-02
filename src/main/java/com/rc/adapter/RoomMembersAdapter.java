package com.rc.adapter;

import com.rc.app.Launcher;
import com.rc.res.Colors;
import com.rc.db.model.CurrentUser;
import com.rc.db.service.ContactsUserService;
import com.rc.db.service.CurrentUserService;
import com.rc.components.UserInfoPopup;
import com.rc.listener.AbstractMouseListener;
import com.rc.utils.AvatarUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by song on 07/06/2017.
 */
public class RoomMembersAdapter extends BaseAdapter<RoomMembersItemViewHolder>
{
    private List<String> members;
    private List<RoomMembersItemViewHolder> viewHolders = new ArrayList<>();
    private CurrentUser currentUser;
    private CurrentUserService currentUserService = Launcher.currentUserService;
    private ContactsUserService contactsUserService = Launcher.contactsUserService;
    private MouseAdapter addMemberButtonMouseListener;
    private MouseAdapter removeMemberButtonMouseListener;


    private static final Map<String, RoomMembersItemViewHolder> ROOM_MEMBERS_VIEW_HOLDER_CAHCE= new HashMap<>(64);


    public RoomMembersAdapter(List<String> members)
    {
        this.members = members;
        List<CurrentUser> users = currentUserService.findAll();
        currentUser = users.size() < 1 ? Launcher.currentUser : users.get(0);
    }

    @Override
    public RoomMembersItemViewHolder onCreateViewHolder(int viewType, int position)
    {
        String name = members.get(position);
        RoomMembersItemViewHolder holder = ROOM_MEMBERS_VIEW_HOLDER_CAHCE.get(name);
        if (holder == null)
        {
            holder = new RoomMembersItemViewHolder();
            holder.fromCache = false;
            ROOM_MEMBERS_VIEW_HOLDER_CAHCE.put(name, holder);
            System.out.println("缓存" + name + " RoomMembersItemViewHolder");
        }
        else
        {
            holder.fromCache = true;
        }


        return holder;
    }

    @Override
    public void onBindViewHolder(RoomMembersItemViewHolder viewHolder, int position)
    {
        String name = members.get(position);
        if (viewHolder.fromCache)
        {
            return;
        }

        if (!viewHolders.contains(viewHolder))
        {
            viewHolders.add(viewHolder);
        }

        viewHolder.roomName.setText(name);

        if (name.equals("添加成员"))
        {
            viewHolder.setCursor(new Cursor(Cursor.HAND_CURSOR));

            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/image/add_member.png"));
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
            viewHolder.avatar.setIcon(imageIcon);

            viewHolder.addMouseListener(new AbstractMouseListener()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    //System.out.println("添加/刪除用戶");
                    //selectAndAddRoomMember();
                    if (addMemberButtonMouseListener != null)
                    {
                        addMemberButtonMouseListener.mouseClicked(e);
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e)
                {
                    viewHolder.setBackground(Colors.LIGHT_GRAY);
                    super.mouseEntered(e);
                }

                @Override
                public void mouseExited(MouseEvent e)
                {
                    viewHolder.setBackground(Colors.WINDOW_BACKGROUND_LIGHT);

                }
            });
        } else if (name.equals("删除成员"))
        {
            viewHolder.setCursor(new Cursor(Cursor.HAND_CURSOR));

            ImageIcon imageIcon = new ImageIcon(getClass().getResource("/image/delete_member.png"));
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
            viewHolder.avatar.setIcon(imageIcon);

            viewHolder.addMouseListener(new AbstractMouseListener()
            {
                @Override
                public void mouseEntered(MouseEvent e)
                {
                    viewHolder.setBackground(Colors.LIGHT_GRAY);
                    super.mouseEntered(e);
                }

                @Override
                public void mouseExited(MouseEvent e)
                {
                    viewHolder.setBackground(Colors.WINDOW_BACKGROUND_LIGHT);

                }

                @Override
                public void mouseClicked(MouseEvent e)
                {
                    if (removeMemberButtonMouseListener != null)
                    {
                        removeMemberButtonMouseListener.mouseClicked(e);
                    }
                }
            });
        } else
        {
            ImageIcon imageIcon = new ImageIcon();
            imageIcon.setImage(AvatarUtil.createOrLoadUserAvatar(name).getScaledInstance(30, 30, Image.SCALE_SMOOTH));
            viewHolder.avatar.setIcon(imageIcon);

            UserInfoPopup userInfoPopup = new UserInfoPopup(name);


            if (!name.equals(currentUser.getUsername()))
            {
                viewHolder.addMouseListener(new AbstractMouseListener()
                {
                    @Override
                    public void mouseEntered(MouseEvent e)
                    {
                        viewHolder.setBackground(Colors.LIGHT_GRAY);
                        super.mouseEntered(e);
                    }

                    @Override
                    public void mouseExited(MouseEvent e)
                    {
                        viewHolder.setBackground(Colors.WINDOW_BACKGROUND_LIGHT);

                    }

                    @Override
                    public void mouseClicked(MouseEvent e)
                    {
                        // 弹出用户信息面板
                        if (e.getButton() == MouseEvent.BUTTON1)
                        {
                            userInfoPopup.show(e.getComponent(), e.getX(), e.getY());
                        }


                        for (RoomMembersItemViewHolder holder : viewHolders)
                        {
                            if (holder != viewHolder)
                            {
                                holder.setBackground(Colors.WINDOW_BACKGROUND_LIGHT);
                            }
                        }

                    }
                });
            }
        }
    }


    @Override
    public int getCount()
    {
        return members.size();
    }

    public void setAddMemberButtonMouseListener(MouseAdapter addMemberButtonMouseListener)
    {
        this.addMemberButtonMouseListener = addMemberButtonMouseListener;
    }

    public void setRemoveMemberButtonMouseListener(MouseAdapter removeMemberButtonMouseListener)
    {
        this.removeMemberButtonMouseListener = removeMemberButtonMouseListener;
    }
}

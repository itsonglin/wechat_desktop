package com.rc.adapter;

import com.rc.app.Launcher;
import com.rc.components.Colors;
import com.rc.db.model.CurrentUser;
import com.rc.db.model.Room;
import com.rc.db.service.CurrentUserService;
import com.rc.db.service.RoomService;
import com.rc.entity.RoomItem;
import com.rc.panels.ChatPanel;
import com.rc.listener.AbstractMouseListener;
import com.rc.utils.AvatarUtil;
import com.rc.utils.TimeUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 17-5-30.
 */
public class RoomItemsAdapter extends BaseAdapter<RoomItemViewHolder>
{
    private List<RoomItem> roomItems;
    private List<RoomItemViewHolder> viewHolders = new ArrayList<>();
    private RoomItemViewHolder selectedViewHolder; // 当前选中的viewHolder
    private RoomService roomService = Launcher.roomService;

    public RoomItemsAdapter(List<RoomItem> roomItems)
    {
        this.roomItems = roomItems;
    }

    @Override
    public int getCount()
    {
        return roomItems.size();
    }

    @Override
    public RoomItemViewHolder onCreateViewHolder(int viewType)
    {
        return new RoomItemViewHolder();
    }

    @Override
    public void onBindViewHolder(RoomItemViewHolder viewHolder, int position)
    {
        if (!viewHolders.contains(viewHolder))
        {
            viewHolders.add(viewHolder);
        }
        //viewHolder.setCursor(new Cursor(Cursor.HAND_CURSOR));

        RoomItem item = roomItems.get(position);

        viewHolder.setTag(item.getRoomId());

        viewHolder.roomName.setText(item.getTitle());


        ImageIcon icon = new ImageIcon();
        // 群组头像
        String type = item.getType();
        if (type.equals("c") || type.equals("p"))
        {
            String[] memberArr = getRoomMembers(item.getRoomId());

            icon.setImage(AvatarUtil.createOrLoadGroupAvatar(item.getTitle(), memberArr, type)
                    .getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        }
        // 私聊头像
        else if (type.equals("d"))
        {
            Image image = AvatarUtil.createOrLoadUserAvatar(item.getTitle()).getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            icon.setImage(image);
        }
        viewHolder.avatar.setIcon(icon);


        // 消息
        viewHolder.brief.setText(item.getLastMessage());
        if (item.getLastMessage() != null && item.getLastMessage().length() > 15)
        {
            viewHolder.brief.setText(item.getLastMessage().substring(0, 15) + "...");
        }
        else
        {
            viewHolder.brief.setText(item.getLastMessage());
        }

        // 时间
        if (item.getTimestamp() > 0)
        {
            viewHolder.time.setText(TimeUtil.diff(item.getTimestamp()));
        }

        // 未读消息数
        if (item.getUnreadCount() > 0)
        {
            viewHolder.unreadCount.setVisible(true);
            viewHolder.unreadCount.setText(item.getUnreadCount() + "");
        }
        else
        {
            viewHolder.unreadCount.setVisible(false);
        }

        // 设置是否激活
        if (ChatPanel.CHAT_ROOM_OPEN_ID != null && item.getRoomId().equals(ChatPanel.CHAT_ROOM_OPEN_ID))
        {
            setBackground(viewHolder, Colors.ITEM_SELECTED);
            selectedViewHolder = viewHolder;
        }
        //viewHolder.unreadCount.setVisible(true);
        //viewHolder.unreadCount.setText(item.getUnreadCount() + "1");


        viewHolder.addMouseListener(new AbstractMouseListener()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {

                    if (selectedViewHolder != viewHolder)
                    {
                        // 进入房间
                        enterRoom(item.getRoomId());

                        for (RoomItemViewHolder holder : viewHolders)
                        {
                            if (holder != viewHolder)
                            {
                                setBackground(holder, Colors.DARK);
                            }
                        }

                        //setBackground(viewHolder, Colors.ITEM_SELECTED);
                        selectedViewHolder = viewHolder;
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

    private String[] getRoomMembers(String roomId)
    {
        Room room = roomService.findById(roomId);
        String members = room.getMember();
        String[] memberArr = null;

        List<String> roomMembers = new ArrayList<>();
        if (members != null)
        {
            String[] userArr = members.split(",");
            for (int i = 0; i < userArr.length; i++)
            {
                if (!roomMembers.contains(userArr[i]))
                {
                    roomMembers.add(userArr[i]);
                }
            }
        }
        String creator = room.getCreatorName();
        if (creator != null)
        {
            if (!roomMembers.equals(creator))
            {
                roomMembers.add(creator);
            }
        }

        memberArr = roomMembers.toArray(new String[]{});
        return memberArr;
    }

    private void setBackground(RoomItemViewHolder holder, Color color)
    {
        holder.setBackground(color);
        holder.nameBrief.setBackground(color);
        holder.timeUnread.setBackground(color);
    }

    private void enterRoom(String roomId)
    {
        // 加载房间消息
        ChatPanel.getContext().enterRoom(roomId);

        //TitlePanel.getContext().hideRoomMembersPanel();
        /*RoomMembersPanel.getContext().setRoomId(roomId);
        if (RoomMembersPanel.getContext().isVisible())
        {
            RoomMembersPanel.getContext().updateUI();
        }*/
    }

}

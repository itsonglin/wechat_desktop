package com.rc.panels;

import com.rc.adapter.RoomItemViewHolder;
import com.rc.adapter.RoomItemsAdapter;
import com.rc.app.Launcher;
import com.rc.components.*;
import com.rc.db.model.Room;
import com.rc.db.service.RoomService;
import com.rc.entity.RoomItem;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 左侧聊天列表
 * Created by song on 17-5-30.
 */
public class RoomsPanel extends ParentAvailablePanel
{
    private static RoomsPanel context;

    private RCListView roomItemsListView;
    private List<RoomItem> roomItemList = new ArrayList<>();
    private RoomService roomService = Launcher.roomService;


    public RoomsPanel(JPanel parent)
    {
        super(parent);
        context = this;

        initComponents();
        initView();
        initData();
        roomItemsListView.setAdapter(new RoomItemsAdapter(roomItemList));
    }

    private void initComponents()
    {
        roomItemsListView = new RCListView();
    }

    private void initView()
    {
        setLayout(new GridBagLayout());
        roomItemsListView.setContentPanelBackground(Colors.DARK);
        add(roomItemsListView, new GBC(0, 0).setFill(GBC.BOTH).setWeight(1, 1));
        //add(scrollPane, new GBC(0, 0).setFill(GBC.BOTH).setWeight(1, 1));
    }

    private void initData()
    {
        roomItemList.clear();

        // TODO: 从数据库中加载房间列表

        List<Room> rooms = roomService.findAll();
        for (Room room : rooms)
        {
            RoomItem item = new RoomItem();
            item.setRoomId(room.getRoomId());
            item.setTimestamp(room.getLastChatAt());
            item.setTitle(room.getName());
            item.setType(room.getType());
            item.setLastMessage(room.getLastMessage());
            item.setUnreadCount(room.getUnreadCount());

            roomItemList.add(item);
        }
    }

    /**
     * 重绘整个列表
     */
    public void notifyDataSetChanged(boolean keepSize)
    {
        initData();
        roomItemsListView.notifyDataSetChanged(keepSize);
    }

    /**
     * 更新房间列表
     * 当这条消息所在的房间在当前房间列表中排在第一位时，此时房间列表项目顺序不变，无需重新排列
     * 因此无需更新整个房间列表，只需更新第一个项目即可
     *
     * @param msgRoomId
     */
    public void updateRoomsList(String msgRoomId)
    {
        String roomId = (String) ((RoomItemViewHolder) (roomItemsListView.getItem(0))).getTag();
        if (roomId.equals(msgRoomId))
        {
            Room room = roomService.findById(roomId);
            for (RoomItem roomItem : roomItemList)
            {
                if (roomItem.getRoomId().equals(roomId))
                {
                    roomItem.setUnreadCount(room.getUnreadCount());
                    roomItem.setTimestamp(room.getLastChatAt());
                    roomItem.setLastMessage(room.getLastMessage());
                    break;
                }
            }

            roomItemsListView.notifyItemChanged(0);
        }
        else
        {
            notifyDataSetChanged(false);
        }
    }

    /**
     * 更新指定位置的房间项目
     * @param roomId
     */
    public void updateRoomItem(String roomId)
    {
        if (roomId == null || roomId.isEmpty())
        {
            notifyDataSetChanged(true);
            return;
        }

        for (int i = 0; i < roomItemList.size(); i++)
        {
            RoomItem item = roomItemList.get(i);
            if (item.getRoomId().equals(roomId))
            {
                Room room = roomService.findById(item.getRoomId());
                if (room != null)
                {
                    item.setLastMessage(room.getLastMessage());
                    item.setTimestamp(room.getLastChatAt());
                    item.setUnreadCount(room.getUnreadCount());
                    roomItemsListView.notifyItemChanged(i);
                }
                break;
            }
        }
    }

    /**
     * 激活指定的房间项目
     * @param position
     */
    public void activeItem(int position)
    {
        RoomItemViewHolder holder = (RoomItemViewHolder) roomItemsListView.getItem(position);
        setItemBackground(holder, Colors.ITEM_SELECTED);
    }

    /**
     * 设置每个房间项目的背影色
     * @param holder
     * @param color
     */
    private void setItemBackground(RoomItemViewHolder holder, Color color)
    {
        holder.setBackground(color);
        holder.nameBrief.setBackground(color);
        holder.timeUnread.setBackground(color);
    }



    public static RoomsPanel getContext()
    {
        return context;
    }
}

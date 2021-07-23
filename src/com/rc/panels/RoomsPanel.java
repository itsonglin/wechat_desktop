package com.rc.panels;

import com.rc.adapter.RoomItemViewHolder;
import com.rc.adapter.RoomItemsAdapter;
import com.rc.app.Launcher;
import com.rc.components.*;
import com.rc.db.model.Message;
import com.rc.db.model.Room;
import com.rc.db.service.RoomService;
import com.rc.entity.RoomItem;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 17-5-30.
 *
 * <P>下图 #RoomsPanel# 对应的位置</P>
 *
 * 显示房间列表
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
 * │                        │                                                        │
 * │                        │                     message time                       │
 * │                        │                                    ┌────────────┐ ┌──┐ │
 * │                        │                                    │  message   │ └──┘ │
 * │                        │                                    └────────────┘      │
 * │      #RoomsPanel#      │                                                        │
 * │                        │                                                        │
 * │                        ├────────────────────────────────────────────────────────┤
 * │                        │  ▆   ▆   ▆                                             │
 * │                        │                                                        │
 * │                        │                                                        │
 * │                        │                                                ┌─────┐ │
 * │                        │                                                └─────┘ │
 * └────────────────────────┴────────────────────────────────────────────────────────┘
 */
public class RoomsPanel extends ParentAvailablePanel
{
    private static RoomsPanel context;
    private final RoomItemsAdapter adapter;

    private RCListView roomItemsListView;
    private List<RoomItem> roomItemList = new ArrayList<>();
    private RoomService roomService = Launcher.roomService;


    public RoomsPanel(JPanel parent)
    {
        super(parent);
        context = this;

        initComponents();
        setListeners();
        initView();
        initData();

        adapter = new RoomItemsAdapter(roomItemList);
       // roomItemsListView.setScrollHiddenOnMouseLeave(roomItemsListView);
        roomItemsListView.setAdapter(adapter);
    }

    private void initComponents()
    {
        roomItemsListView = new RCListView();
    }

    private void setListeners()
    {
    }

    private void initView()
    {
        setLayout(new GridBagLayout());
        roomItemsListView.setContentPanelBackground(Colors.BG_GRAY);
        add(roomItemsListView, new GBC(0, 0).setFill(GBC.BOTH).setWeight(1, 1));
        //add(scrollPane, new GBC(0, 0).setFill(GBC.BOTH).setWeight(1, 1));
    }

    private void initData()
    {
        roomItemList.clear();
       /* for (int i = 0 ; i < 10; i ++)
        {
            RoomItem item = new RoomItem();
            roomItemList.add(item);
        }*/
        List<Room> rooms = roomService.findAll();

        for (Room room : rooms)
        {
            if (room.getHidden())
            {
                //System.out.println(room.getName() + "已隐藏, 不显示在列表中");
                continue;
            }
            RoomItem item = new RoomItem();
            item.setRoomId(room.getRoomId());
            item.setTimestamp(room.getLastChatAt());
            item.setTitle(room.getName());
            item.setType(room.getType());
            item.setLastMessage(room.getLastMessage());
            item.setUnreadCount(room.getUnreadCount());
            item.setShowNotify(room.isShowNotify());

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
     * @param message
     */
    public void updateRoomsList(Message message)
    {
        String roomId = (String) ((RoomItemViewHolder) (roomItemsListView.getItem(0))).getTag();
        if (roomId.equals(message.getRoomId()))
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
                    item.setShowNotify(room.isShowNotify());
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

    public void restoreActiveItem()
    {
        adapter.restoreActiveItem();
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

    public void hideScrollbar()
    {
        roomItemsListView.hideVerticalScrollbar();
    }

    public void showScrollbar()
    {
        roomItemsListView.showVerticalScrollbar();
    }

    public RCListView getRoomItemsListView()
    {
        return roomItemsListView;
    }

    /**
     * 打开房间
     * @param roomId
     */
    public void openRoom(String roomId)
    {
        this.adapter.openRoomPanel(roomId);
    }

    public List<RoomItem> getRoomItemList()
    {
        return roomItemList;
    }

    public void setRoomItemList(List<RoomItem> roomItemList)
    {
        this.roomItemList = roomItemList;
    }

    public void clearSelectedViewHolder()
    {
        adapter.clearSelectedViewHolder();
    }
}

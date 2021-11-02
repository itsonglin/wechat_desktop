package com.rc.adapter;

import com.rc.app.Launcher;
import com.rc.components.Colors;
import com.rc.components.room.RoomPopupMenu;
import com.rc.db.model.Room;
import com.rc.db.service.RoomService;
import com.rc.entity.RoomItem;
import com.rc.panels.ChatPanel;
import com.rc.listener.AbstractMouseListener;
import com.rc.panels.RoomsPanel;
import com.rc.utils.AvatarUtil;
import com.rc.utils.IconUtil;
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
    private RoomPopupMenu popupMenu = new RoomPopupMenu(this);


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
    public RoomItemViewHolder onCreateViewHolder(int viewType, int position)
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


        // 时间
        if (item.getTimestamp() > 0)
        {
            viewHolder.time.setText(TimeUtil.diff(item.getTimestamp()));
        }

        String briefText = item.getLastMessage();

        // 未读消息数
        int unread = item.getUnreadCount();
        if (unread > 0)
        {
            if (item.isShowNotify())
            {
                String txt = unread > 99 ? "99+": unread + "";
                int fontWidth = viewHolder.getFontMetrics().stringWidth(txt);
                if (fontWidth < 20)
                {
                    fontWidth += 15;
                }

                viewHolder.newMsgIcon.setPreferredSize(new Dimension( fontWidth, 15));
                viewHolder.newMsgIcon.setIcon(IconUtil.getIcon(this, "/image/count_bg.png", true));
                viewHolder.newMsgIcon.setText(txt);
            }
            else
            {
                if (unread > 1)
                {
                    briefText = "[" + unread + "条] " + briefText;
                }
                viewHolder.newMsgIcon.setIcon(IconUtil.getIcon(this, "/image/count_bg.png", 10, 10, true));
                viewHolder.newMsgIcon.setText("");
                viewHolder.newMsgIcon.setPreferredSize(new Dimension(10, 10));

            }

        } else
        {
            viewHolder.newMsgIcon.setIcon(null);
            viewHolder.newMsgIcon.setText("");
        }

        // 消息
        viewHolder.brief.setText(briefText);
        if (briefText != null && briefText.length() > 15)
        {
            viewHolder.brief.setText(briefText.substring(0, 15) + "...");
        } else
        {
            viewHolder.brief.setText(briefText);
        }


        // 设置是否激活
        if (ChatPanel.CHAT_ROOM_OPEN_ID != null && item.getRoomId().equals(ChatPanel.CHAT_ROOM_OPEN_ID))
        {
            setBackground(viewHolder, Colors.ITEM_SELECTED);
            selectedViewHolder = viewHolder;
        }

        // 消息免打扰铃铛
        if (item.isShowNotify())
        {
            viewHolder.bell.setIcon(null);
        } else
        {
            viewHolder.bell.setIcon(IconUtil.getIcon(this, "/image/bell.png", 12, 12, true));
        }

        /*Graphics  g = viewHolder.getGraphics();
        if (g != null)
        {
            g.setColor(Colors.RED);
            g.fillRoundRect(0, 10, 10, 10, 10, 10);
        }*/


        AbstractMouseListener viewHolderMouseAdapter = new AbstractMouseListener()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {

                    openRoomPanel(viewHolder);
                }
            }


            @Override
            public void mouseEntered(MouseEvent e)
            {
                if (selectedViewHolder != viewHolder)
                {
                    setBackground(viewHolder, Colors.BG_GRAY_DARKER);
                }
                RoomsPanel.getContext().hideScrollbar();
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                if (selectedViewHolder != viewHolder)
                {
                    setBackground(viewHolder, Colors.BG_GRAY);
                }

                RoomsPanel.getContext().showScrollbar();
            }

            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON3)
                {
                    popupMenu.show(viewHolder, e.getComponent(), e.getX(), e.getY());
                }
            }
        };

        viewHolder.addMouseListener(viewHolderMouseAdapter);
        viewHolder.brief.addMouseListener(viewHolderMouseAdapter);
    }

    /**
     * 打开房间
     *
     * @param viewHolder
     */
    public void openRoomPanel(RoomItemViewHolder viewHolder)
    {
        if (selectedViewHolder != viewHolder)
        {
            for (RoomItemViewHolder holder : viewHolders)
            {
                if (holder != viewHolder)
                {
                    setBackground(holder, Colors.BG_GRAY);
                }
            }

            // 进入房间
            enterRoom(viewHolder.getTag().toString());

            //setBackground(viewHolder, Colors.ITEM_SELECTED);
            selectedViewHolder = viewHolder;
        }
    }

    public void clearSelectedViewHolder()
    {
        this.selectedViewHolder = null;
    }

    /**
     * 打开房间
     *
     * @param roomId 房间ID
     */
    public void openRoomPanel(String roomId)
    {
        for (RoomItemViewHolder viewHolder : this.viewHolders)
        {
            if (viewHolder.getTag().equals(roomId))
            {
                openRoomPanel(viewHolder);
                break;
            }
        }
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

    public RoomItemViewHolder getSelectedViewHolder()
    {
        return selectedViewHolder;
    }

    private void setBackground(RoomItemViewHolder holder, Color color)
    {
        holder.setBackground(color);
        holder.nameBrief.setBackground(color);
        holder.timeUnread.setBackground(color);
        holder.brief.setBackground(color);
        holder.avatarNewMsg.setBackground(color);
    }

    private void enterRoom(String roomId)
    {
        // 加载房间消息
        ChatPanel.getContext().enterRoom(roomId);
    }

    public void restoreActiveItem()
    {
        if (selectedViewHolder != null)
        {
            setBackground(selectedViewHolder, Colors.BG_GRAY);
            selectedViewHolder = null;
        }
    }
}

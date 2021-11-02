package com.rc.components.room;

import com.rc.adapter.RoomItemViewHolder;
import com.rc.adapter.RoomItemsAdapter;
import com.rc.app.Launcher;
import com.rc.res.Colors;
import com.rc.components.NotificationTextArea;
import com.rc.components.RCMenuItemUI;
import com.rc.db.model.Room;
import com.rc.db.service.RoomService;
import com.rc.panels.RoomMembersPanel;
import com.rc.panels.RoomsPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

import static com.rc.panels.ChatPanel.CHAT_ROOM_OPEN_ID;

/**
 * 房间列表右键菜单
 * Created by song on 2019/9/20.
 */
public class RoomPopupMenu extends JPopupMenu
{
    private JMenuItem itemEnter = new JMenuItem("打开");
    private JMenuItem itemNotify = new JMenuItem("消息免打扰");
    private JMenuItem itemDelete = new JMenuItem("删除聊天");
    private RoomItemsAdapter roomItemsAdapter;

    private RoomService roomService = Launcher.roomService;

    public RoomPopupMenu(RoomItemsAdapter roomItemsAdapter)
    {
        this.roomItemsAdapter = roomItemsAdapter;
        initMenuItem();
    }

    private void initMenuItem()
    {

        itemEnter.setUI(new RCMenuItemUI(100, 40));
        itemEnter.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                RoomItemViewHolder viewHolder = (RoomItemViewHolder) RoomPopupMenu.this.getInvoker();
                roomItemsAdapter.openRoomPanel(viewHolder);
            }
        });


        itemNotify.setUI(new RCMenuItemUI(100, 40));
        itemNotify.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                RoomItemViewHolder viewHolder;
                if (RoomPopupMenu.this.getInvoker() instanceof NotificationTextArea)
                {
                    NotificationTextArea textArea = (NotificationTextArea) RoomPopupMenu.this.getInvoker();
                    viewHolder = (RoomItemViewHolder) textArea.getParent().getParent();
                }
                else
                {
                    viewHolder = (RoomItemViewHolder) RoomPopupMenu.this.getInvoker();
                }

                Room room = roomService.findById(viewHolder.getTag().toString());
                room.setShowNotify(!room.isShowNotify());
                roomService.update(room);

                if (CHAT_ROOM_OPEN_ID.equals(room.getRoomId()) && RoomMembersPanel.getContext().isVisible())
                {
                    RoomMembersPanel.getContext().updateUI();
                }

                RoomsPanel.getContext().updateRoomItem(room.getRoomId());
            }
        });

        itemDelete.setUI(new RCMenuItemUI(100, 40));
        itemDelete.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                RoomItemViewHolder viewHolder = (RoomItemViewHolder) RoomPopupMenu.this.getInvoker();
                // todo 发送删除聊天请求
            }
        });

        this.add(itemEnter);
        this.add(itemNotify);
        this.add(itemDelete);

        setBorder(new LineBorder(Colors.SCROLL_BAR_TRACK_LIGHT));
        setBackground(Colors.FONT_WHITE);
    }


    @Override
    public void show(Component invoker, int x, int y)
    {
       // 弃用
    }

    public void show(RoomItemViewHolder viewHolder, Component invoker, int x, int y)
    {
        Room room = roomService.findById(viewHolder.getTag().toString());
        if (room.isShowNotify())
        {
            itemNotify.setText("消息免打扰");
        }
        else
        {
            itemNotify.setText("显示通知气泡");
        }
        super.show(invoker, x, y);
    }

}

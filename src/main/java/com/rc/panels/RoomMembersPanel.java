package com.rc.panels;

import com.rc.adapter.RoomMembersAdapter;
import com.rc.app.Launcher;
import com.rc.components.*;
import com.rc.db.model.ContactsUser;
import com.rc.db.model.CurrentUser;
import com.rc.db.model.Room;
import com.rc.db.service.ContactsUserService;
import com.rc.db.service.CurrentUserService;
import com.rc.db.service.RoomService;
import com.rc.entity.SelectUserData;
import com.rc.frames.AddOrRemoveMemberDialog;
import com.rc.frames.MainFrame;
import com.rc.res.Colors;
import com.rc.tasks.HttpPostTask;
import com.rc.tasks.HttpResponseListener;
import com.rc.utils.AvatarUtil;
import com.rc.utils.FontUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 07/06/2017.
 * <p>
 * <p>下图 #RoomMembersPanel# 对应的位置</p>
 * <p>
 * 显示房间成员列表、添加或删除成员按钮、退出或解散群聊按钮等
 * <p>
 * <P>推荐使用Menlo或Consolas字体</P>
 * ┌────────────────────────┬────────────────────────────────────┬────────────────────┐
 * │ ┌─────┐                │  Room Title                        │                 ≡  │
 * │ │     │ name         ≡ ├────────────────────────────────────┼────────────────────┤
 * │ └─────┘                │                                    │                    │
 * ├────────────────────────┤                  message time      │                    │
 * │    search              │  ┌──┐ ┌────────────┐               │                    │
 * ├────────────────────────┤  └──┘ │  message   │               │                    │
 * │  ▆    │    ▆   │   ▆   │       └────────────┘               │                    │
 * ├────────────────────────┤                                    │                    │
 * │ ┌──┐ name         14:01│                                    │                    │
 * │ └──┘ message        99+│                                    │                    │
 * ├────────────────────────┤                                    │                    │
 * │                        │                                    │ #RoomMembersPanel# │
 * │                        │                                    │                    │
 * │                        │                                    │                    │
 * │          Room          │                                    │                    │
 * │                        ├────────────────────────────────────┤                    │
 * │                        │  ▆   ▆   ▆                         │                    │
 * │          List          │                                    │                    │
 * │                        │                                    │                    │
 * │                        │                                    │                    │
 * │                        │                                    │                    │
 * └────────────────────────┴────────────────────────────────────┴────────────────────┘
 */
public class RoomMembersPanel extends ParentAvailablePanel
{
    public static final int ROOM_MEMBER_PANEL_WIDTH = 200;
    private static RoomMembersPanel roomMembersPanel;

    private RCListView listView = new RCListView();
    private JPanel operationPanel = new JPanel();
    private JButton leaveButton;
    private JCheckBox showNotifyCheckBox;

    private List<String> members = new ArrayList<>();
    private String roomId;
    private RoomService roomService = Launcher.roomService;
    private CurrentUserService currentUserService = Launcher.currentUserService;
    private CurrentUser currentUser;
    private Room room;
    private ContactsUserService contactsUserService = Launcher.contactsUserService;
    private RoomMembersAdapter adapter;
    private AddOrRemoveMemberDialog addOrRemoveMemberDialog;

    public RoomMembersPanel(JPanel parent)
    {
        super(parent);
        roomMembersPanel = this;

        initComponents();
        initView();
        setListeners();

        List<CurrentUser> users = currentUserService.findAll();
        currentUser = users.size() < 1 ? Launcher.currentUser : users.get(0);
    }

    private void initComponents()
    {
        setBorder(new LineBorder(Colors.LIGHT_GRAY));
        setBackground(Colors.FONT_WHITE);

        setPreferredSize(new Dimension(ROOM_MEMBER_PANEL_WIDTH, MainFrame.getContext().currentWindowHeight));
        setVisible(false);
        listView.setScrollBarColor(Colors.SCROLL_BAR_THUMB, Colors.WINDOW_BACKGROUND);
        listView.setContentPanelBackground(Colors.FONT_WHITE);
        listView.getContentPanel().setBackground(Colors.FONT_WHITE);

        operationPanel.setPreferredSize(new Dimension(60, 80));
        operationPanel.setBackground(Colors.FONT_WHITE);


        leaveButton = new RCButton("退出群聊", Colors.WINDOW_BACKGROUND_LIGHT, Colors.WINDOW_BACKGROUND, Colors.SCROLL_BAR_TRACK_LIGHT);
        leaveButton.setForeground(Colors.RED);
        leaveButton.setPreferredSize(new Dimension(180, 30));

        showNotifyCheckBox = new JCheckBox("显示通知气泡");
        showNotifyCheckBox.setBackground(Colors.FONT_WHITE);
        showNotifyCheckBox.setFont(FontUtil.getDefaultFont(14));

    }

    private void initView()
    {
        operationPanel.add(showNotifyCheckBox);
        operationPanel.add(leaveButton);

        setLayout(new GridBagLayout());
        add(listView, new GBC(0, 0).setFill(GBC.BOTH).setWeight(1, 90));
        add(operationPanel, new GBC(0, 1).setFill(GBC.BOTH).setWeight(1, 5).setInsets(10, 0, 5, 0));

        adapter = new RoomMembersAdapter(members);
        listView.setAdapter(adapter);

        /*setLayout(new VerticalFlowLayout(FlowLayout.CENTER, 0, 10, true, false));
        add(listView);
        add(operationPanel);*/
    }

    public void setRoomId(String roomId)
    {
        this.roomId = roomId;
        room = roomService.findById(roomId);
    }

    public void setVisibleAndUpdateUI(boolean aFlag)
    {
        if (aFlag)
        {
            updateUI();
            setVisible(aFlag);
        }

        setVisible(aFlag);
    }

    public void updateUI()
    {
        if (roomId != null)
        {
            try
            {
                room = roomService.findById(roomId);
            } catch (Exception e)
            {
                /*try
                {
                    Thread.sleep(200);
                } catch (InterruptedException e1)
                {
                    e1.printStackTrace();
                }*/
                e.printStackTrace();
                System.out.println("roomService.findById(roomId)获取失败，200ms后重新获取");
                room = roomService.findById(roomId);
            }

            getRoomMembers();

            // 单独聊天，不显示退出按钮
            if (room.getType().equals("d"))
            {
                leaveButton.setVisible(false);
            }
            else
            {
                leaveButton.setVisible(true);
            }

            listView.notifyDataSetChanged(false);

            // 如果是公告，则不允许退出
            if (room.getRoomId().equals("GENERAL"))
            {
                setLeaveButtonVisibility(false);
            }
            else
            {
                setLeaveButtonVisibility(true);

                if (isRoomCreator())
                {
                    leaveButton.setText("解散群聊");
                }
                else
                {
                    leaveButton.setText("退出群聊");
                }
            }

            showNotifyCheckBox.setSelected(room.isShowNotify());
        }
    }

    private void getRoomMembers()
    {
        members.clear();

        // 单独聊天，成员只显示两人
        if (room.getType().equals("d"))
        {
            members.add(currentUser.getUsername());
            members.add(room.getName());
        }
        else
        {
            String roomMembers = room.getMember();
            String[] userArr = new String[]{};
            if (roomMembers != null)
            {
                userArr = roomMembers.split(",");
            }

            if (isRoomCreator())
            {
                members.remove("添加成员");
                members.add("添加成员");

                if (userArr.length > 1)
                {
                    members.remove("删除成员");
                    members.add("删除成员");
                }
            }

            if (room.getCreatorName() != null)
            {
                members.add(room.getCreatorName());
            }

            for (int i = 0; i < userArr.length; i++)
            {
                if (!members.contains(userArr[i]))
                {
                    members.add(userArr[i]);
                }
            }
        }
    }


    /**
     * 判断当前用户是否是房间创建者
     *
     * @return
     */
    private boolean isRoomCreator()
    {
        return room.getCreatorName() != null && room.getCreatorName().equals(currentUser.getUsername());
    }


    public static RoomMembersPanel getContext()
    {
        return roomMembersPanel;
    }

    public void setLeaveButtonVisibility(boolean visible)
    {
        operationPanel.setVisible(visible);
    }

    private void setListeners()
    {
        adapter.setAddMemberButtonMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                selectAndAddRoomMember();
                super.mouseClicked(e);
            }
        });

        adapter.setRemoveMemberButtonMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                selectAndRemoveRoomMember();
                super.mouseClicked(e);
            }
        });

        leaveButton.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (isRoomCreator())
                {
                    int ret = JOptionPane.showConfirmDialog(MainFrame.getContext(), "确认解散群聊？", "确认解散群聊", JOptionPane.YES_NO_OPTION);
                    if (ret == JOptionPane.YES_OPTION)
                    {
                        deleteChannelOrGroup(room.getRoomId());
                    }
                }
                else
                {
                    int ret = JOptionPane.showConfirmDialog(MainFrame.getContext(), "退出群聊，并从聊天列表中删除该群聊", "确认退出群聊", JOptionPane.YES_NO_OPTION);
                    if (ret == JOptionPane.YES_OPTION)
                    {
                        leaveChannelOrGroup(room.getRoomId());
                    }
                }
                super.mouseClicked(e);
            }
        });

        showNotifyCheckBox.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                room.setShowNotify(showNotifyCheckBox.isSelected());
                roomService.update(room);
                room = roomService.findById(roomId);
                RoomsPanel.getContext().updateRoomItem(room.getRoomId());
            }
        });
    }


    /**
     * 选择并添加群成员
     */
    private void selectAndAddRoomMember()
    {
        List<ContactsUser> contactsUsers = contactsUserService.findAll();
        List<SelectUserData> selectUsers = new ArrayList<>();

        for (ContactsUser contactsUser : contactsUsers)
        {
            if (!members.contains(contactsUser.getUsername()))
            {
                selectUsers.add(new SelectUserData(contactsUser.getUsername(), false));
            }
        }
        addOrRemoveMemberDialog = new AddOrRemoveMemberDialog(MainFrame.getContext(), true, selectUsers);
        addOrRemoveMemberDialog.getOkButton().setText("添加");
        addOrRemoveMemberDialog.getOkButton().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (((JButton) e.getSource()).isEnabled())
                {
                    ((JButton) e.getSource()).setEnabled(false);
                    List<SelectUserData> selectedUsers = addOrRemoveMemberDialog.getSelectedUser();
                    String[] userArr = new String[selectedUsers.size()];
                    for (int i = 0; i < selectedUsers.size(); i++)
                    {
                        userArr[i] = selectedUsers.get(i).getName();
                    }

                    inviteOrKick(userArr, "invite");
                }
                super.mouseClicked(e);
            }
        });
        addOrRemoveMemberDialog.setVisible(true);
    }

    /**
     * 选择并移除群成员
     */
    private void selectAndRemoveRoomMember()
    {
        List<SelectUserData> userDataList = new ArrayList<>();
        for (String member : members)
        {
            if (member.equals(room.getCreatorName()) || member.equals("添加成员") || member.equals("删除成员"))
            {
                continue;
            }
            userDataList.add(new SelectUserData(member, false));
        }

        addOrRemoveMemberDialog = new AddOrRemoveMemberDialog(MainFrame.getContext(), true, userDataList);
        addOrRemoveMemberDialog.getOkButton().setText("移除");
        addOrRemoveMemberDialog.getOkButton().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (((JButton) e.getSource()).isEnabled())
                {
                    ((JButton) e.getSource()).setEnabled(false);
                    List<SelectUserData> selectedUsers = addOrRemoveMemberDialog.getSelectedUser();
                    String[] userArr = new String[selectedUsers.size()];
                    for (int i = 0; i < selectedUsers.size(); i++)
                    {
                        userArr[i] = selectedUsers.get(i).getName();
                    }

                    inviteOrKick(userArr, "kick");
                }

                super.mouseClicked(e);
            }
        });
        addOrRemoveMemberDialog.setVisible(true);
    }


    private void inviteOrKick(final String[] usernames, String type)
    {
        final int[] count = {0};
        HttpResponseListener callback = new HttpResponseListener<JSONObject>()
        {
            @Override
            public void onSuccess(JSONObject retJson)
            {
                if (++count[0] == usernames.length)
                {
                    addOrRemoveMemberDialog.setVisible(false);
                    updateRoomMembers(retJson);
                }

            }

            @Override
            public void onFailed(Object why)
            {
                JOptionPane.showMessageDialog(null, "网络请求失败", "网络请求失败", JOptionPane.ERROR_MESSAGE);
            }
        };

        for (String username : usernames)
        {
            String t = getRoomType();
            String url = "";
            String userId = contactsUserService.findByUsername(username).getUserId();

            HttpPostTask task = new HttpPostTask();
            task.addHeader("X-Auth-Token", currentUser.getAuthToken());
            task.addHeader("X-User-Id", currentUser.getUserId());
            task.addRequestParam("roomId", room.getRoomId());
            task.addRequestParam("userId", userId);
            task.setListener(callback);

            task.execute(url);
        }
    }

    private String getRoomType()
    {
        String t;
        if (room.getType().equals("c"))
        {
            t = "channels";
        }
        else if (room.getType().equals("p"))
        {
            t = "groups";
        }
        else
        {
            throw new RuntimeException("房间类型不正确");
        }

        return t;
    }

    /**
     * 更新房间成员
     *
     * @param retJson
     */
    private void updateRoomMembers(JSONObject retJson)
    {
        try
        {
            String type = "";
            if (retJson.has("channel"))
            {
                type = "channel";
            }
            else if (retJson.has("group"))
            {
                type = "group";
            }
            else if (retJson.has("error"))
            {
                JOptionPane.showMessageDialog(null, "添加成员失败！", "添加成员失败", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else
            {
                // 移除成员时，只收到成员通知
                if (retJson.has("success"))
                {
                    ChatPanel.getContext().loadRemoteRoomMembers();
                }
                return;
            }

            JSONObject obj = retJson.getJSONObject(type);
            JSONArray usernames = obj.getJSONArray("usernames");
            List<String> users = new ArrayList<>();
            for (int i = 0; i < usernames.length(); i++)
            {
                if (!users.contains(usernames.get(i)))
                {
                    users.add(usernames.get(i).toString());
                }
            }

            ChatPanel.getContext().updateLocalDBRoomMembers(users);
            ChatPanel.getContext().updateRoomTitle();

            // 如果已打开成员面板，则更新
            if (isVisible())
            {
                updateUI();
            }

            // 重新生成群头像
            System.out.println("删除原来群头像: " + room.getName());
            AvatarUtil.deleteGroupAvatar(room.getName());

        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 删除Channel或Group
     *
     * @param roomId
     */
    private void deleteChannelOrGroup(final String roomId)
    {
        HttpPostTask task = new HttpPostTask();
        task.addHeader("X-Auth-Token", currentUser.getAuthToken());
        task.addHeader("X-User-Id", currentUser.getUserId());
        task.addRequestParam("roomId", room.getRoomId());
        task.setListener(new HttpResponseListener<JSONObject>()
        {
            @Override
            public void onSuccess(JSONObject retJson)
            {
                if (retJson.has("success"))
                {
                    RightPanel.getContext().showPanel(RightPanel.TIP);
                    TitlePanel.getContext().showAppTitle();
                    RoomMembersPanel.getContext().setVisible(false);
                }
                else
                {
                    JOptionPane.showMessageDialog(MainFrame.getContext(), "删除失败", "删除失败", JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override
            public void onFailed(Object why)
            {
                JOptionPane.showMessageDialog(null, "网络请求失败", "网络请求失败", JOptionPane.ERROR_MESSAGE);
            }
        });

        String t = getRoomType();
        String url = "";
        task.execute(url);
    }

    /**
     * 退出Channel或Group
     *
     * @param roomId
     */
    private void leaveChannelOrGroup(final String roomId)
    {
        HttpPostTask task = new HttpPostTask();
        task.addHeader("X-Auth-Token", currentUser.getAuthToken());
        task.addHeader("X-User-Id", currentUser.getUserId());
        task.addRequestParam("roomId", roomId);
        task.setListener(new HttpResponseListener<JSONObject>()
        {
            @Override
            public void onSuccess(JSONObject retJson)
            {
                if (retJson.has("success"))
                {
                    RightPanel.getContext().showPanel(RightPanel.TIP);
                    TitlePanel.getContext().showAppTitle();
                    RoomMembersPanel.getContext().setVisible(false);
                }
                else
                {
                    JOptionPane.showMessageDialog(MainFrame.getContext(), "退出失败", "退出失败", JOptionPane.ERROR_MESSAGE);
                }
            }

            @Override
            public void onFailed(Object why)
            {
                JOptionPane.showMessageDialog(null, "网络请求失败", "网络请求失败", JOptionPane.ERROR_MESSAGE);
            }
        });

        String t = getRoomType();
        String url = "";
        task.execute(url);
    }

}

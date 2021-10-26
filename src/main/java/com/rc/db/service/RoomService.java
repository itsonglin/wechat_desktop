package com.rc.db.service;

import com.rc.db.dao.RoomDao;
import com.rc.db.model.Room;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Created by song on 08/06/2017.
 */
public class RoomService extends BasicService<RoomDao, Room>
{
    public RoomService(SqlSession session)
    {
        dao = new RoomDao(session);
        super.setDao(dao);
    }

    public int insertOrUpdate(Room room)
    {
        if (exist(room.getRoomId()))
        {
            return update(room);
        }else
        {
            room.setShowNotify(true);
            return insert(room);
        }
    }

    public Room findRelativeRoomIdByUserId(String userId)
    {
        return dao.findRelativeRoomIdByUserId(userId);
    }

    public Room findByName(String name)
    {
        List list = dao.find("name", name);
        if (list.size() > 0)
        {
            return (Room) list.get(0);
        }
        return null;
    }

    public List<Room> searchByName(String name)
    {
        return dao.searchByName(name);
    }

    public int countUnHidden()
    {
        return dao.countUnHidden();
    }

    /**
     * 统计总未读消息数(只统计showNotify为true的room)
     * @return
     */
    public int totalUnreadCount()
    {
        return dao.totalUnreadCount();
    }
}

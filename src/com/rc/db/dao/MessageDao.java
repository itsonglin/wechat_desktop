package com.rc.db.dao;

import com.rc.db.model.Message;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by song on 09/06/2017.
 */
public class MessageDao extends BasicDao
{
    public MessageDao(SqlSession session)
    {
        super(session, MessageDao.class);
    }

    public Message findLastMessage(String roomId)
    {
        return (Message) session.selectOne("findLastMessage", roomId);
    }

    public int deleteByRoomId(String roomId)
    {
        return session.delete("deleteByRoomId", roomId);
    }

    public int countByRoom(String roomId)
    {
        return (int) session.selectOne("countByRoom", roomId);
    }

    public List<Message> findByPage(String roomId, int page, int pageLength)
    {
        page = page < 1 ? 1 : page;
        Map map = new HashMap();
        map.put("roomId", "'" + roomId + "'");
        map.put("offset", (page - 1) * pageLength);
        map.put("pageLength", pageLength);
        return session.selectList("findByPage", map);
    }

    public long findLastMessageTime(String roomId)
    {
        Object count = session.selectOne("findLastMessageTime", roomId);
        return count == null ? -1 : (long) count;
    }

    public int insertAll(List<Message> list)
    {
        int count = session.insert("insertAll", list);
        session.commit();
        return count;
    }

    public List<Message> findBetween(String roomId, long start, long end)
    {
        Map map = new HashMap();
        map.put("roomId", "'" + roomId + "'");
        map.put("start", start);
        map.put("end", end);
        return session.selectList("findBetween", map);
    }

    public long findFirstMessageTime(String roomId)
    {
        Object count = session.selectOne("findFirstMessageTime", roomId);
        return count == null ? -1 : (long) count;
    }

    public List<Message> findOffset(String roomId, int offset, int pageLength)
    {
        Map map = new HashMap();
        map.put("roomId", "'" + roomId + "'");
        map.put("offset",offset);
        map.put("pageLength", pageLength);
        return session.selectList("findByPage", map);
    }

    public int updateNeedToResend(String id, boolean status)
    {
        Map map = new HashMap();
        map.put("id", "'" + id + "'");
        map.put("status","'" + status + "'");
        return session.update("updateNeedToResend", map);
    }

    public int updateProgress(String id, int progress)
    {
        Map map = new HashMap();
        map.put("id", "'" + id + "'");
        map.put("progress", progress);
        return session.update("updateProgress", map);
    }

    public List<Message> search(String key)
    {
        Map map = new HashMap();
        map.put("condition", "'%" + key + "%'");
        return session.selectList(MessageDao.class.getName() + ".search", map);
    }

    public int markDeleted(String id)
    {
        return session.update("markDeleted", id);
    }
}

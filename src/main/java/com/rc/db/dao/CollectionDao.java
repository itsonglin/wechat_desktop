package com.rc.db.dao;

import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by song on 2019-11-14.
 */
public class CollectionDao extends BasicDao
{
    public CollectionDao(SqlSession session)
    {
        super(session, CollectionDao.class);
    }

    public int countUnreadCollection()
    {
        return (int) session.selectOne("countUnreadCollection");
    }

    public void deleteAllBug()
    {
        session.delete("deleteAllBug");
    }

    public void deleteAllTask()
    {
        session.delete("deleteAllTask");
    }

    public void deleteBugsExcept(List<String> exceptIds)
    {
        Map map = new HashMap();
        map.put("exceptIds", exceptIds);
        session.delete("deleteBugsExcept", map);
    }

    public void deleteTasksExcept(List<String> exceptIds)
    {
        Map map = new HashMap();
        map.put("exceptIds", exceptIds);
        session.delete("deleteTasksExcept", map);
    }
}

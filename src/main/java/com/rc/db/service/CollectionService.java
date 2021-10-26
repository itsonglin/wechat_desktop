package com.rc.db.service;

import com.rc.db.dao.CollectionDao;
import com.rc.db.model.Collection;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Created by song on 2019-11-14.
 */
public class CollectionService extends BasicService<CollectionDao, Collection>
{
    public CollectionService(SqlSession session)
    {
        dao = new CollectionDao(session);
        super.setDao(dao);
    }

    public int insertOrUpdate(Collection collection)
    {
        if (exist(collection.getId()))
        {
            return update(collection);
        } else
        {
            return insert(collection);
        }
    }

    public Collection findByName(String name)
    {
        List list = dao.find("name", name);
        if (list.size() > 0)
        {
            return (Collection) list.get(0);
        }
        return null;
    }

    public void setCollectionRead(String id)
    {
        Collection task = findById(id);
        if (task != null)
        {
            task.setRead(true);

            update(task);
        }
    }

    /**
     * 统计未读的集合数, 包括task与bug
     *
     * @return
     */
    public int countUnreadCollection()
    {
        return dao.countUnreadCollection();
    }

    public void deleteAllBug()
    {
        dao.deleteAllBug();
    }

    public void deleteAllTask()
    {
        dao.deleteAllTask();
    }

    public void deleteBugsExcept(List<String> exceptIds)
    {
        dao.deleteBugsExcept(exceptIds);
    }

    public void deleteTasksExcept(List<String> exceptIds)
    {
        dao.deleteTasksExcept(exceptIds);
    }
}


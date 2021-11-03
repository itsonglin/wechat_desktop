package com.rc.db.service;

import com.rc.db.dao.CollectionsDao;
import com.rc.db.model.Collections;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Created by song on 2019-11-14.
 */
public class CollectionService extends BasicService<CollectionsDao, Collections>
{
    public CollectionService(SqlSession session)
    {
        dao = new CollectionsDao(session);
        super.setDao(dao);
    }

    public int insertOrUpdate(Collections collection)
    {
        if (exist(collection.getId()))
        {
            return update(collection);
        } else
        {
            return insert(collection);
        }
    }

    public Collections findByName(String name)
    {
        List list = dao.find("name", name);
        if (list.size() > 0)
        {
            return (Collections) list.get(0);
        }
        return null;
    }
}


package com.rc.db.dao;

import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by song on 2019-11-14.
 */
public class CollectionsDao extends BasicDao
{
    public CollectionsDao(SqlSession session)
    {
        super(session, CollectionsDao.class);
    }
}

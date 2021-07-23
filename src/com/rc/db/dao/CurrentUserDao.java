package com.rc.db.dao;

import com.rc.db.model.CurrentUser;
import com.rc.db.service.CurrentUserService;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Created by song on 08/06/2017.
 */
public  class CurrentUserDao extends BasicDao
{
    public CurrentUserDao(SqlSession session)
    {
        super(session, CurrentUserDao.class);
    }
}
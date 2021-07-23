package com.rc.db.service;

import com.rc.db.dao.CurrentUserDao;
import com.rc.db.model.CurrentUser;
import com.rc.utils.DbUtils;
import org.apache.ibatis.session.SqlSession;
import org.sqlite.core.DB;

import java.util.List;

/**
 * Created by song on 08/06/2017.
 */
public class CurrentUserService extends BasicService<CurrentUserDao, CurrentUser>
{
    public CurrentUserService(SqlSession session)
    {
        dao = new CurrentUserDao(session);
        super.setDao(dao);
    }

    public int insertOrUpdate(CurrentUser currentUser)
    {
        if (exist(currentUser.getUserId()))
        {
            return update(currentUser);
        }else
        {
            return insert(currentUser);
        }
    }
}

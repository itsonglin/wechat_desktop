package com.rc.db.dao;

import com.rc.db.model.ContactsUser;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by song on 09/06/2017.
 */
public class ContactsUserDao extends BasicDao
{
    public ContactsUserDao(SqlSession session)
    {
        super(session, ContactsUserDao.class);
    }

    public int deleteByUsername(String username)
    {
        return session.delete("deleteByUsername", username);
    }

    public List<ContactsUser> searchByUsernameOrName(String username, String name)
    {
        Map map = new HashMap();
        map.put("usernameCondition", "'%" + username + "%'");
        map.put("nameCondition", "'%" + name + "%'");
        return session.selectList("searchByUsernameOrName", map);
    }
}

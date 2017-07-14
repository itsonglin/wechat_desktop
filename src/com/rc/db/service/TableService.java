package com.rc.db.service;

import com.rc.db.dao.TableDao;
import org.apache.ibatis.session.SqlSession;

/**
 * Created by song on 08/06/2017.
 */
public class TableService
{
    private TableDao dao;

    public TableService(SqlSession session)
    {
        dao = new TableDao(session);
    }

    public void createCurrentUserTable()
    {
        dao.createCurrentUserTable();
    }

    public boolean exist(String name)
    {
        return dao.exist(name);
    }

    public void createRoomTable()
    {
        dao.createRoomTable();
    }

    public void createMessageTable()
    {
        dao.createMessageTable();
    }

    public void createFileAttachmentTable()
    {
        dao.createFileAttachmentTable();
    }

    public void createImageAttachmentTable()
    {
        dao.createImageAttachmentTable();
    }

    public void createContactsUserTable()
    {
        dao.createContactsUserTable();
    }
}

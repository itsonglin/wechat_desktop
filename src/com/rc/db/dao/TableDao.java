package com.rc.db.dao;

import org.apache.ibatis.session.SqlSession;

/**
 * Created by song on 08/06/2017.
 */
public class TableDao
{
    private SqlSession session;

    public TableDao(SqlSession session)
    {
        this.session = session;
    }

    public void createCurrentUserTable()
    {
        session.update("createCurrentUserTable");
    }

    public boolean exist(String name)
    {
        return ((int) session.selectOne("tableExist", name)) > 0;
    }

    public void createRoomTable()
    {
        session.update("createRoomTable");
    }

    public void createMessageTable()
    {
        session.update("createMessageTable");
    }

    public void createFileAttachmentTable()
    {
        session.update("createFileAttachmentTable");
    }

    public void createImageAttachmentTable()
    {
        session.update("createImageAttachmentTable");
    }

    public void createContactsUserTable()
    {
        session.update("createContactsUserTable");
    }
}

package com.rc.db.dao;

import org.apache.ibatis.session.SqlSession;

/**
 * Created by song on 09/06/2017.
 */
public class ImageAttachmentDao extends BasicDao
{
    public ImageAttachmentDao(SqlSession session)
    {
        super(session, ImageAttachmentDao.class);
    }
}

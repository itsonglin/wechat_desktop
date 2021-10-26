package com.rc.db.dao;

import com.rc.db.model.FileAttachment;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by song on 09/06/2017.
 */
public class FileAttachmentDao extends BasicDao
{
    public FileAttachmentDao(SqlSession session)
    {
        super(session, FileAttachmentDao.class);
    }

    public List<FileAttachment> search(String key)
    {
        Map map = new HashMap();
        map.put("condition", "'%" + key + "%'");
        return session.selectList(FileAttachmentDao.class.getName() + ".search", map);
    }
}

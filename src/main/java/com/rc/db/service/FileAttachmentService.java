package com.rc.db.service;

import com.rc.db.dao.FileAttachmentDao;
import com.rc.db.model.FileAttachment;
import com.rc.db.model.Message;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Created by song on 08/06/2017.
 */
public class FileAttachmentService extends BasicService<FileAttachmentDao, FileAttachment>
{
    public FileAttachmentService(SqlSession session)
    {
        dao = new FileAttachmentDao(session);
        super.setDao(dao);
    }

    public int insertOrUpdate(FileAttachment attachment)
    {
        if (exist(attachment.getId()))
        {
            return update(attachment);
        }else
        {
            return insert(attachment);
        }
    }

    public List<FileAttachment> search(String key)
    {
        return dao.search(key);
    }
}

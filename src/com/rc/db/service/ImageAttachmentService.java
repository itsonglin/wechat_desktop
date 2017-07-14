package com.rc.db.service;

import com.rc.db.dao.ImageAttachmentDao;
import com.rc.db.model.ImageAttachment;
import org.apache.ibatis.session.SqlSession;

/**
 * Created by song on 08/06/2017.
 */
public class ImageAttachmentService extends BasicService<ImageAttachmentDao, ImageAttachment>
{
    public ImageAttachmentService(SqlSession session)
    {
        dao = new ImageAttachmentDao(session);
        super.setDao(dao);
    }

    public int insertOrUpdate(ImageAttachment attachment)
    {
        if (exist(attachment.getId()))
        {
            return update(attachment);
        }else
        {
            return insert(attachment);
        }
    }

}

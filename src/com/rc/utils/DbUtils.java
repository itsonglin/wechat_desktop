package com.rc.utils;

import com.rc.db.service.TableService;
import com.sun.deploy.security.SessionCertStore;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

public class DbUtils
{
    private static SqlSession sqlSession = null;

    static
    {
		/*try {
			Reader reader = Resources.getResourceAsReader("mybatis.xml");
			SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);
			sqlSession = sqlMapper.openSession(true);
			reader.close();

			checkTable();

		} catch (IOException e) {
			e.printStackTrace();
		}*/

        sqlSession = getSqlSession();
        checkTable();
    }

    private DbUtils()
    {

    }

    public static SqlSession getSqlSession()
    {
        try
        {
            Reader reader = Resources.getResourceAsReader("mybatis.xml");
            SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);
            SqlSession sqlSession = sqlMapper.openSession(true);
            reader.close();
            return sqlSession;
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private static void checkTable()
    {
        SqlSession session = DbUtils.getSqlSession();

        TableService tableService = new TableService(session);
        if (!tableService.exist("current_user"))
        {
            System.out.println("创建表 current_user");
            tableService.createCurrentUserTable();
        }
        if (!tableService.exist("room"))
        {
            System.out.println("创建表 room");
            tableService.createRoomTable();
        }
        if (!tableService.exist("message"))
        {
            System.out.println("创建表 message");
            tableService.createMessageTable();
        }
        if (!tableService.exist("file_attachment"))
        {
            System.out.println("创建表 file_attachment");
            tableService.createFileAttachmentTable();
        }
        if (!tableService.exist("image_attachment"))
        {
            System.out.println("创建表 image_attachment");
            tableService.createImageAttachmentTable();
        }
        if (!tableService.exist("contacts_user"))
        {
            System.out.println("创建表 contacts_user");
            tableService.createContactsUserTable();
        }

        if (!tableService.exist("collection"))
        {
            System.out.println("创建表 collection");
            tableService.createCollectionTable();
        }

    }
}

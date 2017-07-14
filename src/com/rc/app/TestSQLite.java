package com.rc.app;

import com.rc.db.model.CurrentUser;
import com.rc.db.service.CurrentUserService;
import com.rc.db.service.TableService;
import com.rc.utils.DbUtils;
import org.apache.ibatis.session.SqlSession;

import java.awt.*;
import java.sql.*;

/**
 * Created by song on 08/06/2017.
 */
public class TestSQLite
{
    public static void main(String[] args)
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

        //CurrentUser currentUser = new CurrentUser("3", "3", "3", "3", "3", "3", "3", "3", "3");
        //CurrentUserService currentUserService = new CurrentUserService(session);
        //currentUserService.insert(currentUser);
        //System.out.println(currentUserService.findAll());




        /*try
        {
            //连接SQLite的JDBC

            Class.forName("org.sqlite.JDBC");

            Connection conn = DriverManager.getConnection("jdbc:sqlite:rocketchat.db");

            Statement stat = conn.createStatement();

            //stat.executeUpdate("create table current_user(userId varchar(20), username varchar(20), authToken varchar(64),password varchar(20),rawPassword varchar(64),expireDate varchar(20),realName varchar(20),bcrypt varchar(64),avatarOrigin varchar(1024));");

            exits(conn, "current_user");
            conn.close(); //结束数据库的连接

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/
    }

    public static boolean exits(Connection conn, String table) throws SQLException
    {
        String sql = "select count(1) from sqlite_master where type = 'table' and name = '"+table+"'";
        Statement stat = conn.createStatement();

        ResultSet rs = stat.executeQuery(sql);
        while (rs.next())
        {
            System.out.println(rs.getInt(1));
        }

        return false;
    }
}
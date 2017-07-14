package com.rc.app;

import com.rc.db.service.*;
import com.rc.frames.LoginFrame;
import com.rc.frames.MainFrame;
import com.rc.tasks.HttpGetTask;
import com.rc.tasks.HttpResponseListener;
import com.rc.utils.DbUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * Created by song on 09/06/2017.
 */
public class Launcher
{
    private static Launcher context;

    public static SqlSession sqlSession;
    public static RoomService roomService;
    public static CurrentUserService currentUserService;
    public static MessageService messageService;
    public static ContactsUserService contactsUserService;
    public static ImageAttachmentService imageAttachmentService;
    public static FileAttachmentService fileAttachmentService;

    public static final String HOSTNAME = "http://www.baidu.com";

    public static final String APP_VERSION = "1.0.0";

    public static String userHome;
    public static String appFilesBasePath;


    static
    {
        sqlSession = DbUtils.getSqlSession();
        roomService = new RoomService(sqlSession);
        currentUserService = new CurrentUserService(sqlSession);
        messageService = new MessageService(sqlSession);
        contactsUserService = new ContactsUserService(sqlSession);
        imageAttachmentService = new ImageAttachmentService(sqlSession);
        fileAttachmentService = new FileAttachmentService(sqlSession);
    }

    private JFrame currentFrame;


    public Launcher()
    {
        context = this;
    }

    public void launch()
    {
        config();

        if (!isApplicationRunning())
        {
            openFrame();
        }
        else
        {
            System.exit(-1);
        }
    }


    private void openFrame()
    {
        // 原来登录过
        if (checkLoginInfo())
        {
            currentFrame = new MainFrame();
        }
        // 从未登录过
        else
        {
            currentFrame = new LoginFrame();
            currentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        currentFrame.setVisible(true);
    }

    private void config()
    {
        userHome = System.getProperty("user.home");

        appFilesBasePath = userHome + System.getProperty("file.separator") + "wechat";
    }

    /**
     * 检查是否有登录信息
     * @return
     */
    private boolean checkLoginInfo()
    {
        // TODO 判断是否已登录的逻辑
        return false;
    }

    /**
     * 通过文件锁来判断程序是否正在运行
     *
     * @return 如果正在运行返回true，否则返回false
     */
    private static boolean isApplicationRunning()
    {
        boolean rv = false;
        try
        {
            String path = appFilesBasePath + System.getProperty("file.separator") + "appLock";
            File dir = new File(path);
            if (!dir.exists())
            {
                dir.mkdirs();
            }

            File lockFile = new File(path + System.getProperty("file.separator") + "appLaunch.lock");
            if (!lockFile.exists())
            {
                lockFile.createNewFile();
            }

            //程序名称
            RandomAccessFile fis = new RandomAccessFile(lockFile.getAbsolutePath(), "rw");
            FileChannel fileChannel = fis.getChannel();
            FileLock fileLock = fileChannel.tryLock();
            if (fileLock == null)
            {
                System.out.println("程序已在运行.");
                rv = true;
            }
        }
        catch (FileNotFoundException e1)
        {
            e1.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return rv;
    }

    public void reLogin(String username)
    {
        MainFrame.getContext().setVisible(false);
        MainFrame.getContext().dispose();

        currentFrame = new LoginFrame(username);
        currentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        currentFrame.setVisible(true);
    }

    public static Launcher getContext()
    {
        return context;
    }


}

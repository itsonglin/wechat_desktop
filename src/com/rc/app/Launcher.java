package com.rc.app;

import com.rc.db.model.CurrentUser;
import com.rc.db.service.*;
import com.rc.frames.LoginFrame;
import com.rc.frames.MainFrame;
import com.rc.utils.DbUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.lf5.util.StreamUtils;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static CollectionService collectionService;

    public static final String APP_VERSION = "2.0";


    public static String userHome;
    public static String appFilesBasePath;

    public static Map<Integer, KeyStroke> hotKeyMap;


    /**
     * 截取屏幕
     */
    public static final int HOT_KEY_SCREEN_SHOT = 1;


    static
    {
        sqlSession = DbUtils.getSqlSession();
        roomService = new RoomService(sqlSession);
        currentUserService = new CurrentUserService(sqlSession);
        messageService = new MessageService(sqlSession);
        contactsUserService = new ContactsUserService(sqlSession);
        imageAttachmentService = new ImageAttachmentService(sqlSession);
        fileAttachmentService = new FileAttachmentService(sqlSession);
        collectionService = new CollectionService(sqlSession);

        hotKeyMap = new HashMap<>();
        hotKeyMap.put(HOT_KEY_SCREEN_SHOT, KeyStroke.getKeyStroke('A', InputEvent.CTRL_DOWN_MASK|InputEvent.ALT_DOWN_MASK));

    }

    private JFrame currentFrame;
    public static CurrentUser currentUser;


    public Launcher()
    {
        context = this;
    }

    public void launch()
    {
        config();

        //演示时使用,复制头像等资源信息
        copyDemoResource();


        if (!isApplicationRunning())
        {
            openFrame();
        }
        else
        {
            System.exit(-1);
        }

        copyLib();
    }


    private void copyDemoResource()
    {
        File dir = new File(appFilesBasePath, "/cache/avatar/custom");
        if (!dir.exists())
        {
            dir.mkdirs();
        }

        copyRes("admin", dir);
        copyRes("Android", dir);
        copyRes("Apple", dir);
        copyRes("Centos", dir);
        copyRes("Github", dir);
        copyRes("HarmonyOS", dir);
        copyRes("Oracle", dir);
        copyRes("Redhat", dir);
        copyRes("song", dir);
        copyRes("Ubuntu", dir);
        copyRes("文件传输助手", dir);
    }

    private void copyRes(String s, File destDir)
    {
        try (InputStream in = getClass().getResourceAsStream("/custom_avatar/" + s + ".png");
             FileOutputStream fos = new FileOutputStream(new File(destDir, s + ".png")))
        {
            StreamUtils.copy(in, fos);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void copyLib()
    {
        try(InputStream inputStream = getClass().getResourceAsStream("/libs/NsUserNotificationsBridge.dylib"))
        {

            File libDir = new File(appFilesBasePath + File.separator + "libs");
            if (!libDir.exists())
            {
                System.out.println("创建目录：" + libDir.getAbsolutePath());
                libDir.mkdirs();
            }

            File destDylibFile = new File(libDir, "NsUserNotificationsBridge.dylib");
            if (!destDylibFile.exists())
            {

                FileOutputStream outputStream = new FileOutputStream(destDylibFile);
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = inputStream.read(buffer)) > -1)
                {
                    outputStream.write(buffer, 0, len);
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    private void openFrame()
    {
        // 原来登录过
        if (checkLoginInfo())
        {
            currentFrame = new MainFrame();
            currentUser = currentUserService.findAll().get(0);
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

        appFilesBasePath = userHome + System.getProperty("file.separator") + "wechat_desktop";
    }

    private boolean checkLoginInfo()
    {
        List list = currentUserService.findAll();
        return  list.size() > 0;
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

package com.rc.app;

import com.rc.db.service.TableService;
import com.rc.utils.DbUtils;
import org.apache.ibatis.session.SqlSession;

/**
 * Created by song on 17-5-28.
 */
public class App
{
    public static void main(String[] args)
    {
        Launcher launcher = new Launcher();
        launcher.launch();
    }

}

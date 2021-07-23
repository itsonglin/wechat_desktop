package com.rc.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by song on 28/04/2017.
 */

public class TimeUtil
{
    private static final SimpleDateFormat daySimpleDateFormat = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat monthSimpleDateFormat = new SimpleDateFormat("MM/dd");
    private static final SimpleDateFormat yearSimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

    private static final Calendar calendar = Calendar.getInstance();

    public static boolean inTheSameMinute(long ts1, long ts2)
    {
        calendar.setTimeInMillis(ts1);
        int min1 = calendar.get(Calendar.MINUTE);

        calendar.setTimeInMillis(ts2);
        int min2 = calendar.get(Calendar.MINUTE);

        return min1 == min2;
    }

    public static String diff(long timestamp)
    {
        return diff(timestamp, false);
    }

    public static String diff(long timestamp, boolean detail)
    {
        long current = System.currentTimeMillis();
        calendar.setTimeInMillis(current);
        int today = calendar.get(Calendar.DAY_OF_YEAR);
        int thisYear = calendar.get(Calendar.YEAR);

        calendar.setTimeInMillis(timestamp);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);

        int diff = today - day;
        boolean sameYear = thisYear == year;

        String ret;
        if (sameYear && diff < 1)
        {
            ret = daySimpleDateFormat.format(new Date(timestamp));
        }
        else if (sameYear && diff < 2)
        {
            if (detail)
            {
                ret = "昨天 " + daySimpleDateFormat.format(new Date(timestamp));
            }
            else
            {
                ret = "昨天"/* + daySimpleDateFormat.format(new Date(timestamp))*/;
            }
        }
        else if (sameYear && diff < 8)
        {
            if (detail)
            {
                ret = "星期" + getWeekDay(calendar.get(Calendar.DAY_OF_WEEK)) + " " + daySimpleDateFormat.format(new Date(timestamp));
            }
            else
            {
                ret = "星期" + getWeekDay(calendar.get(Calendar.DAY_OF_WEEK))/* + " " + daySimpleDateFormat.format(new Date(timestamp))*/;
            }
        }
        else if (sameYear && diff < 366)
        {
            if (detail)
            {
                ret = monthSimpleDateFormat.format(new Date(timestamp)) + " " + daySimpleDateFormat.format(new Date(timestamp));
            }
            else
            {
                ret = monthSimpleDateFormat.format(new Date(timestamp));
            }
        }
        else
        {
            ret = yearSimpleDateFormat.format(new Date(timestamp));
        }
        return ret;
    }



    private static String getWeekDay(int val)
    {
        switch (val)
        {
            case 1:
                return "日";
            case 2:
                return "一";
            case 3:
                return "二";
            case 4:
                return "三";
            case 5:
                return "四";
            case 6:
                return "五";
            case 7:
                return "六";
            default:
                return "";
        }
    }
}

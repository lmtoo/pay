package com.somnus.pay.payment.util;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * 时间处理
 * @author Wiley
 * @version v1.0
 */
public class DateUtils implements Serializable {

    private static final long  serialVersionUID   = 6622278926579307357L;

    public static final String DATE_STR_FORMATTER = "yyyy-MM-dd";

    public static final String DATE_FULL_FORMATTER = "yyyyMMddHHmmss";

    public static final String DATE_FORMATTER_YYYYMMDD = "yyyyMMdd";

    public static final int    DAY_SECONDS        = 24 * 60 * 60;

    public static String getToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date());
    }

    public static String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssMs");
        return sdf.format(new Date());
    }

    public static String getDateTime2() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmssMs");
        return sdf.format(new Date());
    }

    public static synchronized String getInviteCodeByNowDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmssMs");
        String dateStr = sdf.format(new Date());
        return getRandomInteger() + dateStr;
    }

    public static String getRandomInteger() {
        int temp = (int) (Math.random() * 9999);
        return String.valueOf(temp);
    }

    public static Date String2Date(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        return String2Date(dateStr, "yyyyMMdd");
    }

    public static String Date2String(Date date) {
        if (null == date) {
            return "";
        }
        return Date2String(date, "yyyy-MM-dd");
    }

    public static Date String2Date(String dateStr, String DateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String Date2String(Date date, String DateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
        try {
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 获取离截止时间的剩余时间
     * @param deadline 截止时间,格式yyyyMMddHHmmss
     * @return
     */
    public static String getRemainingTime(String endDate) {
        String rtn = "";
        if (StringUtils.isBlank(endDate))
            return rtn;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); // 存在线程安全问题，故不能使用静态
        try {
            Date deadline = sdf.parse(endDate);
            long remaining = deadline.getTime() - System.currentTimeMillis();
            if (remaining > 0) {
                remaining /= 1000;
                remaining /= 60;
                int mn = (int) (remaining % 60);
                remaining /= 60;
                int hr = (int) (remaining % 24);
                long dy = (int) remaining / 24;
                rtn = dy + "天" + hr + "小时" + mn + "分";//+ sc + "秒";
            } else {
                rtn = "过期";
            }
        } catch (ParseException e) {

        }
        return rtn;
    }

    /**
     * 根据当天日期 格式 2013-05-31
     * @return
     */
    public static String getCurrentDay() {
        return getBeforeOrAfterDay(0);
    }

    /**
     * 根据当前时间获取后一天日期 格式 2013-05-31
     * @return
     */
    public static String getAfterToday() {
        return getBeforeOrAfterDay(1);
    }

    /**
     * 根据date 获取后一天日期 格式 2013-05-31
     * @param date 
     * @return
     */
    public static String getAfterToday(Date date) {
        return getBeforeOrAfterDay(date, 1);
    }

    /**
     * 根据当前时间获取后一周前的日期 格式 2013-05-31
     * @return
     */
    public static String getOneWeekBefore() {
        return getBeforeOrAfterDay(-7);
    }

    /**
     * 设置当前日期的 前 或者 后 daycount 天
     * @param daycount
     * @return
     */
    public static String getBeforeOrAfterDay(int daycount) {
        Date dayBefore = new Date();
        if (daycount != 0) {
            Calendar calendar = Calendar.getInstance(); // 得到日历
            calendar.setTime(new Date()); // 把当前时间赋给日历
            calendar.add(Calendar.DAY_OF_MONTH, daycount); // 设置为  当前 + dayCount
            dayBefore = calendar.getTime(); // 得到当前 + dayCount 的时间
        }
        return Date2String(dayBefore);
    }

    public static String getBeforeOrAfterDay(Date date, int daycount) {
        if (daycount != 0) {
            Calendar calendar = Calendar.getInstance(); // 得到日历
            calendar.setTime(date);// 把当前时间赋给日历
            calendar.add(Calendar.DAY_OF_MONTH, daycount); // 设置为  当前 + dayCount
            date = calendar.getTime(); // 得到当前 + dayCount 的时间
        }
        return Date2String(date);
    }

    public static Date getBeforeOrAfterDayToDate(Date date, int daycount) {
        Calendar calendar = null;
        if (daycount != 0) {
            calendar = Calendar.getInstance(); // 得到日历
            calendar.setTime(date);// 把当前时间赋给日历
            calendar.add(Calendar.DAY_OF_MONTH, daycount); // 设置为  当前 + dayCount
        }
        return calendar.getTime();
    }

    /**
     * 计算 当前一天 还剩余 多少秒
     * @return int
     */
    @SuppressWarnings("deprecation")
    public static int remainSecondsInToday() {
        int remainSeconds = DAY_SECONDS;
        Date date = new Date();
        int currentTimeSendcods = date.getHours() * 3600 + date.getMinutes() * 60
                                  + date.getSeconds();
        remainSeconds = DAY_SECONDS - currentTimeSendcods;
        return remainSeconds;
    }

    public static Date getBeforeOrAfterMinute(Date date, int minutecount) {
        if (minutecount != 0) {
            Calendar calendar = Calendar.getInstance(); // 得到日历
            calendar.setTime(date); // 把当前时间赋给日历
            calendar.add(Calendar.MINUTE, minutecount); // 设置为  当前 + minutecount
            date = calendar.getTime(); // 得到当前 + dayCount 的时间
        }
        return date;
    }
    
    public static Date getBeforeOrAfterMonth(Date date, int monthcount) {
        if (monthcount != 0) {
            Calendar calendar = Calendar.getInstance(); // 得到日历
            calendar.setTime(date); // 把当前时间赋给日历
            calendar.add(Calendar.MONTH, monthcount); // 设置为  当前 + monthcount
            date = calendar.getTime(); // 得到当前 + dayCount 的时间
        }
        return date;
    }

}

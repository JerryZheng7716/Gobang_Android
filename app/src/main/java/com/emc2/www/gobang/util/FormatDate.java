package com.emc2.www.gobang.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FormatDate {
    public static String changeDate(String time)  {
        //格式化时间

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat("MM月dd日");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy年MM月dd日");
//        String[] strings = date.split("T");
//        String[] strings1 = strings[1].split("\\.");
//        date = strings[0] + " " + strings1[0];
        Date now;
        Date date= null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String result = "";
        long minutes, hour, days;
        now = new Date();
//            date = sdf.parse(date);
        //计算差值，分钟数
        minutes = (now.getTime() - date.getTime()) / (1000 * 60);
        //计算差值，小时数
        hour = (now.getTime() - date.getTime()) / (1000 * 60 * 60);
        //计算差值，天数
        days = (now.getTime() - date.getTime()) / (1000 * 60 * 60 * 24);
        if (minutes <= 1) {
            result = "刚刚";
        } else if (minutes <= 60) {
            result = minutes + "分钟前";
        } else if (hour <= 24) {
            result = hour + "小时前";
        } else if (days < 7) {
            result = days + "天前";
        } else if (date.getYear() == now.getYear()) {
            result = String.valueOf(sdf2.format(date));
        } else {
            result = String.valueOf(sdf3.format(date));
        }
        return result;
    }
}

package com.ruoyi.meeting.service;

import com.ruoyi.common.core.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class main {
    public static void main(String[] args) {
        Date now = DateUtils.getNowDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, -1);
        Date time = calendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        System.out.println("now: " + df.format(now));
        System.out.println("sub: " + df.format(time));
    }
}

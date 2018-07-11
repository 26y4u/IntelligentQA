package com.example.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GetTime {
    public static java.sql.Date getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss ");
        String nowTime = simpleDateFormat.format(new Date());
        java.sql.Date date = java.sql.Date.valueOf(nowTime);

        System.out.println(date);
        return date;
    }
}

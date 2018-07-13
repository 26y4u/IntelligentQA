package com.example.Utils;

import java.util.Date;

public class GetTime {
    public static java.sql.Date getTime() {
        java.sql.Date date =new java.sql.Date(new Date().getTime());
        System.out.println(date);
        return date;
    }
}

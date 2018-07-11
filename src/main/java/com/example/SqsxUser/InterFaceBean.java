package com.example.SqsxUser;

import javax.persistence.*;

public class InterFaceBean {
    private Integer id;
    private String username;
    private int hashpassword;
    private String newpassword;
    private String password;
    private Integer type;
    private String tag0;
    private String tag1;
    private String tag2;
    private Integer isdel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public int getHashpassword() {
        return hashpassword;
    }

    public int getHashPassword() {
        return hashpassword;
    }

    public void setHashPassword(int password) {
        this.hashpassword = hashpassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public String getTag0() {
        return tag0;
    }

    public void setTag0(String tag0) {
        this.tag0 = tag0;
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public InterFaceBean() {
    }

}

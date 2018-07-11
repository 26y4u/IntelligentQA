package com.example.file;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Date;

import com.example.Utils.*;
import com.example.SqsxUser.*;

@Entity
@Table(name="sqsx_upload")
public class UploadBean {
    @Id
    @GeneratedValue
    @NotNull
    @Column(name="id")
    private Integer id;

//    @JoinTable(name = "file", joinColumns = {@JoinColumn(name = "file_id", referencedColumnName = "file_id")},
//            inverseJoinColumns = {@JoinColumn(name = "id", referencedColumnName = "file_id")}
//            )
//
//    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(name = "id"))

@Column(name="user_id")
    private Integer user_id;
    //@ForeignKey()
    //@ForeignKey
    //@ManyToOne

    @Column(name="file_id")
    public Integer file_id;

    @Column(name="time")
    private Date time;

    @Column(name="down_num")
    private Integer down_num;

    @Column(name="isdel")
    Integer isdel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer type) {
        this.user_id = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getDown_num() {
        return down_num;
    }

    public void setDown_num(Integer down_num) {
        this.file_id = down_num;
    }

    public Integer getFile_id() {
        return file_id;
    }

    public void setFile_id(Integer file_id) {
        this.file_id = file_id;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public UploadBean() {
    }
}

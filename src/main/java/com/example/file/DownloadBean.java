package com.example.file;

import org.springframework.data.jpa.repository.Query;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name="sqsx_download")
public class DownloadBean {
    @Id
    @GeneratedValue
    @NotNull
    @Column(name="id")
    private Integer id;

    @Column(name="user_id")
    private Integer user_id;

    @Column(name="file_id")
    private Integer file_id;

    @Column(name="time")
    private Date time;

    @Column(name="isdel")
    private Integer isdel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getFile_id() {
        return file_id;
    }

    public void setFile_id(Integer file_id) {
        this.file_id = file_id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public DownloadBean() {
    }
}

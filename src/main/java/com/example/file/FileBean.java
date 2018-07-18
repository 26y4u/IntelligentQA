package com.example.file;

import org.springframework.data.jpa.repository.Query;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="sqsx_file")
public class FileBean  {
    @Id
    @GeneratedValue

    @Column(name="id")
    private Integer id;

    @Column(name="md5")
    private String md5;

    @Column(name="filename")
    private String filename;

    @Column(name="type")
    private String type;

    @Column(name="tag0")
    private String tag0;
    @Column(name="tag1")
    private String tag1;
    @Column(name="tag2")
    private String tag2;

    @Column(name="isdel")
    private Integer isdel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
    public String getFileName() {
        return filename;
    }

    public void setFileName(String filename){
        this.filename = filename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public FileBean() {
    }
}

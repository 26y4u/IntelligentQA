package com.example.file;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sqsx_file")
public class FileBean extends UploadBean {
    @Id
    @GeneratedValue

    private Integer id;

    private String md5;
    private Integer type_id;
    private Integer label_id;
    private Integer isdel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type_id;
    }

    public void setType(Integer type) {
        this.type_id = type;
    }

    public Integer getLabel_id() {
        return label_id;
    }

    public void setLabel_id(Integer label_id) {
        this.label_id = label_id;
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

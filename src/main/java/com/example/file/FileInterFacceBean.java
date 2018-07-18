package com.example.file;

import javax.persistence.Entity;


public class FileInterFacceBean {

    private Integer id;//资源id（数据库中文件的主码）
    private String title;//资源标题（可根据此完全匹配去数据库中查找文件）
    private Integer fileid;
    private String md5;
    private Integer downloads;//下载次数
    private String uploader;//上传者,用户名
    private String downloader;//下载者
    private String type;//资源类型(word ppt pdf 视频
    private String tag1;
    private String tag2;
    private String tag3;//资源分类
    private Integer isdel;
    private Integer urlType;
    private Integer sort;//排序0是时间，1是下载量

    public Integer getId() {
        return id;
    }

    public String getMd5() {
        return md5;
    }

    public Integer getFileid() {
        return fileid;
    }

    public String getTag3() {
        return tag3;
    }

    public String getTag1() {
        return tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public String getTitle() {
        return title;
    }

    public String getUploader() {
        return uploader;
    }

    public String getType() {
        return type;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public Integer getDownloads() {
        return downloads;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void setFileid(Integer fileid) {
        this.fileid = fileid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public void setDownloads(Integer downloads) {
        this.downloads = downloads;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTag3(String tag0) {
        this.tag3 = tag3;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public Integer getUrlType() {
        return urlType;
    }

    public void setUrlType(Integer urlType) {
        this.urlType = urlType;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getSort() {
        return sort;
    }

    public FileInterFacceBean(){ }
}

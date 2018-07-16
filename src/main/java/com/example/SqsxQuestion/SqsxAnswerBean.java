package com.example.SqsxQuestion;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "sqsx_answer")
public class SqsxAnswerBean implements Serializable {


    private static final long serialVersionUID = 8881L;

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "question_id")
    private Integer question_id;
    @Column(name="user_id")
    private Integer user_id;
    @Column(name="details")
    private String details;
    @Column(name="time")
    private Date time;
    @Column(name="count_like")
    private Integer count_like;
    @Column(name = "usertype")
    private Integer usertype;
    @Column(name="isdel")
    private Integer isdel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(Integer question_id) {
        this.question_id = question_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getCount_like() {
        return count_like;
    }

    public void setCount_like(Integer count_like) {
        this.count_like = count_like;
    }

    public Integer getUsertype() {
        return usertype;
    }

    public void setUsertype(Integer usertype) {
        this.usertype = usertype;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }
}

package com.example.SqsxQuestion;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "sqsx_question")
public class SqsxQuestionBean implements Serializable {


    private static final long serialVersionUID = 8881L;


    @Id
    @GeneratedValue
    private Integer id;

    @Column(name="user_id")
    private Integer user_id;

    @Column(name="title")
    private String title;

    @Column(name="description")
    private String description;

    @Column(name="time")
    private Date time;

    @Column(name="count_answer")
    private Integer count_answer;

    @Column(name="best_answer_id")
    private Integer best_answer_id;

    @Column(name="isclose")
    private Integer isclose;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getCount_answer() {
        return count_answer;
    }

    public void setCount_answer(Integer count_answer) {
        this.count_answer = count_answer;
    }

    public Integer getBest_answer_id() {
        return best_answer_id;
    }

    public void setBest_answer_id(Integer best_answer_id) {
        this.best_answer_id = best_answer_id;
    }

    public Integer getIsclose() {
        return isclose;
    }

    public void setIsclose(Integer isclose) {
        this.isclose = isclose;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public SqsxQuestionBean() {
    }


}

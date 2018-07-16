package com.example.SqsxQuestion;


import javax.persistence.*;

@Entity
@Table(name = "sqsx_like")
public class SqsxLikeBean {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "answer_id")
    private Integer answer_id;
    @Column(name = "user_id")
    private Integer user_id;
    @Column(name = "isdel")
    private Integer isdel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(Integer answer_id) {
        this.answer_id = answer_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }
}

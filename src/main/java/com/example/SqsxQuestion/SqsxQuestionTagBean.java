package com.example.SqsxQuestion;


import javax.persistence.*;

@Entity
@Table(name = "sqsx_question_label")
public class SqsxQuestionTagBean {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "question_id")
    private Integer question_id;
    @Column(name = "label")
    private String label;
    @Column(name = "isdel")
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }
}

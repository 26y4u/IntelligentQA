package com.example.SqsxQuestion;


public class QuestionInterfaceBean {



    private Integer questionId;
    private String questionTitle;
    private String questionDescription;
    private String questionLabel1;
    private String questionLabel2;
    private String questionLabel3;

    private Integer answerId;
    private String answerDetails;



    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionDescription() {
        return questionDescription;
    }

    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
    }

    public String getQuestionLabel1() {
        return questionLabel1;
    }

    public void setQuestionLabel1(String questionLabel1) {
        this.questionLabel1 = questionLabel1;
    }

    public String getQuestionLabel2() {
        return questionLabel2;
    }

    public void setQuestionLabel2(String questionLabel2) {
        this.questionLabel2 = questionLabel2;
    }

    public String getQuestionLabel3() {
        return questionLabel3;
    }

    public void setQuestionLabel3(String questionLabel3) {
        this.questionLabel3 = questionLabel3;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getAnswerDetails() {
        return answerDetails;
    }

    public void setAnswerDetails(String answerDetails) {
        this.answerDetails = answerDetails;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }


    public QuestionInterfaceBean(){ }
}

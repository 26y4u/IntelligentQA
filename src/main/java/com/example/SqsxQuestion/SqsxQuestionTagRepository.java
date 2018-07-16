package com.example.SqsxQuestion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SqsxQuestionTagRepository extends JpaRepository<SqsxQuestionTagBean,Integer> {

    //按问题id查找
   // List<SqsxQuestionTagBean> findByQuestion_id(Integer questionid);
    @Query(value = "select * from sqsx_question_label where sqsx_question_label.question_id = ?1",nativeQuery = true)
    List<SqsxQuestionTagBean> selectedByQuestionid(Integer questionid);

    //匹配标签
    @Query(value = "select * from  sqsx_question_label",nativeQuery = true)
    List<SqsxQuestionTagBean> selectByLabel();
}

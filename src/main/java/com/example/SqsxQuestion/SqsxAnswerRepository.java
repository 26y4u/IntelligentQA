package com.example.SqsxQuestion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SqsxAnswerRepository extends JpaRepository<SqsxAnswerBean,Integer> {

    //按照回答id查找回答
    SqsxAnswerBean findById(Integer id);
    //按照回答内容details查找回答


    //按照问题id查找回答，并按照时间倒序排序
    @Query(value = "select * from sqsx_answer where sqsx_answer.isdel = 0 and sqsx_answer.question_id = ?1 order by sqsx_question.time desc",nativeQuery = true)
    List<SqsxAnswerBean> selectedByQuestionid(Integer question_id);

    //按照问题id查找回答，并按照点赞数量排序
    @Query(value = "select * from sqsx_answer where sqsx_answer.isdel = 0 and sqsx_answer.question_id = ?1 order by sqsx_question.count_like",nativeQuery = true)
    List<SqsxAnswerBean> selectedByQuestionidlike(Integer question_id);

    //按照问题title查找回答
    @Query(value = "select * from sqsx_answer join sqsx_question where sqsx_answer.isdel = 0 and sqsx_answer.question_id = sqsx_question.id and sqsx_question.title = ?1",nativeQuery = true)
    List<SqsxAnswerBean> selectedByQuestiontitle(String title);

    //按照回答者id查找回答
    @Query(value = "select * from sqsx_answer where sqsx_answer.isdel = 0 and sqsx_answer.user_id = ?1",nativeQuery = true)
    List<SqsxAnswerBean> selectedByUserid(Integer user_id);
  //  @Query(value = "select sqsx_answer.id,sqsx_answer.details from sqsx_answer where sqsx_answer.isdel = 0 and sqsx_answer.user_id = ?1",nativeQuery = true)
   // List<SqsxAnswerBean> selectedByUserid(Integer user_id);

}

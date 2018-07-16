

package com.example.SqsxQuestion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SqsxQuestionRepository extends JpaRepository<SqsxQuestionBean,Integer> {

    //按照问题id查找问题
    SqsxQuestionBean findById(Integer id);
    //按照问题标题查找问题
    SqsxQuestionBean findByTitle(String title);


     //@Query(value = "select sqsx_ask.question_id from sqsx_ask  join sqsx_question  where sqsx_ask.question_id = sqsx_question.id and sqsx_ask.user_id=?1",nativeQuery = true)

    //按照用户名查找该用户提出的全部问题，状态isdel = 0 并按时间倒序排序
    @Query(value = "select * from sqsx_question where sqsx_question.isdel = 0 and sqsx_question.user_id = ?1 order by sqsx_question.time desc ",nativeQuery = true)
    List<SqsxQuestionBean> selectedByUserid(Integer userid);

    //按照用户标签推荐问题，状态isclose = 0，isdel = 0
    @Query(value = "select * from sqsx_question join sqsx_question_label where sqsx_question.isclose = 0 and sqsx_question.isdel = 0 and sqsx_question.id = sqsx_question_label.question_id and sqsx_question_label.label = ?1 order by sqsx_question.time desc",nativeQuery = true)
    List<SqsxQuestionBean> selectByLabel(String label);
   // @Query(value = "select * from sqsx_question where sqsx_question.isclose = 0 and sqsx_question.isdel = 0 sqsx_question.tag0 = ?1 or sqsx_question.tag1 = ?1 or sqsx_question.tag2 = ?1 ",nativeQuery = true)
    //List<SqsxQuestionBean> selectedByTag(Integer tag);




}

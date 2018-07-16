package com.example.SqsxQuestion;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.SqsxUser.SqsxUser;
import com.example.SqsxUser.SqsxUserRepository;
import com.example.Utils.GetTime;
import com.example.Utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

//设置最佳回答 状态不可点赞 不可回答
//管理员 打不开链接

@RestController
@RequestMapping("/")
public class SqsxQuestionController {


    @Autowired
    SqsxUserRepository sqsxUserRepository;
    @Autowired
    SqsxQuestionRepository sqsxQuestionRepository;
    @Autowired
    SqsxAnswerRepository sqsxAnswerRepository;
    @Autowired
    SqsxLikeRepository sqsxLikeRepository;
    @Autowired
    SqsxQuestionTagRepository sqsxQuestionTagRepository;




    //问题搜索：
    @PostMapping("QAComm/search")
    public JsonResult search(@RequestBody QuestionInterfaceBean bean) {
        String questionTitle = bean.getQuestionTitle();
        return JsonResult.ok();
    }

    /*
     * 展示当前用户提问的所有问题
     *
     * */
    @PostMapping("profile/myQuestion")
    public JsonResult showQuestions(HttpServletRequest request) {

        SqsxUser sqsxUser = (SqsxUser) request.getSession().getAttribute("currentUser");
        if(sqsxUser.getType() != 0 && sqsxUser.getType()!=1)//检查权限
            return JsonResult.returnnull("权限不足");
        else {

            Integer userid = sqsxUser.getId();
            System.out.println(userid);
            if (sqsxQuestionRepository.selectedByUserid(userid) != null){
                List<SqsxQuestionBean> sqsxQuestionBeans = sqsxQuestionRepository.selectedByUserid(userid);
                List<Object> list = new ArrayList<Object>();
                JSONArray jsonArray = new JSONArray();
                for(int i  = 0;i<sqsxQuestionBeans.size();i++)
                {
                    if(sqsxQuestionBeans.get(i).getIsdel() == 1)
                    {
                        System.out.println("问题"+sqsxQuestionBeans.get(i).getId()+"已被删除");
                    }
                    else {
                        JSONObject jsonObject = new JSONObject();
                        Integer questionid = sqsxQuestionBeans.get(i).getId();
                        List<SqsxQuestionTagBean> list1 = sqsxQuestionTagRepository.selectedByQuestionid(questionid);
                        List<String> tags = new ArrayList<String >();
                        for(int j = 0;j<list1.size();j++){
                            tags.add(list1.get(j).getLabel());
                        }
                        list.add(sqsxQuestionBeans.get(i).getId());
                        jsonObject.put("id",sqsxQuestionBeans.get(i).getId());
                        jsonObject.put("title",sqsxQuestionBeans.get(i).getTitle());
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String time = simpleDateFormat.format(sqsxQuestionBeans.get(i).getTime());
                        java.sql.Date date = java.sql.Date.valueOf(time);
                        jsonObject.put("time",date);
                        jsonObject.put("count",sqsxQuestionBeans.get(i).getCount_answer());
                        jsonObject.put("status",sqsxQuestionBeans.get(i).getIsclose());
                        jsonObject.put("tags",tags);
                        jsonArray.add(jsonObject);
                    }
                }
                if (list.isEmpty()){
                    return JsonResult.returnnull("该用户没有提问记录");
                }
                else {
                    return JsonResult.ok(jsonArray);
                }
            }
            else return JsonResult.returnnull("该用户没有提问记录");
        }
    }


    /**
     * 用户提问问题
     **/
    @PostMapping("QAComm/quiz")
    public JsonResult quiz(@RequestBody QuestionInterfaceBean bean,HttpServletRequest request) {
        String title = bean.getQuestionTitle();
        String description = bean.getQuestionDescription();
        String label1 = bean.getQuestionLabel1();
        String label2 = bean.getQuestionLabel2();
        String label3 = bean.getQuestionLabel3();

            SqsxUser sqsxUser = (SqsxUser) request.getSession().getAttribute("currentUser");
        if((sqsxUser.getType() != 0) && sqsxUser.getType() != 1)
        {//检查权限:学生0，教师1
            //权限不足
            return JsonResult.refuse();
        }
        else {
            //添加问题
            Integer userid = sqsxUser.getId();
            SqsxQuestionBean sqsxQuestionBean = new SqsxQuestionBean();
            sqsxQuestionBean.setUser_id(userid);
            sqsxQuestionBean.setTitle(title);
            sqsxQuestionBean.setDescription(description);
            sqsxQuestionBean.setTime(GetTime.getTime());
            sqsxQuestionBean.setCount_answer(0);
            sqsxQuestionBean.setBest_answer_id(null);
            sqsxQuestionBean.setIsclose(0);
            sqsxQuestionBean.setIsdel(0);

            sqsxQuestionRepository.save(sqsxQuestionBean);
            Integer questionid = sqsxQuestionBean.getId();

            //添加label记录
            SqsxQuestionTagBean sqsxQuestionTagBean1 = new SqsxQuestionTagBean();
            sqsxQuestionTagBean1.setQuestion_id(questionid);
            sqsxQuestionTagBean1.setLabel(label1);
            sqsxQuestionTagBean1.setIsdel(0);
            sqsxQuestionTagRepository.save(sqsxQuestionTagBean1);
            SqsxQuestionTagBean sqsxQuestionTagBean2 = new SqsxQuestionTagBean();
            sqsxQuestionTagBean2.setQuestion_id(questionid);
            sqsxQuestionTagBean2.setLabel(label2);
            sqsxQuestionTagBean2.setIsdel(0);
            sqsxQuestionTagRepository.save(sqsxQuestionTagBean2);
            SqsxQuestionTagBean sqsxQuestionTagBean3 = new SqsxQuestionTagBean();
            sqsxQuestionTagBean3.setQuestion_id(questionid);
            sqsxQuestionTagBean3.setLabel(label3);
            sqsxQuestionTagBean3.setIsdel(0);
            sqsxQuestionTagRepository.save(sqsxQuestionTagBean3);


            JSONObject jsonObject = new JSONObject();
            List<String> tags = new ArrayList<String >();
            tags.add(label1);
            tags.add(label2);
            tags.add(label3);
            jsonObject.put("title",sqsxQuestionBean.getTitle());
            jsonObject.put("count",sqsxQuestionBean.getCount_answer());
            jsonObject.put("time",sqsxQuestionBean.getTime());
            jsonObject.put("tags",tags);
            return JsonResult.ok(jsonObject);
        }
    }

    //返回所有标签
    @PostMapping("QAComm/getTags")
    public JsonResult matchLabel(){
       // String label = bean.getQuestionLabel1();
        List<SqsxQuestionTagBean> sqsxQuestionTagBeans = sqsxQuestionTagRepository.selectByLabel();
        JSONObject jsonObject = new JSONObject();
        List<String> tags = new ArrayList<String >();
        for (int i = 0;i<sqsxQuestionTagBeans.size();i++)
            tags.add(sqsxQuestionTagBeans.get(i).getLabel());

        List newList = new ArrayList(new HashSet(tags));//去重
        return JsonResult.ok(newList);
    }

    //展示某个问题以及回答
    @PostMapping("QAComm/QA")
    public JsonResult quiz(@RequestBody QuestionInterfaceBean bean){
        String title = bean.getQuestionTitle();
        SqsxQuestionBean sqsxQuestionBean = sqsxQuestionRepository.findByTitle(title);

        if(sqsxQuestionBean==null)
        {
            return JsonResult.returnnull("该问题不存在");
        }
        else {
        if(sqsxQuestionBean.getIsclose() == 1||sqsxQuestionBean.getIsdel() == 1){
            return JsonResult.refuse();
        }
        else {

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("question:", sqsxQuestionBean);
            List<SqsxAnswerBean> sqsxAnswerBeans = sqsxAnswerRepository.selectedByQuestiontitle(title);
            jsonObject1.put("answers:", sqsxAnswerBeans);
            return JsonResult.ok(jsonObject1);
            }
        }
    }

    /**
    * 用户回答问题，提交答案
    **/
    @PostMapping("QAComm/reply")
    public JsonResult reply(@RequestBody QuestionInterfaceBean bean,HttpServletRequest request){

        String questionTitle = bean.getQuestionTitle();
        String details = bean.getAnswerDetails();
        SqsxUser sqsxUser = (SqsxUser) request.getSession().getAttribute("currentUser");
       if((sqsxUser.getType() != 0) && sqsxUser.getType() != 1)
       {//检查权限，0:学生 1:教师
           // 权限不足
           return JsonResult.refuse();
       }
       else {
           if((sqsxQuestionRepository.findByTitle(questionTitle).getIsclose() == 1)){//检查问题是否关闭
               // isclose = 1，问题关闭无法回答
               return JsonResult.refuse();
           }
           else {
                //添加回答
               Integer userid = sqsxUser.getId();
               System.out.println(userid);
               Integer question_id = sqsxQuestionRepository.findByTitle(questionTitle).getId();

               SqsxAnswerBean sqsxAnswerBean = new SqsxAnswerBean();
               sqsxAnswerBean.setUser_id(userid);
               sqsxAnswerBean.setQuestion_id(question_id);
               sqsxAnswerBean.setDetails(details);
               sqsxAnswerBean.setTime(GetTime.getTime());
               sqsxAnswerBean.setUsertype(sqsxUser.getType());
               sqsxAnswerBean.setCount_like(0);
               sqsxAnswerBean.setIsdel(0);

               sqsxAnswerRepository.save(sqsxAnswerBean);

               SqsxQuestionBean sqsxQuestionBean = sqsxQuestionRepository.findById(question_id);
               sqsxQuestionBean.setCount_answer(sqsxQuestionBean.getCount_answer() + 1);
               sqsxQuestionRepository.save(sqsxQuestionBean);

               return JsonResult.ok(sqsxAnswerBean);//提交成功
           }
       }
    }

    //没用
    @PostMapping("/findAnswerByQuestionTitle")
    public JsonResult findAnswerByQuestionTitle(@RequestBody QuestionInterfaceBean bean){
        String title = bean.getQuestionTitle();

        List<SqsxAnswerBean> sqsxAnswerBeans = sqsxAnswerRepository.selectedByQuestiontitle(title);
        return JsonResult.ok(sqsxAnswerBeans);
    }

    /**
    * 我的回答（没用）
    * */
    @PostMapping("/myAnswer")
    public JsonResult findAnswerByUserid(HttpServletRequest request){
        SqsxUser sqsxUser = (SqsxUser) request.getSession().getAttribute("currentUser");
        if(sqsxUser.getType() != 0 && sqsxUser.getType()!=1)
            return JsonResult.refuse();
        else {
            List<SqsxAnswerBean> sqsxAnswerBeans = sqsxAnswerRepository.selectedByUserid(sqsxUser.getId());
            if (sqsxAnswerBeans != null)
                return JsonResult.ok(sqsxAnswerBeans);
            else return JsonResult.returnnull("该用户没有回答记录");
        }
    }

    /**
    * 推荐回答的问题
    **/
    @PostMapping("QAComm/recommend")
    public JsonResult recommend(@RequestBody QuestionInterfaceBean bean,HttpServletRequest request) {
        String tag0 = bean.getQuestionLabel1();
        SqsxUser sqsxUser = (SqsxUser) request.getSession().getAttribute("currentUser");
        if(sqsxUser.getType() != 0 && sqsxUser.getType() != 1) {//检查权限
            return JsonResult.returnnull("权限不足");
        }
        else {
            if(tag0 == null){
                return JsonResult.returnnull("标签不能为空");
            }
            else {
            List<SqsxQuestionBean> sqsxQuestionBeans = sqsxQuestionRepository.selectByLabel(tag0);
            List<Object> list = new ArrayList<Object>();
            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < sqsxQuestionBeans.size(); i++) {
                if (sqsxQuestionBeans.get(i).getIsdel() == 1) {
                    System.out.println("问题" + sqsxQuestionBeans.get(i).getId() + "已被删除");
                } else {
                    JSONObject jsonObject = new JSONObject();
                    Integer questionid = sqsxQuestionBeans.get(i).getId();
                    List<SqsxQuestionTagBean> list1 = sqsxQuestionTagRepository.selectedByQuestionid(questionid);
                    List<String> tags = new ArrayList<String>();
                    for (int j = 0; j < list1.size(); j++) {
                        tags.add(list1.get(j).getLabel());
                    }
                    list.add(sqsxQuestionBeans.get(i).getId());
                    jsonObject.put("id", sqsxQuestionBeans.get(i).getId());
                    jsonObject.put("title", sqsxQuestionBeans.get(i).getTitle());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String time = simpleDateFormat.format(sqsxQuestionBeans.get(i).getTime());
                    java.sql.Date date = java.sql.Date.valueOf(time);
                    jsonObject.put("time",date);
                    jsonObject.put("count", sqsxQuestionBeans.get(i).getCount_answer());
                    jsonObject.put("status",sqsxQuestionBeans.get(i).getIsclose());
                    jsonObject.put("tags", tags);
                    jsonArray.add(jsonObject);
                }
            }
            if (list.isEmpty()) {
                return JsonResult.returnnull("没有相关问题");
            } else {
                return JsonResult.ok(jsonArray);
                }
            }
        }
    }

    /**
    * 设置最佳答案
    **/
    @PostMapping("QAComm/chooseBest")
    public JsonResult bestAnswer(@RequestBody QuestionInterfaceBean bean,HttpServletRequest request){

        String questionTitle = bean.getQuestionTitle();
        Integer answer_id = bean.getAnswerId();
        SqsxUser sqsxUser = (SqsxUser) request.getSession().getAttribute("currentUser");
        SqsxQuestionBean sqsxQuestionBean =  sqsxQuestionRepository.findByTitle(questionTitle);
        if(sqsxQuestionBean.getIsclose() == 1){//问题已设置最佳答案
            return JsonResult.returnnull("已设置最佳答案");
        }
        else {
            if(sqsxQuestionBean.getUser_id() != sqsxUser.getId())//不是提问者id，无法设置
            {
                return JsonResult.returnnull("不是提问者，无法设置");
            }
            else{//设置成功，设置问题isclose=1
                sqsxQuestionBean.setBest_answer_id(answer_id);
                sqsxQuestionBean.setIsclose(1);
                sqsxQuestionRepository.save(sqsxQuestionBean);
                return JsonResult.ok();
            }
        }

    }


    /**
     * 点赞
     *
     * */
    @PostMapping("QAComm/like")
    public JsonResult like(@RequestBody QuestionInterfaceBean bean, HttpServletRequest request) {
        Integer answer_id = bean.getAnswerId();
        SqsxUser sqsxUser = (SqsxUser) request.getSession().getAttribute("currentUser");
        if (sqsxUser.getType() != 0 && sqsxUser.getType() != 1)
        {//检查权限，0:学生 1:教师
            // 权限不足
            return JsonResult.refuse();
        } else {
            SqsxAnswerBean sqsxAnswerBean1 = sqsxAnswerRepository.findById(answer_id);
            SqsxQuestionBean sqsxQuestionBean = sqsxQuestionRepository.findById(sqsxAnswerBean1.getQuestion_id());
            if(sqsxQuestionBean.getIsclose() == 1){//检查问题是否关闭
                //isclose = 1,问题关闭无法点赞
                return JsonResult.refuse();
            }
            else {
                //增加点赞记录
                Integer userid = sqsxUser.getId();
                System.out.println(userid);

                SqsxLikeBean sqsxLikeBean = new SqsxLikeBean();
                sqsxLikeBean.setAnswer_id(answer_id);
                sqsxLikeBean.setUser_id(userid);
                sqsxLikeBean.setIsdel(0);

                sqsxLikeRepository.save(sqsxLikeBean);

                SqsxAnswerBean sqsxAnswerBean = sqsxAnswerRepository.findById(answer_id);
                sqsxAnswerBean.setCount_like(sqsxAnswerBean.getCount_like() + 1);
                sqsxAnswerRepository.save(sqsxAnswerBean);
                return JsonResult.ok(sqsxAnswerBean.getCount_like());
            }
        }
    }


    /**
    * 关闭问题（管理员权限）
    **/
    @PostMapping("QAComm/closeQuestion")
    public JsonResult close(@RequestBody QuestionInterfaceBean bean,HttpServletRequest request) {
        Integer question_id = bean.getQuestionId();
        SqsxUser sqsxUser = (SqsxUser) request.getSession().getAttribute("currentUser");
        if (sqsxUser.getType() != 2) {//检查权限 2:管理员权限
            //权限不足
            return JsonResult.refuse();
        } else {

            //设置问题isdel为1，关闭问题
            SqsxQuestionBean sqsxQuestionBean = sqsxQuestionRepository.findById(question_id);
            sqsxQuestionBean.setIsdel(1);
            sqsxQuestionRepository.save(sqsxQuestionBean);
            return JsonResult.ok();
        }
    }

}

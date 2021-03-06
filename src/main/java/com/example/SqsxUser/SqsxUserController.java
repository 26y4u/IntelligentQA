package com.example.SqsxUser;

import com.alibaba.fastjson.JSONObject;
import com.example.SqsxQuestion.SqsxAnswerRepository;
import com.example.SqsxQuestion.SqsxQuestionRepository;
import com.example.SqsxQuestion.SqsxQuestionTagRepository;
import com.example.Utils.CookieUtil;
import com.example.Utils.ImportJson;
import com.example.Utils.JsonResult;
import com.example.Utils.ToHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

import static com.example.Utils.ToHash.toHash;

/**
 * 控制层
 * <p/>
 * yutianran 2017/1/19 下午9:02
 */
@CrossOrigin
@RestController
@RequestMapping("/")
public class SqsxUserController {
    @Autowired
    private SqsxUserRepository sqsxuserRepository;
    @Autowired
    private SqsxUserService sqsxUserService;
    @Autowired
    private SqsxQuestionRepository questionRepository;
    @Autowired
    private SqsxAnswerRepository answerRepository;
    @Autowired
    private SqsxQuestionTagRepository questionTagRepository;



    @GetMapping("findAll")
    public List<SqsxUser> getAll() {
        return sqsxuserRepository.findAll();
    }

    @PostMapping("findByUsername")
    @Transactional   //事务操作
    public SqsxUser findByUsername(@RequestParam("username") String username) {
        //通过用户名查找数据库中是否有该用户名
        return sqsxuserRepository.findByUsername(username);
    }

    @PostMapping("sign/up")
    @Transactional   //事务操作
    public JsonResult save(@RequestBody InterFaceBean bean,HttpServletRequest request)
    {//增加一个用户
            if(findByUsername(bean.getUsername())!=null)
            {
                return JsonResult.refuse();//已有该用户名。返回400，表示服务器已经理解请求，但是拒绝执行它。

            }else {
                SqsxUser sqsxUser = new SqsxUser();
                sqsxUser.setUsername(bean.getUsername());
                sqsxUser.setPassword((toHash(bean.getPassword())));
                sqsxUser.setType(bean.getType());
                sqsxUser.setIsdel(0);
                sqsxuserRepository.save(sqsxUser);
                CookieUtil.addCookie("username",sqsxUser.getUsername());
                CookieUtil.addCookie("type",sqsxUser.getType());
                CookieUtil.addCookie("id",sqsxUser.getId());
                return JsonResult.ok(sqsxUser);
            }
    }
    @PostMapping("findById")
    public SqsxUser findById(@RequestParam("id") Integer id) {
        return sqsxuserRepository.findById(id);
    }

    @PostMapping("sign/in")
    @Transactional   //事务操作
    public JsonResult login(@RequestBody InterFaceBean bean,HttpServletRequest request) {//登陆，验证结果返回：-1表示失败，1表示成功
        SqsxUser sqsxUser = sqsxuserRepository.findByUserName(bean.getUsername());
        //System.out.println(sqsxUser.getId()+sqsxUser.getUsername()+sqsxUser.getPassword()+sqsxUser.getType()+sqsxUser.getIsdel());
        if (toHash(bean.getPassword())!= sqsxUser.getPassword())
        {
            return JsonResult.returnnull("用户与密码不匹配");
        } else if(sqsxUser.getIsdel()==1) {
            return JsonResult.refuseservice("该用户不存在");
        }else if(sqsxUser.getType()!= bean.getType())
        {
            return JsonResult.refuseservice("用户类型选择错误");
        }else{
            //在需要判断用户是否登录的地方,或者获取用户信息的地方,使用 SqsxUser user = request.getSession.getAttribute("currentUser")
            //request.getSession().setAttribute("currentUser",sqsxUser);
            CookieUtil.addCookie("username",sqsxUser.getUsername());
            CookieUtil.addCookie("type",sqsxUser.getType());
            CookieUtil.addCookie("id",sqsxUser.getId());
            return JsonResult.ok(sqsxUser);


        }

      //return 1;
    }
    @PostMapping("profile/modifyPass")
    @Transactional   //事务操作
    public JsonResult updatepwd(@RequestBody InterFaceBean bean,HttpServletRequest request) {
        //SqsxUser sqsxUser = (SqsxUser) request.getSession().getAttribute("currentUser");
        String username = CookieUtil.getCookie("username");
        if(username==null)
        {
            return JsonResult.refuseservice("请先登录");
        }else {
            //已登录,
            username = username.replace('"', ' ').trim();
            SqsxUser sqsxUser = sqsxuserRepository.findByUserName(username);
            if (toHash(bean.getPassword()) == sqsxUser.getPassword() && sqsxUser.getIsdel() != 1) {
                sqsxUser.setPassword(toHash(bean.getNewpassword()));
                sqsxuserRepository.save(sqsxUser);
                return JsonResult.ok(sqsxUser);//根据主键查找并更新
            } else {
                return JsonResult.refuse();//密码输入错误,状态码：400服务器已经理解请求，但是拒绝执行它。
            }
        }
    }

    @PostMapping("profile/modifytag")
    @Transactional   //事务操作
    public JsonResult updatetag(@RequestBody InterFaceBean bean,HttpServletRequest request) {
        String username = CookieUtil.getCookie("username");
        if(username==null)
        {
            return JsonResult.refuseservice("请先登录");
        }else {
            //已登录,
            username = username.replace('"', ' ').trim();
            SqsxUser sqsxUser = sqsxuserRepository.findByUserName(username);
            sqsxUser.setTag0(bean.getTag0());
            sqsxUser.setTag1(bean.getTag1());
            sqsxUser.setTag2(bean.getTag2());
            sqsxuserRepository.save(sqsxUser);
            return JsonResult.ok(sqsxUser);
        }
    }
    @GetMapping("testAdd")
    @Transactional
    public void testAdd() {
        ImportJson importJson = new ImportJson(questionRepository,answerRepository,sqsxuserRepository,questionTagRepository);
        importJson.testAdd();
    }

}

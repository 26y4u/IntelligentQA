package com.example.SqsxUser;

import com.example.Utils.JsonResult;
import com.example.Utils.ToHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.Utils.ToHash.toHash;

/**
 * 控制层
 * <p/>
 * yutianran 2017/1/19 下午9:02
 */
@RestController
@RequestMapping("/")
public class SqsxUserController {
    @Autowired
    private SqsxUserRepository sqsxuserRepository;
    @Autowired
    private SqsxUserService sqsxUserService;

    @RequestMapping("a")
    public String a()
    {
        return "hello";
    }

    @GetMapping("findAll")
    public List<SqsxUser> getAll() {
        return sqsxuserRepository.findAll();
    }

    @PostMapping("findByUsername")
    public SqsxUser findByUsername(@RequestParam("username") String username) {
        //通过用户名查找数据库中是否有该用户名
        return sqsxuserRepository.findByUsername(username);
    }

    @PostMapping("sign/up")
    public JsonResult save(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("type") Integer type)
    {//增加一个用户
            if(findByUsername(username)!=null)
            {
                return JsonResult.refuse();//已有该用户名。返回400，表示服务器已经理解请求，但是拒绝执行它。

            }else {
                SqsxUser sqsxUser = new SqsxUser();
                sqsxUser.setUsername(username);
                sqsxUser.setPassword(toHash(password));
                sqsxUser.setType(type);
                sqsxUser.setIsdel(0);
                sqsxuserRepository.save(sqsxUser);
                return JsonResult.ok(sqsxUser);
            }
    }
    @PostMapping("findById")
    public SqsxUser findById(@RequestParam("id") Integer id) {
        return sqsxuserRepository.findById(id);
    }

    @PostMapping("sign/in")
    public JsonResult login(@RequestParam("username") String username, @RequestParam("password") String password) {//登陆，验证结果返回：-1表示失败，1表示成功

        SqsxUser sqsxUser = sqsxuserRepository.findByUsername(username);
        System.out.println(sqsxUser.getId()+sqsxUser.getUsername()+sqsxUser.getPassword()+sqsxUser.getType()+sqsxUser.getIsdel());
        if (sqsxUser!= null && toHash(password) == sqsxUser.getPassword() && sqsxUser.getIsdel()!=1) {
            return JsonResult.ok(sqsxUser);
        } else {
                return JsonResult.refuse();//密码输入错误,状态码：400服务器已经理解请求，但是拒绝执行它。
        }
      //return 1;
    }
    @PostMapping("profile/modifyPass")
    @Transactional   //事物操作
    public JsonResult update(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("newpassword") String newpassword) {
        SqsxUser sqsxUser = sqsxuserRepository.findByUsername(username);
        if(toHash(password) == sqsxUser.getPassword() && sqsxUser.getIsdel()!=1)
        {
            sqsxUser.setPassword(toHash(newpassword));
            sqsxuserRepository.save(sqsxUser);
            return JsonResult.ok(sqsxUser);//根据主键查找并更新
        }else {
            return JsonResult.refuse();//密码输入错误,状态码：400服务器已经理解请求，但是拒绝执行它。
        }
    }
}

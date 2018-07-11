package com.example.file;

import com.example.SqsxUser.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.example.Utils.GetTime;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 */
@CrossOrigin
@Controller
@RestController
@javax.transaction.Transactional
@Service
@RequestMapping("/file")
public class FileController{


    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private UpLoadRepository uploadRepository;
    @Autowired
    private DownLoadRepository downloadRepository;
    @Autowired
    private SqsxUserRepository sqsxuserRepository;
    @Autowired
    private SqsxUserService sqsxUserService;

    //时间格式化




    @RequestMapping("a")
    public String a()
    {
        return "hello";
    }

    @RequestMapping("aafile")
    public String file() {
        return "file";
    }

    @PostMapping("/findAll")
    public JsonResult getAll() {
        return  JsonResult.ok(fileRepository.findAll());
    }

    @Transactional
    @PostMapping("/findByUserNameUp")
    public JsonResult findByUserNameUp(@RequestBody FileInterFacceBean bean){

        SqsxUser user = sqsxuserRepository.findByUsername(bean.getUploader());//根据用户名找到唯一userid
        List<UploadBean> upload =  uploadRepository.findByUserId(user.getId());//该用户上传的记录
        if(upload==null||upload.size()==0){
            return JsonResult.returnnull("该用户没有上传记录");
        }
        List<FileBean> fileList = new ArrayList<>();
        for(int i =0;i<upload.size();i++){
            UploadBean uploadrecord = upload.get(i);//取出fileid
            fileList.add(fileRepository.findOne(uploadrecord.getFile_id()));
        }
        return JsonResult.ok(fileList);

    }

    @Transactional
    @PostMapping("/findByUserNameDown")
    public JsonResult findByUserNameDown(@RequestBody String user_name){
        //根据用户名，查找某个用户的下载记录
        if(fileRepository.findByUND(user_name)!=null)//存在该用户的下载文件记录
        {
            return JsonResult.ok(fileRepository.findByUND(user_name));}
        else{//服务器成功处理了请求，且没有返回任何内容
            return JsonResult.returnnull("该用户没有下载文件记录");
        }
         // return JsonResult.ok();
    }

    @Transactional
    @PostMapping("/findByLabelUp")
    public JsonResult findByLabelUp(@RequestBody FileInterFacceBean bean){
        if(fileRepository.findByLU(bean.getTag2())!= null)//先看子标签（最细化的），如果小的分类下有文件则返回
            return JsonResult.ok(fileRepository.findByLU(bean.getTag2()));
        if(fileRepository.findByLU(bean.getTag1())!=null)
            return JsonResult.ok(fileRepository.findByLU(bean.getTag1()));
        if(fileRepository.findByLU(bean.getTag0())!=null)
            return JsonResult.ok(fileRepository.findByLU(bean.getTag0()));
        else{//服务器成功处理了请求，且没有返回任何内容
            return JsonResult.returnnull("该标签下没有文件记录");
        }
        // return JsonResult.ok();
    }

    @Transactional
    @PostMapping("/findByTitle")
    public JsonResult findByFilename(@RequestBody FileInterFacceBean bean){
        //根据文件名查找文件
        return  JsonResult.ok(fileRepository.findByFileName(bean.getTitle()));
    }


    @Transactional
    @RequestMapping(value = "Upload",method = RequestMethod.POST)
    public JsonResult singleFileUpload(@RequestBody MultipartFile file,@RequestBody FileInterFacceBean bean,HttpServletRequest request) {
        //后端通过MultiFile拿到前端传入的文件，并在代码中实现转存。
        try {
            //获取 用户名，文件名，md5码将文件存到file和upload中。7/11日更改需求：同一个文件也要在数据库中存多次md5码,这样好像把file和upload表合在一起也可以。。。但还是先不改了
            SqsxUser user = (SqsxUser) request.getSession().getAttribute("currentUser");

            FileBean fileup = new FileBean();
            fileup.setMd5(QiniuUtil.upload(file));//返回的是七牛云反馈的string的hash码
            fileup.setFileName(bean.getTitle());//文件名
            //file.setType(type);
            fileup.setIsdel(0);
            fileRepository.save(fileup);

            UploadBean upload = new UploadBean();
            upload.setUser_id(user.getId());
            upload.setFile_id(fileup.getFile_id());
            upload.setDown_num(0);
            upload.setTime(GetTime.getTime());
            upload.setIsdel(0);
            uploadRepository.save(upload);

            return JsonResult.ok();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return JsonResult.refuse();
        } catch (IOException e) {
            e.printStackTrace();
            return JsonResult.refuse();
        }
    }

    @Transactional
    @PostMapping("/DownLoad")
    public JsonResult DownLoad( @RequestBody FileInterFacceBean bean,HttpServletRequest request) throws IOException {
        //获取 用户名，文件名，md5码先查文件id，再在upload表里down_num+1，在download表里加一行
        SqsxUser user = (SqsxUser) request.getSession().getAttribute("currentUser");
        UploadBean upload = uploadRepository.findByUserIdAndFileId(user.getId(),bean.getFileid());
        int dn = upload.getDown_num();
        upload.setDown_num(dn+1);

        DownloadBean down = new DownloadBean();
        down.setFile_id(bean.getFileid());
        down.setUser_id(user.getId());
        down.setTime(GetTime.getTime());

        return JsonResult.ok(QiniuUtil.download(bean.getMd5()));
    }

}

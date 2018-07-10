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
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 */
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

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");//时间格式化

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
    public JsonResult findByUserNameUp(@RequestParam("username") String user_name){

        SqsxUser user = new SqsxUser();
        user = sqsxuserRepository.findByUserName(user_name).get(0);
        UploadBean upload = new UploadBean();
        upload =  uploadRepository.findByUserId(user.getId()).get(0);

        if(fileRepository.findByUNU(user.getId())!=null)//存在该用户的上传文件记录
        {
        return JsonResult.ok(fileRepository.findByUNU(user.getId()));}
        else{//服务器成功处理了请求，且没有返回任何内容
            return JsonResult.returnnull("该用户没有上传文件记录");
        }
     //  return JsonResult.ok();
    }

    @Transactional
    @PostMapping("/findByUserNameDown")
    public JsonResult findByUserNameDown(@RequestParam("username") String user_name){

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
    public JsonResult findByLabelUp(@RequestParam("label") Integer label){

        if(fileRepository.findByLU(label)!= null)//存在该用户的下载文件记录
        {
            return JsonResult.ok(fileRepository.findByLU(label));}
        else{//服务器成功处理了请求，且没有返回任何内容
            return JsonResult.returnnull("该用户没有下载文件记录");
        }
        // return JsonResult.ok();
    }



//    @Transactional
//    @PostMapping("/UpLoad")
//    public JsonResult UpLoad(@RequestParam("username") String user_name){
//        //还没有写！！！1啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊
//        if(fileRepository.findByUNU(user_name)!=null)//存在该用户的上传文件记录
//        {
//            return JsonResult.ok(fileRepository.findByUNU(user_name));}
//        else{//服务器成功处理了请求，且没有返回任何内容
//            return JsonResult.returnnull("该用户没有上传文件记录");
//        }
//        //  return JsonResult.ok();
//    }
    /*
    @RequestMapping("upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String time = sdf.format(System.currentTimeMillis());
            File dest = new File("/Users/yutianran/Desktop", time + "_" + file.getOriginalFilename());
            FileUtils.copyInputStreamToFile(file.getInputStream(), dest);
        }
        return "上传成功";
    }

    @RequestMapping("download")
    public ResponseEntity<byte[]> download() throws IOException {
        String filename = "down.jpg";
        File file = new File("/Users/yutianran/Pictures", filename);
        //封装http头部信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", filename);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                headers, HttpStatus.CREATED);
    }

    */

}

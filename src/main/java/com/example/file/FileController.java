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
@RequestMapping("/resource")
public class FileController {


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

    @RequestMapping("a")
    public String a() {
        return "hello";
    }

    @RequestMapping("aafile")
    public String file() {
        return "file";
    }

    @Transactional
    @PostMapping("/findAll")
    public JsonResult getAll() {
        List<UploadBean> upload = uploadRepository.selectAll();
        if (upload == null || upload.size() == 0) {
            return JsonResult.returnnull("还没有上传文件的记录");
        } else {
            List<FileBean> fileList = new ArrayList<>();
            for (int i = 0; i < upload.size(); i++) {
                UploadBean uploadrecord = upload.get(i);//取出fileid
                fileList.add(fileRepository.selectByFileId(uploadrecord.getFile_id()));
            }
            return JsonResult.ok(fileList);
        }
    }
    @Transactional
    @PostMapping("/findByUserNameUp")
    public JsonResult findByUserNameUp(HttpServletRequest request) {
        SqsxUser user = (SqsxUser) request.getSession().getAttribute("currentUser");
        // SqsxUser user = sqsxuserRepository.findByUsername(bean.getUploader());//根据用户名找到唯一userid
        List<UploadBean> upload = uploadRepository.selectByUserId(user.getId());

        if (upload == null || upload.size() == 0) {
            return JsonResult.returnnull("该用户没有上传文件的记录");
        } else {
            //System.out.println("1");
            List<FileBean> fileList = new ArrayList<>();
            for (int i = 0; i < upload.size(); i++) {

                UploadBean uploadrecord = upload.get(i);//取出fileid
                //System.out.println(i);
                fileList.add(fileRepository.selectByFileId(uploadrecord.getFile_id()));
            }
            return JsonResult.ok(fileList);
        }
    }

    @Transactional
    @PostMapping("/findByUserNameDown")
    public JsonResult findByUserNameDown(@RequestBody FileInterFacceBean bean, HttpServletRequest request) {
        //根据用户名，查找某个用户的下载记录(该用户自己的)
        SqsxUser user = (SqsxUser) request.getSession().getAttribute("currentUser");
        if (user != null) {//非游客
            List<DownloadBean> download = downloadRepository.findByUserId(user.getId());
            if (download == null || download.size() == 0)//不存在该用户的下载文件记录
            {
                return JsonResult.returnnull("该用户没有下载记录");
            } else {
                List<FileBean> fileList = new ArrayList<>();
                for (int i = 0; i < download.size(); i++) {
                    DownloadBean downloadrecord = download.get(i);//取出fileid
                    fileList.add(fileRepository.selectByFileId(downloadrecord.getFile_id()));
                }
                return JsonResult.ok(fileList);
            }
        } else return JsonResult.returnnull("该用户没有下载记录");
        // return JsonResult.ok();
    }

    @Transactional
    @PostMapping("/findByTag")
    public JsonResult findByTag(@RequestBody FileInterFacceBean bean) {
        if (fileRepository.findByTG2(bean.getTag2()) != null)//先看子标签（最细化的），如果小的分类下有文件则返回
            return JsonResult.ok(fileRepository.findByTG2(bean.getTag2()));
        if (fileRepository.findByTG1(bean.getTag1()) != null)
            return JsonResult.ok(fileRepository.findByTG1(bean.getTag1()));
        if (fileRepository.findByTG0(bean.getTag0()) != null)
            return JsonResult.ok(fileRepository.findByTG0(bean.getTag0()));
        else {//服务器成功处理了请求，且没有返回任何内容
            return JsonResult.returnnull("该标签下没有文件记录");
        }
        // return JsonResult.ok();
    }

    @Transactional
    @PostMapping("/findByTitle")
    public JsonResult findByFilename(@RequestBody FileInterFacceBean bean) {
        //根据文件名查找文件
        return JsonResult.ok(fileRepository.findbyName(bean.getTitle()));
    }


    @Transactional
    @RequestMapping(value = "Upload", method = RequestMethod.POST)
    public JsonResult singleFileUpload(@RequestBody MultipartFile file, @RequestBody FileInterFacceBean bean, HttpServletRequest request) throws IOException {

        //获取 用户名，文件名，md5码将文件存到file和upload中。7/11日更改需求：同一个文件也要在数据库中存多次md5码
        //管理员不可上传资源
        SqsxUser user = (SqsxUser) request.getSession().getAttribute("currentUser");

        if (user.getType() == 0 || user.getType() == 1) {
            try {
                FileBean fileup = new FileBean();
                fileup.setMd5(QiniuUtil.upload(file));//返回的是七牛云反馈的string的hash码
                fileup.setFileName(bean.getTitle());//文件名
                fileup.setType(bean.getTitle().substring(bean.getTitle().lastIndexOf(".")));
                fileup.setIsdel(0);
                fileRepository.save(fileup);

                UploadBean upload = new UploadBean();
                upload.setUser_id(user.getId());
                upload.setFile_id(fileup.getId());
                upload.setDown_num(0);
                upload.setTime(GetTime.getTime());
                upload.setIsdel(0);
                uploadRepository.save(upload);

                return JsonResult.ok(upload);
            } catch (IllegalStateException e) {
                e.printStackTrace();
                return JsonResult.refuse();
            } catch (IOException e) {
                e.printStackTrace();
                return JsonResult.refuse();
            }
        } else return JsonResult.refuseforlimit("您没有权限上传文件");

    }

    @Transactional
    @PostMapping("/DownLoad")
    public JsonResult DownLoad(@RequestBody FileInterFacceBean bean, HttpServletRequest request) throws IOException {
        //获取 下载者用户名，文件名，md5码先查文件id，再在upload表里down_num+1，在download表里加一行
        SqsxUser user = (SqsxUser) request.getSession().getAttribute("currentUser");

        //UploadBean upload = uploadRepository.selectByUseridAndFileid( user.getId(),bean.getFileid());//根据fileid找到相应记录
        UploadBean upload = uploadRepository.selectByFileId(bean.getFileid());
        int dn = upload.getDown_num();
        upload.setDown_num(dn + 1);
        uploadRepository.save(upload);

        DownloadBean down = new DownloadBean();
        down.setFile_id(bean.getFileid());
        if (user != null)
            down.setUser_id(user.getId());
        else down.setUser_id(0);//游客
        down.setTime(GetTime.getTime());
        downloadRepository.save(down);
        return JsonResult.ok(QiniuUtil.download(bean.getMd5()));
    }

    @PostMapping("/deletefile")
    @Transactional   //事务操作
    public JsonResult deletefile(@RequestBody FileInterFacceBean bean, HttpServletRequest request) {

        SqsxUser sqsxUser = (SqsxUser) request.getSession().getAttribute("currentUser");
        UploadBean upload = uploadRepository.selectByFileId(bean.getFileid());
        if (sqsxUser.getId() != upload.getUser_id() && sqsxUser.getType() != 2)//非本人操作或非管理员
        {
            return JsonResult.refuseforlimit("您没有权限");
        } else {
            FileBean file = fileRepository.selectByFileId(bean.getFileid());
            file.setIsdel(1);
            fileRepository.save(file);
            return JsonResult.ok("您已成功删除该文件");
        }
    }
}

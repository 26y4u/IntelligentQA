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
import javax.servlet.http.HttpServletResponse;
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
            List<FileInterFacceBean> fileinterface = new ArrayList<>();
            //根据upload中的记录去找file中的isdel不为1的文件，和user表中的用户名
            FileBean file = new FileBean();
            SqsxUser user=new SqsxUser();
            for (int i = 0; i < upload.size(); i++) {
                UploadBean uploadrecord = upload.get(i);//取出fileid
                file = fileRepository.selectByFileId(uploadrecord.getFile_id());
                user = sqsxuserRepository.findById(uploadrecord.getUser_id());
                FileInterFacceBean fileinertfacebean = new FileInterFacceBean();
                //返回：文件名，文件id，下载量，上传用户名，标题，，类型
                fileinertfacebean.setFileid(file.getId());
                fileinertfacebean.setTitle(file.getFileName());
                fileinertfacebean.setTag0(file.getTag0());
                fileinertfacebean.setTag1(file.getTag1());
                fileinertfacebean.setTag2(file.getTag2());
                fileinertfacebean.setDownloads(uploadrecord.getDown_num());
                fileinertfacebean.setUploader(user.getUsername());
                fileinertfacebean.setType(file.getType());
                fileinterface.add(fileinertfacebean);
            }
            return JsonResult.ok(fileinterface);
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
            List<FileInterFacceBean> fileinterface = new ArrayList<>();
            FileBean file = new FileBean();
            for (int i = 0; i < upload.size(); i++) {
                UploadBean uploadrecord = upload.get(i);//取出fileid
                file = fileRepository.selectByFileId(uploadrecord.getFile_id());
                user = sqsxuserRepository.findById(uploadrecord.getUser_id());
                FileInterFacceBean fileinertfacebean = new FileInterFacceBean();
                //返回：文件名，文件id，下载量，上传用户名，标题，，类型
                fileinertfacebean.setFileid(file.getId());
                fileinertfacebean.setTitle(file.getFileName());
                fileinertfacebean.setTag0(file.getTag0());
                fileinertfacebean.setTag1(file.getTag1());
                fileinertfacebean.setTag2(file.getTag2());
                fileinertfacebean.setDownloads(uploadrecord.getDown_num());
                fileinertfacebean.setUploader(user.getUsername());
                fileinertfacebean.setType(file.getType());
                fileinterface.add(fileinertfacebean);
            }
            return JsonResult.ok(fileinterface);
        }
    }

    @Transactional
    @PostMapping("/searchByMyNameDown")
    public JsonResult findByUserNameDown(@RequestBody FileInterFacceBean bean, HttpServletRequest request) {
        //根据用户名，查找某个用户的下载记录(该用户自己的)
        SqsxUser user = (SqsxUser) request.getSession().getAttribute("currentUser");
        if (user != null) {//非游客
            List<DownloadBean> download = downloadRepository.findByUserId(user.getId());
            if (download == null || download.size() == 0)//不存在该用户的下载文件记录
            {
                return JsonResult.returnnull("该用户没有下载记录");
            } else {
                List<FileInterFacceBean> fileinterface = new ArrayList<>();
                FileBean file = new FileBean();
                for (int i = 0; i < download.size(); i++) {
                    DownloadBean downloadrecord = download.get(i);//取出fileid
                    file = fileRepository.selectByFileId(downloadrecord.getFile_id());
                    FileInterFacceBean fileinertfacebean = new FileInterFacceBean();
                    //返回：文件名，文件id，上传用户名，类型
                    fileinertfacebean.setFileid(file.getId());
                    fileinertfacebean.setTitle(file.getFileName());
                    fileinertfacebean.setTag0(file.getTag0());
                    fileinertfacebean.setTag1(file.getTag1());
                    fileinertfacebean.setTag2(file.getTag2());
                    fileinertfacebean.setUploader(user.getUsername());
                    fileinertfacebean.setType(file.getType());
                    fileinterface.add(fileinertfacebean);
                }
                return JsonResult.ok(fileinterface);
            }
            // return JsonResult.ok();
        }else return JsonResult.refuseforlimit("游客请先登录");
    }

    @Transactional
    @PostMapping("/findByTag")
    public JsonResult findByTag(@RequestBody FileInterFacceBean bean) {
        List<FileInterFacceBean> fileinterface = new ArrayList<>();
        List<FileBean> filelist = new ArrayList<>();
        if ( bean.getTag2()!=null && bean.getTag2()!="全部")//先看子标签（最细化的），如果小的分类下有文件则返回
            filelist = fileRepository.findByTG2(bean.getTag2());
        if (bean.getTag1() != null)
            filelist = fileRepository.findByTG1(bean.getTag1());
        if (bean.getTag0() != null)
            filelist = fileRepository.findByTG0(bean.getTag0());
        FileBean file = new FileBean();
        SqsxUser user = new SqsxUser();
        UploadBean upload = new UploadBean();
        for (int i = 0; i < filelist.size(); i++) {
            FileBean afile = filelist.get(i);//取出fileid
            upload = uploadRepository.selectByFileId(afile.getId());
            user = sqsxuserRepository.findById(upload.getUser_id());
            FileInterFacceBean fileinertfacebean = new FileInterFacceBean();
            //返回：文件名，文件id，下载量，上传用户名，标题，，类型
            fileinertfacebean.setFileid(file.getId());
            fileinertfacebean.setTitle(file.getFileName());
            fileinertfacebean.setTag0(file.getTag0());
            fileinertfacebean.setTag1(file.getTag1());
            fileinertfacebean.setTag2(file.getTag2());
            fileinertfacebean.setDownloads(upload.getDown_num());
            fileinertfacebean.setUploader(user.getUsername());
            fileinertfacebean.setType(file.getType());
            fileinterface.add(fileinertfacebean);
        }
        return JsonResult.ok(fileinterface);
    }

    @Transactional
    @PostMapping("/findByTitle")
    public JsonResult findByFilename(@RequestBody FileInterFacceBean bean) {
        //根据文件名查找文件
        List<FileInterFacceBean> fileinterface = new ArrayList<>();
        List<FileBean> filelist = new ArrayList<>();
        SqsxUser user = new SqsxUser();
        UploadBean upload = new UploadBean();
        FileBean file = new FileBean();
        filelist = fileRepository.selectByFileName(bean.getTitle());
        for (int i = 0; i < filelist.size(); i++) {
            file = filelist.get(i);
            upload = uploadRepository.selectByFileId(file.getId());
            user = sqsxuserRepository.findById(upload.getUser_id());
            FileInterFacceBean fileinertfacebean = new FileInterFacceBean();
            //返回：文件名，文件id，下载量，上传用户名，标题，，类型
            fileinertfacebean.setFileid(file.getId());
            fileinertfacebean.setTitle(file.getFileName());
            fileinertfacebean.setTag0(file.getTag0());
            fileinertfacebean.setTag1(file.getTag1());
            fileinertfacebean.setTag2(file.getTag2());
            fileinertfacebean.setDownloads(upload.getDown_num());
            fileinertfacebean.setUploader(user.getUsername());
            fileinertfacebean.setType(file.getType());

            fileinterface.add(fileinertfacebean);
        }
        return JsonResult.ok(fileinterface);
}

    @Transactional
    @PostMapping("/search")
    public JsonResult search(@RequestBody FileInterFacceBean bean) {
        //根据文件名或上传者查找文件
        //根据文件名查找文件
        List<FileInterFacceBean> fileinterface = new ArrayList<>();
        List<FileBean> filelist = new ArrayList<>();
        SqsxUser user = new SqsxUser();
        UploadBean upload = new UploadBean();
        FileBean file = new FileBean();
        filelist = fileRepository.selectByFileName(bean.getTitle());
        FileInterFacceBean fileinertfacebean = new FileInterFacceBean();

        for (int i = 0; i < filelist.size(); i++) {
            file = filelist.get(i);
            upload = uploadRepository.selectByFileId(file.getId());
            user = sqsxuserRepository.findById(upload.getUser_id());
            //返回：文件名，文件id，下载量，上传用户名，标题，，类型
            fileinertfacebean.setFileid(file.getId());
            fileinertfacebean.setTitle(file.getFileName());
            fileinertfacebean.setTag0(file.getTag0());
            fileinertfacebean.setTag1(file.getTag1());
            fileinertfacebean.setTag2(file.getTag2());
            fileinertfacebean.setDownloads(upload.getDown_num());
            fileinertfacebean.setUploader(user.getUsername());
            fileinertfacebean.setType(file.getType());
            fileinterface.add(fileinertfacebean);
        }

        user = sqsxuserRepository.findByUserName(bean.getUploader());
        List<UploadBean> uploadlist = uploadRepository.selectByUserId(user.getId());
        if (uploadlist == null || uploadlist.size() == 0) ;
            else {
            FileBean afile = new FileBean();
            for (int i = 0; i < uploadlist.size(); i++) {
                UploadBean uploadrecord = uploadlist.get(i);//取出fileid
                file = fileRepository.selectByFileId(uploadrecord.getFile_id());
                user = sqsxuserRepository.findById(uploadrecord.getUser_id());
                //返回：文件名，文件id，下载量，上传用户名，标题，，类型
                fileinertfacebean.setFileid(file.getId());
                fileinertfacebean.setTitle(file.getFileName());
                fileinertfacebean.setTag0(file.getTag0());
                fileinertfacebean.setTag1(file.getTag1());
                fileinertfacebean.setTag2(file.getTag2());
                fileinertfacebean.setDownloads(uploadrecord.getDown_num());
                fileinertfacebean.setUploader(user.getUsername());
                fileinertfacebean.setType(file.getType());
                fileinterface.add(fileinertfacebean);
            }
        }
        return JsonResult.ok(fileinterface);
    }


    @Transactional
    @RequestMapping(value = "upLoad", method = RequestMethod.POST)
    public JsonResult singleFileUpload(@RequestParam("file") MultipartFile file,@RequestParam("filename") String filename,@RequestParam("tag1") String tag0, @RequestParam("tag2") String tag1,@RequestParam("tag3") String tag2,@RequestParam("type") String type,@RequestBody FileInterFacceBean bean, HttpServletRequest request) throws IOException {
        //文件名，真正传的文件，tag是搜索用的：有可能tag3是“所有”即显示tag2下的所有类型文件。type是用户选的文件类型。下载和预览是一个接口方法，type1是预览啥的
        //获取 用户名，文件名，md5码将文件存到file和upload中。7/11日更改需求：同一个文件也要在数据库中存多次md5码
        //管理员不可上传资源
        SqsxUser user = (SqsxUser) request.getSession().getAttribute("currentUser");
        if (user.getType() == 0 || user.getType() == 1) {
            try {
                //7/13日更改需求：前端再给一个新的用户名（用户可自己在写一个文件名，在file表中存这个，文件类型也是用户自己选的在filename中加上）
                FileBean fileup = new FileBean();
                String title = filename;
                fileup.setMd5(QiniuUtil.upload(file,title + type));//返回的是七牛云反馈的string的hash码
                fileup.setType(type);
                fileup.setTag0(tag0);
                fileup.setTag1(tag1);
                fileup.setTag2(tag2);
                fileup.setFileName(title + type);
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
    @PostMapping("/getUrl")
    public String DownLoad(@RequestBody FileInterFacceBean bean, HttpServletRequest request,HttpServletResponse response) throws IOException {
        //获取 下载者用户名，type表示1预览2是下载。文件名，md5码先查文件id，再在upload表里down_num+1，在download表里加一行
        SqsxUser user = (SqsxUser) request.getSession().getAttribute("currentUser");
        if(bean.getUrlType()==2) {
            //UploadBean upload = uploadRepository.selectByUseridAndFileid( user.getId(),bean.getFileid());//根据fileid找到相应记录
            UploadBean upload = uploadRepository.selectByFileId(bean.getFileid());
            int dn = upload.getDown_num();
            upload.setDown_num(dn + 1);
            uploadRepository.save(upload);
            //return JsonResult.ok(upload);
            DownloadBean down = new DownloadBean();
            down.setFile_id(bean.getFileid());
            if (user != null)
                down.setUser_id(user.getId());
            else down.setUser_id(0);//游客
            down.setTime(GetTime.getTime());
            downloadRepository.save(down);
            FileBean file = fileRepository.selectByFileId(bean.getFileid());
            response.setHeader("Content-Type","octet/stream");//setContentType("octet/stream");
            response.setHeader("Content-Disposition","attachment:filename="+file.getFileName());
            return QiniuUtil.download(file.getMd5());
        }else {
            FileBean file = fileRepository.selectByFileId(bean.getFileid());
            return QiniuUtil.download(file.getMd5());
        }
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

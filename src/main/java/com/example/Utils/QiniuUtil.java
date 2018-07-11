package com.example.Utils;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;

public class QiniuUtil {

    //设置好账号的ACCESS_KEY和SECRET_KEY
    private static final String ACCESS_KEY = "C6LpWHSCNCT6I7_0D_EIgdoieSYg0HXXcipPug5D";
    private static final String SECRET_KEY = "5juuFk14S8aqnQF8xeB6SS6H8D4vDo7g29Y74uk-";
    //要上传的空间
    private static final String BUCKET_NAME = "intellgentqa";

    private static final String BUCKET_DOMAIN = "pbmqrl67c.bkt.clouddn.com";

    static Zone z = Zone.autoZone();
    static Configuration c = new Configuration(z);
    static Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    //创建上传对象
    static UploadManager uploadManager = new UploadManager(c);

    //设置callbackUrl以及callbackBody,七牛将文件名和文件大小回调给业务服务器
    public static String getUpToken() {
        /*return auth.uploadToken(BUCKET_NAME, null, 3600, new StringMap()
                .put("callbackUrl", "http://your.domain.com/callback")
                .put("callbackBody", "filename=$(fname)&filesize=$(fsize)"));
                */
        return auth.uploadToken(BUCKET_NAME);
    }

    public static String upload(MultipartFile file) throws IOException {
        try {
            //调用put方法上传
            //Response res = uploadManager.put(file.getBytes(), file.getName(), getUpToken());
            //key为null,则使用hash的值作为key
            Response res = uploadManager.put("C:\\Users\\Zhu\\Desktop\\38.jpg", null, getUpToken());
            //System.out.print(res.bodyString());
            JSONObject object = JSONObject.parseObject(res.bodyString());
            String hash="";
            try{
                hash = object.get("hash").toString();

            }catch (NullPointerException e){
                e.printStackTrace();
                return null;
            }
            return hash;


        } catch (QiniuException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String download(String hash) throws IOException {
        String encodedFileName = URLEncoder.encode(hash, "utf-8");
        String finalUrl = String.format("%s/%s", BUCKET_DOMAIN, encodedFileName);
        return finalUrl;

    }


}

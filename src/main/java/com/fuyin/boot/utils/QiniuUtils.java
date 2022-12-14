package com.fuyin.boot.utils;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 何义祈安
 */
@Component
public class QiniuUtils {
    /**
    *@Description 类似账号，在七牛云个人空间---密钥管理获取
    **/
    @Value("${qiniu.accessKey}")
    private String accessKey;
    /**
     *@Description 密码
     **/
    @Value("${qiniu.secretKey}")
    private String secretKey;
    /**
     *@Description 存储空间
     **/
    @Value("${qiniu.bucket}")
    private String bucket;
    /**
     *@Description 七牛云图片服务器域名（有效1个月）
     **/
    @Value("${qiniu.prefix}")
    private String url;

     /**
      * 处理多文件
      * @param multipartFiles
      * @return
      */
     public Map<String, List<String>> uploadImages(MultipartFile[] multipartFiles){
         Map<String, List<String>> map = new HashMap<>();
         List<String> imageUrls = new ArrayList<>();
         for(MultipartFile file : multipartFiles){
            imageUrls.add(uploadImageQiniu(file));
         }
         map.put("imageUrl",imageUrls);
         return map;
     }

     /**
      * 上传图片到七牛云
      * @param multipartFile
      * @return
      */
     public String uploadImageQiniu(MultipartFile multipartFile){
         try {
             //1、获取文件上传的流
             byte[] fileBytes = multipartFile.getBytes();
             //2、创建日期目录分隔
             SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
             String datePath = dateFormat.format(new Date());
             //3、获取文件名
             String originalFilename = multipartFile.getOriginalFilename();
             String fileName = getRandomImgName(multipartFile.getOriginalFilename());
//             String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
//                 String filename = datePath+"/"+ UUID.randomUUID().toString().replace("-", "")+ suffix;
             //4.构造一个带指定 Region 对象的配置类
             //Region.南(根据自己的对象空间的地址选
             Configuration cfg = new Configuration(Region.huanan());
             UploadManager uploadManager = new UploadManager(cfg);
             //5.获取七牛云提供的 token
             Auth auth = Auth.create(accessKey, secretKey);
             String upToken = auth.uploadToken(bucket);
             Response response = uploadManager.put(fileBytes, fileName, upToken);
             //解析上传成功的结果
             DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
             return url+fileName;
             } catch (IOException e) {
                 e.printStackTrace();
             }
         return null;
     }


    /**
     * @Description: 生成唯一图片名称
     * @Param: fileName
     * @return: 云服务器fileName
     */
    public static String getRandomImgName(String fileName) {
        int index = fileName.lastIndexOf(".");

        if (fileName.isEmpty() || index == -1){
            throw new IllegalArgumentException();
        }
        // 获取文件后缀
        String suffix = fileName.substring(index).toLowerCase();
        // 生成UUID
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        // 拼接新的名称
        return uuid + suffix;
    }
}

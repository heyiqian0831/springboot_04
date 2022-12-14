package com.fuyin.boot.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: PictureController
 * @Author: 何义祈安
 * @Description: 图片上传控制层
 * @Date: 2022/11/25 19:26
 * @Version: 1.0
 */
@CrossOrigin
@RestController
@RequestMapping("/picture")
public class PictureController {
    @Autowired
    private QiniuUtils qiniuUtils;

    @PostMapping(value = "/upload")
    public ResponseResult uploadPictures(@RequestParam(value = "file") MultipartFile[] multipartFile){
        System.out.println("开始上传");
        if(ObjectUtils.isEmpty(multipartFile)){
            return new ResponseResult(1001,"上传失败");
        }
        Map<String, List<String>> uploadImagesUrl = qiniuUtils.uploadImages(multipartFile);
        return new ResponseResult(200,"操作成功",uploadImagesUrl);
    }


    @PostMapping("/uploadSingle")
    public ResponseResult uploadSingle(@RequestParam("file") MultipartFile file){
        //上传文件，上传到哪呢？图片服务器七牛云
        //把图片发放到距离图片最近的服务器上，降低我们自身服务器的带宽消耗
        String imagePath = qiniuUtils.uploadImageQiniu(file);
        if (!imagePath.isEmpty()){
            //上传成功
            return new ResponseResult(200,"上传成功",imagePath);
        }
        return new ResponseResult(5001,"图片上传失败");
    }
}

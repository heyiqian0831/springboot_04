package com.fuyin.boot.controller;

import com.fuyin.boot.model.param.*;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName AdminController
 * @Description 管理员业务
 * @Author 何义祈安
 * @Date 2022/9/13 15:18
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @RequestMapping(value = "/adminControllerTest")
    public ResponseResult test(){
        String test = "AdminController类测试，不需要登录和权限";
        return new ResponseResult(200,"测试成功,Welcome SpringBoot",test);
    }

    /**
    *@Description 管理员登录，传入手机号密码
    *@Param [adminLoginParam]
    *@return com.fuyin.boot.result.ResponseResult
    **/
    @PostMapping(value = "/login")
    public ResponseResult login(@RequestBody AdminLoginParam adminLoginParam, HttpServletRequest request){
        return adminService.login(adminLoginParam,request);
    }

    /**
    *@Description 管理员获取所有用户
    **/
    @GetMapping(value = "/AllUser")
    @PreAuthorize("hasAuthority('admin:management:user')")
    public ResponseResult getAllUser(HttpServletRequest request){
        return adminService.getAllUser(request);
    }

    /**
    *@Description 分页查询 TODO后期要对数据库添加索引
    **/
    @GetMapping(value = "/getUserPage")
    @PreAuthorize("hasAuthority('admin:management:user')")
    public ResponseResult getUserPage(@RequestBody UserSelectPage userSelectPage){
        return adminService.getUserPage(userSelectPage);
    }

    /**
    *@Description 通过用户名和手机号查找某个用户
    **/
    @PostMapping(value = "/OneUser")
    @PreAuthorize("hasAuthority('admin:management:user')")
    public ResponseResult getOne(@RequestBody UsernameAndPhoneNumberParam userNameAndPhoneNumberParam){
        return adminService.getOneByUsernameAndPhoneNumber(userNameAndPhoneNumberParam);
    }

    /**
     *@Description 新增用户，输入昵称，手机，角色 user+user_role_relation
     **/
    @PostMapping(value = "/insertUser")
    @PreAuthorize("hasAuthority('admin:management:user')")
    public ResponseResult insertNewOneUser(@RequestBody InsertUserParam insertUserParam){
        return adminService.insertOne(insertUserParam);
    }

    /**
     *@Description 修改用户信息（角色权限表没确定）传入参数：用户id，用户新角色name
     **/
    @PostMapping(value = "/updateUser")
    @PreAuthorize("hasAuthority('admin:management:user')")
    public ResponseResult updateUser(@RequestBody InsertUserParam insertUserParam){
        return adminService.update(insertUserParam);
    }

    /**
     *@Description 删除用户,仅操作user表
     **/
    @PostMapping(value = "/delete")
    @PreAuthorize("hasAuthority('admin:management:user')")
    public ResponseResult deleteUser(@RequestBody UserDeleteParam userDelete){
        return adminService.deleteUser(userDelete.getId());
    }

    /**
     *@Description 退出登录（完成jwt拦截器才能用）9.19jwtbug修完
     **/
    @RequestMapping(value = "adminLogout")
    @PreAuthorize("hasAuthority('admin:management:user')")
    public ResponseResult logout(){
        return adminService.logout();
    }
}

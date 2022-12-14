package com.fuyin.boot.controller;

import com.fuyin.boot.mgb.entity.UserDO;
import com.fuyin.boot.model.param.InputEnterpriceParam;
import com.fuyin.boot.model.param.UserLoginParam;
import com.fuyin.boot.model.param.UserOldNewPasswordParam;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.UserService;
import com.fuyin.boot.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName UserController
 * @Description
 * @Author 何义祈安
 * @Date 2022/8/10 17:40
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/test")
    public ResponseResult test(){
        String test = "测试：任何时候都能访问";
        return new ResponseResult(200,"测试成功,Welcome SpringBoot",test);
    }

//  权限：[system:dept:index, system:dept:test, system:dept:develop]
    @RequestMapping(value = "/hello")
    @PreAuthorize("hasAuthority('system:dept:index')")  //表示用户访问该请求需要有hello权限（推荐）
//    @PreAuthorize("@PermissionExpressionRoot.hasAuthority('system:dept:index')")
    //@PreAuthorize("hasAnyAuthority('admin','test','system:dept:list')")  //可以传入多个权限，只要用户有其中任意一个权限都可以访问对应资源。
    //@PreAuthorize("hasRole('system:dept:list')")  //要有对应角色才可访问，内部会把传入的参数 拼接上 'ROLE_' 后再比较。所以用户对应的权限也要有 'ROLE_' 这个前缀才可以。
    //@PreAuthorize("hasAnyRole('admin','system:dept:list')")  //有任意的角色就可以访问。内部会把传入的参数拼接上 'ROLE_' 后再比较。所以用户对应的权限也要有 'ROLE_' 这个前缀才可。
    public ResponseResult hello(){
        String hello = "测试配置为只有登录认证之后才能访问";
        return new ResponseResult(200,"测试成功",hello);
    }

    /**
    *@Description 登录接口
    *@Param [userDO]
    *@return com.fuyin.boot.result.ResponseResult
    **/
    @PostMapping("/login")
    public ResponseResult login(@RequestBody UserLoginParam userLoginParam,HttpServletRequest request){
        return userService.login(userLoginParam,request);
    }

    /**
    *@Description 注册接口
    *@Param [UserDO: PhoneNumber,Nickname,Password]
    *@return com.fuyin.boot.result.ResponseResult
    **/
    @PostMapping(value = "/register")
    public ResponseResult register(@RequestBody UserDO usersDO){
        return userService.register(usersDO);
    }

    /**
    *@Description 用户修改昵昵称手机号
    *@return com.fuyin.boot.result.ResponseResult
    **/
    @PostMapping(value = "/update")
    @PreAuthorize("hasAuthority('system:updateNamePhone:update')")
    public ResponseResult update(@RequestBody UserDO userDO){
        return userService.updateUsernamePhoneNumberSet(userDO);
    }

    /**
    *@Description 用户修改密码接口
    **/
    @PostMapping(value = "/updatePassword")
    @PreAuthorize("hasAuthority('system:updatePassword:update')")
    public ResponseResult updatePassword(@RequestBody UserOldNewPasswordParam oldNewPassword){
        return userService.updatePassword(oldNewPassword);
    }

    /**
    *@Description 用户推出登录接口
    **/
    @RequestMapping(value = "/logout")
    @PreAuthorize("hasAuthority('system:logout:index')")
    public ResponseResult logout(){
        return loginService.logout();
    }




}

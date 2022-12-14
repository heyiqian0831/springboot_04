package com.fuyin.boot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fuyin.boot.mgb.entity.AdminDO;
import com.fuyin.boot.mgb.entity.UserDO;
import com.fuyin.boot.model.param.AdminLoginParam;
import com.fuyin.boot.model.param.InputEnterpriceParam;
import com.fuyin.boot.model.param.UserLoginParam;
import com.fuyin.boot.model.param.UserOldNewPasswordParam;
import com.fuyin.boot.result.ResponseResult;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 何义祈安
 */
public interface UserService extends IService<UserDO> , UserDetailsService {
    /**
    *@Description 用户登录
    **/
    ResponseResult login(UserLoginParam userLoginParam, HttpServletRequest request);

    /**
    *@Description 刷新用户最后登录时间
    **/
    int setUserLastLoginTime(UserDO userDO);

    /**
    *@Description 注册新用户
    **/
    ResponseResult register(UserDO usersDO);

    /**
     *@Description 更新昵称和手机号
     **/
    ResponseResult updateUsernamePhoneNumber(UserDO userDO);

    ResponseResult updateUsernamePhoneNumberSet(UserDO userDO);

    /**
    *@Description 更新用户密码，传入两个参数
    **/
    ResponseResult updatePassword(UserOldNewPasswordParam oldNewPassword);

}

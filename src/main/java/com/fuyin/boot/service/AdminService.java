package com.fuyin.boot.service;

import com.fuyin.boot.mgb.entity.AdminDO;
import com.fuyin.boot.model.param.AdminLoginParam;
import com.fuyin.boot.model.param.InsertUserParam;
import com.fuyin.boot.model.param.UserSelectPage;
import com.fuyin.boot.model.param.UsernameAndPhoneNumberParam;
import com.fuyin.boot.result.ResponseResult;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 何义祈安
 */
public interface AdminService extends UserDetailsService {
    /**
     * 管理员登录
    **/
    ResponseResult login(AdminLoginParam adminLoginParam, HttpServletRequest request);
    /**
     * 管理员获取所有用户
     **/
    ResponseResult getAllUser(HttpServletRequest request);
    /**
     * 分页查询用户
     **/
    ResponseResult getUserPage(UserSelectPage userSelectPage);
    /**
     * 管理员根据用户昵称、手机号查询用户信息
     **/
    ResponseResult getOneByUsernameAndPhoneNumber(UsernameAndPhoneNumberParam usernameAndPhoneNumberParam);

    /**
     * 管理员删除普通用户
     **/
    ResponseResult deleteUser(Long userId);

    /**
     * 更新管理员账号最后登录时间
     **/
    int setAdminLastLoginTime(AdminDO adminDO);
    /**
     * 管理员退出登录
     * **/
    ResponseResult logout();

    /**
     * 管理员新增一个用户
     **/
    ResponseResult insertOne(InsertUserParam insertUserParam);

    /**
     * 管理员修改用户信息
     **/
    ResponseResult update(InsertUserParam insertUserParam);


}

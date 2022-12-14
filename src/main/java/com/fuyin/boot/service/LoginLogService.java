package com.fuyin.boot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fuyin.boot.mgb.entity.LoginLogDO;
import com.fuyin.boot.model.param.ListLog4Page;
import com.fuyin.boot.result.ResponseResult;

public interface LoginLogService extends IService<LoginLogDO> {
    /**
     *@Description 插入用户登录日志
     **/
    void insertLog(LoginLogDO loginLogDO);
    /**
     *@Description 分页查询登录日志
     **/
    ResponseResult getLogPage(ListLog4Page page);
}

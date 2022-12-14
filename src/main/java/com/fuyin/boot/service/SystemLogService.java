package com.fuyin.boot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fuyin.boot.mgb.entity.SystemLogDO;
import com.fuyin.boot.model.param.ListLog4Page;
import com.fuyin.boot.result.ResponseResult;

public interface SystemLogService extends IService<SystemLogDO> {
    /**
     *@Description 插入操作系统数据
     **/
    void insertLog(SystemLogDO systemLogDO);
    /**
    *@Description 分页获取操作系统日志
    **/
    ResponseResult getLogPage(ListLog4Page page);
}

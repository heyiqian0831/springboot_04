package com.fuyin.boot.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuyin.boot.mgb.entity.SystemLogDO;
import com.fuyin.boot.mgb.mapper.SystemLogMapper;
import com.fuyin.boot.model.param.ListLog4Page;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.SystemLogService;
import org.springframework.stereotype.Service;

/**
 * @ClassName: SystemLogServiceImpl
 * @Author: 何义祈安
 * @Description: TODO
 * @Date: 2022/11/22 22:21
 * @Version: 1.0
 */
@Service
public class SystemLogServiceImpl extends ServiceImpl<SystemLogMapper,SystemLogDO> implements SystemLogService {

    /**
    *@Description 新增日志数据
    **/
    @Override
    public void insertLog(SystemLogDO systemLogDO) {
        this.save(systemLogDO);
    }
    /**
     *@Description 分页获取数据
     **/
    @Override
    public ResponseResult getLogPage(ListLog4Page page) {
        IPage<SystemLogDO> queryPage = new Page<>();
        //查询第几页
        queryPage.setCurrent(page.getPageNum());
        //每页条数
        queryPage.setSize(page.getPageSize());
        IPage<SystemLogDO> page1 = this.page(queryPage);
        return new ResponseResult(200,"操作成功",page1.getRecords());
    }
}

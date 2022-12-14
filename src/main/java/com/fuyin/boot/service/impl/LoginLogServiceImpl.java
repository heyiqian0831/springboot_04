package com.fuyin.boot.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuyin.boot.mgb.entity.LoginLogDO;
import com.fuyin.boot.mgb.mapper.LoginLogMapper;
import com.fuyin.boot.model.param.ListLog4Page;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.LoginLogService;
import org.springframework.stereotype.Service;

/**
 * @ClassName: LoginLogServiceImpl
 * @Author: 何义祈安
 * @Description: TODO
 * @Date: 2022/11/22 22:22
 * @Version: 1.0
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLogDO> implements LoginLogService {

    /**
    *@Description 插入用户登录日志
     * 因为需要有成功或失败的数据，所以要放在token验证后
    **/
    @Override
    public void insertLog(LoginLogDO loginLogDO) {
        try {
            boolean save = this.save(loginLogDO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     *@Description 分页查找
     **/
    @Override
    public ResponseResult getLogPage(ListLog4Page page) {
        IPage<LoginLogDO> logPage = new Page<>();
        //查询第几页
        logPage.setCurrent(page.getPageNum());
        //每页条数
        logPage.setSize(page.getPageSize());
        IPage<LoginLogDO> pageRecord = this.page(logPage);
        return new ResponseResult(200,"操作成功",pageRecord.getRecords());
    }
}

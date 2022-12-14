package com.fuyin.boot.controller;

import com.fuyin.boot.model.param.ListLog4Page;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: SystemLogController
 * @Author: 何义祈安
 * @Description: 操作日志控制类
 * @Date: 2022/11/22 22:50
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/systemLog")
public class SystemLogController {
    @Autowired
    private SystemLogService systemLogService;

    /**
     *@Description 分页获取操作系统日志
     **/
    @PostMapping(value = "/getLogPage")
    @PreAuthorize("hasAuthority('admin:management:systemLog')")
    public ResponseResult getLogPage(@RequestBody ListLog4Page page){
        return systemLogService.getLogPage(page);
    }
}

package com.fuyin.boot.controller;

import com.fuyin.boot.model.param.ListLog4Page;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: LoginLogController
 * @Author: 何义祈安
 * @Description: 登录日志控制类
 * @Date: 2022/11/22 22:51
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/loginLog")
public class LoginLogController {
    @Autowired
    private LoginLogService logService;

    @RequestMapping(value = "/listLogPage")
    @PreAuthorize("hasAuthority('admin:management:systemLog')")
    public ResponseResult getLogPage(@RequestBody ListLog4Page page){
        return logService.getLogPage(page);
    }


}

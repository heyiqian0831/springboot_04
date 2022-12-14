package com.fuyin.boot.utils;

import com.fuyin.boot.domain.LoginAdmin;
import com.fuyin.boot.mgb.entity.SystemLogDO;
import com.fuyin.boot.model.domain.UserAgentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @ClassName: Log4AddUtil
 * @Author: 何义祈安
 * @Description: 新增日志工具类
 * @Date: 2022/11/23 17:35
 * @Version: 1.0
 */
@Component
public class Log4AddUtil {

    public SystemLogDO getLogInfo(String type) {
        SystemLogDO systemLogDO = new SystemLogDO();
        systemLogDO.setOperationType(type);
        //获取操作人员
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) authentication.getPrincipal();
        String name = user.getUsername();
        systemLogDO.setOperationName(name);

        //获取操作IP 参数：把request放在UsernamePasswordAuthenticationToken的参数里
        HttpServletRequest request = (HttpServletRequest) authentication.getCredentials();
        UserAgentUtil userAgentUtil = new UserAgentUtil();
        UserAgentInfo userAgent = userAgentUtil.getUserAgentInfo(request);
        systemLogDO.setIp(userAgent.getIp());
        //获取请求方式
        systemLogDO.setRequestMode(userAgent.getMethod());
        //获取操作时间
        systemLogDO.setOperationDate(new Date());
        return systemLogDO;
    }

    /**
    *@Description 用户管理日志
    **/
    public SystemLogDO userManageLog(String type){
        SystemLogDO logInfo = this.getLogInfo(type);
        logInfo.setOperationModule("用户管理");
        return logInfo;
    }
    /**
     *@Description 角色管理模块
     **/
    public SystemLogDO roleManageLog(String type){
        SystemLogDO logInfo = this.getLogInfo(type);
        logInfo.setOperationModule("角色管理");
        return logInfo;
    }
    /**
     *@Description 内容管理模块
     **/
    public SystemLogDO contentManageLog(String type){
        SystemLogDO logInfo = this.getLogInfo(type);
        logInfo.setOperationModule("内容管理");
        return logInfo;
    }
    /**
     *@Description 系统日志模块
     **/
    public SystemLogDO logManageLog(String type){
        SystemLogDO logInfo = this.getLogInfo(type);
        logInfo.setOperationModule("系统日志");
        return logInfo;
    }
    /**
     *@Description 债务重组模块
     **/
    public SystemLogDO debtManageLog(String type){
        SystemLogDO logInfo = this.getLogInfo(type);
        logInfo.setOperationModule("债务重组");
        return logInfo;
    }
    /**
     *@Description 业务重组模块
     **/
    public SystemLogDO businManageLog(String type){
        SystemLogDO logInfo = this.getLogInfo(type);
        logInfo.setOperationModule("业务重组");
        return logInfo;
    }

}

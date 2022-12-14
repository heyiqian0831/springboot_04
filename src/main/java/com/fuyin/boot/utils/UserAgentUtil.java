package com.fuyin.boot.utils;

import com.fuyin.boot.domain.LoginUser;
import com.fuyin.boot.model.domain.UserAgentInfo;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: RequestParseUtil
 * @Author: 何义祈安
 * @Description: 解析request获取对应信息工具类
 * @Date: 2022/11/23 13:23
 * @Version: 1.0
 */
@Component
public class UserAgentUtil {
    /**
    *@Description 获取浏览器+操作系统+ip
    **/
    public UserAgentInfo getUserAgentInfo(HttpServletRequest request) {
        UserAgentInfo userAgentInfo = new UserAgentInfo();
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("user-agent"));
        //获取硬件
        userAgentInfo.setHardwareType(userAgent.getOperatingSystem().getDeviceType().toString());
        //获取系统
        userAgentInfo.setOs(userAgent.getOperatingSystem().getName());
        //获取浏览器
        userAgentInfo.setBrowserType(userAgent.getBrowser().toString());
        //获取IP
        userAgentInfo.setIp(HttpUtil.getIpAddress(request));
        //获取请求类型
        userAgentInfo.setMethod(request.getMethod());
        return userAgentInfo;
    }


    /**
    *@Description 单个返回

    **/
}

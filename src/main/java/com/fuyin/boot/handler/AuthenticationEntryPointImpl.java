package com.fuyin.boot.handler;

import com.alibaba.fastjson.JSON;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName AuthenticationEntryPointImpl
 * @Description 认证失败处理器
 * @Author 何义祈安
 * @Date 2022/8/19 13:52
 * @Version 1.0
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //HttpStatus: spring提供的枚举类
        ResponseResult result = new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "认证失败请重新登录");
        //将result对象转换成json字符串
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response,json);
    }
}

package com.fuyin.boot.expression;

import com.fuyin.boot.domain.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName PermissionExpressionRoot
 * @Description 自定义权限验证类,在controller接口中前端调用接口前判断权限
 * @Author 何义祈安
 * @Date 2022/9/5 15:29
 * @Version 1.0
 */
@Component("PermissionExpressionRoot")
public class PermissionExpressionRoot {

    public boolean hasAuthority(String authority){
        //获取当前用户权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<String> permissions = loginUser.getPermissions();
        //判断用户权限中是否存在authority
        return permissions.contains(authority);
    }

}

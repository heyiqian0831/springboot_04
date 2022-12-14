package com.fuyin.boot.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fuyin.boot.mgb.entity.UserDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName loginUser
 * @Description
 * @Author 何义祈安
 * @Date 2022/8/15 16:11
 * @Version 1.0
 */
@Data
@NoArgsConstructor
public class LoginUser implements UserDetails {

    private UserDO userDO;

    //存储权限信息
    private List<String> permissions;

    public LoginUser(UserDO userDO, List<String> permissions) {
        this.userDO = userDO;
        this.permissions = permissions;
    }

    @JSONField(serialize = false) //该成员变量不会被序列化到redis中
    private List<SimpleGrantedAuthority> authorities;

    //获取用户包含的权限，返回权限集合，权限是一个继承了GrantedAuthority的对象；
    //为什么要完成自定义对象permissions 到authorities：
        //因为框架当中是不会调用自定义对象，loginUser因为实现了框架中的类，所以框架中会调用重写的方法获取权限信息
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(!Objects.isNull(authorities)){
            return authorities;
        }
        //把permissions中字符串类型的权限信息转换成GrantedAuthority对象存入authorities中
//        1.简单方法：
//        authorities = new ArrayList<>();
//        for (String permission : permissions) {
//            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permission);
//            authorities.add(authority);
//        }

//        2.函数方法：
        authorities = permissions.stream().
                map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return authorities;
    }

    //用于获取密码和用户名
    @Override
    public String getPassword() {
        return userDO.getPassword();
    }

    //用于获取密码和用户名
    @Override
    public String getUsername() {
        return userDO.getUsername();
    }

    //方法返回boolean类型，用于判断账户是否未过期，未过期返回true反之返回false；
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //方法用于判断账户是否未锁定；
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //用于判断用户凭证是否没过期，即密码是否未过期；
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //方法用于判断用户是否可用
    @Override
    public boolean isEnabled() {
        return true;
    }

}

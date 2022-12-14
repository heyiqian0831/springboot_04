package com.fuyin.boot.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fuyin.boot.mgb.entity.AdminDO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName LoginAdmin
 * @Description 管理员登录实体类 用户+权限集合
 * @Author 何义祈安
 * @Date 2022/9/14 21:16
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@ToString
public class LoginAdmin implements UserDetails {
    private AdminDO adminDO;

    /*
    *@Description 存储权限信息
    **/
    private List<String> permissions;

    public LoginAdmin(AdminDO adminDO, List<String> permissions) {
        this.adminDO = adminDO;
        this.permissions = permissions;
    }

    @JSONField(serialize = false) //该成员变量不会被序列化到redis中
    private List<SimpleGrantedAuthority> authorities;

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
        return adminDO.getPassword();
    }

    //用于获取密码和用户名
    @Override
    public String getUsername() {
        return adminDO.getUsername();
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

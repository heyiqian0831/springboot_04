package com.fuyin.boot.enums;
/**
 *@author 何义祈安
 *@Description 用户状态
 **/
public enum Roles {
    /**
    *@Description 超级管理员
    **/
    SUPER_ADMIN("super_admin","超级管理员持有所有后台权限"),
    /**
     *@Description 角色管理员
     **/
    ROLE_ADMIN("role_admin","角色管理员"),
    /**
     *@Description 日志管理员
     **/
    LOG_ADMIN("log_admin","日志管理员"),
    /**
     *@Description 普通用户
     **/
    COMMON_USER("common_user","普通用户"),
    /**
     *@Description 可以获取公共信息的用户
     **/
    PUBLIC_INFO_USER("public_info_user","可以获取公共信息的用户");

    /**
    *@Description 角色名称
    **/
    private String name;

    /**
    *@Description 角色描述
    **/
    private String description;

    Roles(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Roles fromString(String name) {
        //如果名称不为空
        if (name != null) {
            //循环枚举类所有的值
            for (Roles role : Roles.values()) {
                //忽略大小写，当参数name等于枚举中的name
                if (name.equalsIgnoreCase(role.name)) {
                    //返回枚举中的对象
                    return role;
                }
                if(name.equalsIgnoreCase(role.description)){
                    return role;
                }
            }
        }
        //如果名字为空，返回默认枚举中的对象
        return COMMON_USER;
    }

}

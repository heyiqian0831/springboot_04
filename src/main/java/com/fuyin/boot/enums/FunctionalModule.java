package com.fuyin.boot.enums;
/**
*@Description 按照项目功能模块区分具体模块
*@Param
*@return
**/
public enum FunctionalModule {
    /**
     *@Description 模块
     * 角色管理  Role management
     * 用户管理  user management
     * 系统日志  system log
     * 债务重组  Debt restructure
     * 业务重组  Business restructure
     **/
    ROLE_MANAGEMENT("角色管理","角色管理"),

    USER_MANAGEMENT("用户管理","用户管理"),

    SYSTEM_LOG("系统日志","系统日志"),

    DEBT_RESTRUCTURE("债务重组","债务重组"),

    BUSINESS_RESTRUCTURE("业务重组","业务重组"),

    UNKNOWN("unknown","未知");

    private String code;
    private String message;

    FunctionalModule(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static FunctionalModule fromString(String code) {
        //如果名称不为空
        if (code != null) {
            //循环枚举类所有的值
            for (FunctionalModule module : FunctionalModule.values()) {
                //忽略大小写，当参数name等于枚举中的name
                if (code.equalsIgnoreCase(module.code)) {
                    //返回枚举中的对象
                    return module;
                }
            }
        }
        //如果名字为空，返回默认枚举中的对象
        return UNKNOWN;
    }
}

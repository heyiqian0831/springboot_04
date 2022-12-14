package com.fuyin.boot.enums;
/**
*@author 何义祈安
*@Description 用户状态
**/
public enum DataStatus {
    /**
    * 未知
    **/
    UNKNOWN("UNKNOWN", "未知"),

    /**
    * 启用
    **/
    ENABLE("ENABLE", "启用"),

    /**
    * 禁用
    **/
    DISABLE("DISABLE", "禁用");


    private String code;

    private String message;

    private DataStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static DataStatus fromString(String code) {
        if (code != null) {
            for (DataStatus status : DataStatus.values()) {
                if (code.equalsIgnoreCase(status.code)) {
                    return status;
                }
            }
        }
        return null;
    }

}

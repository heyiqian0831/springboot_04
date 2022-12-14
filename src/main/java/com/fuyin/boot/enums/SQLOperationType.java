package com.fuyin.boot.enums;

/**
 * @ClassName: SQLOperationType
 * @Author: 何义祈安
 * @Description: 数据库操作类型
 * @Date: 2022/11/23 17:26
 * @Version: 1.0
 */
public enum SQLOperationType {
    /**
     *@Description 新增
     **/
    INSERT("insert","新增"),
    /**
     *@Description 删除
     **/
    DELETE("delete","删除"),
    /**
     *@Description 修改
     **/
    UPDATE("update","修改"),
    /**
     *@Description 查询
     **/
    SELECT("select","查询"),

    UNKNOWN("unknow","未知");

    private String code;

    private String message;

    SQLOperationType(String code, String message) {
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

    public static SQLOperationType fromString(String code) {
        //如果名称不为空
        if (code != null) {
            //循环枚举类所有的值
            for (SQLOperationType type : SQLOperationType.values()) {
                //忽略大小写，当参数name等于枚举中的name
                if (code.equalsIgnoreCase(type.code)) {
                    //返回枚举中的对象
                    return type;
                }
            }
        }
        //如果名字为空，返回默认枚举中的对象
        return UNKNOWN;
    }
}

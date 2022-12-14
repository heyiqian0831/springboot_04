package com.fuyin.boot.enums;

/**
 *@ClassName: ErrorCode
 *@author 何义祈安
 *@Description: 错误代码
 *@Version: 1.0
 */
public enum ErrorCode {
    /**
    *@Description
    **/
    NOT_MODIFIED(304, "未修改"),

    BAD_REQUEST(400, "请求或请求参数错误"),

    UNAUTHORIZED(401, "用户没有登录"),

    FORBIDDEN(403, "用户没有权限"),

    NOT_FOUND(404, "资源未找到"),

    METHOD_NOT_ALLOWED(405, "客户端请求中的方法被禁止"),

    REQUEST_OVERTIME(408, "请求超时"),

    UNSUPPORTED_MEDIA_TYPE(415, "服务器无法处理请求附带的媒体格式"),

    INTERNAL_SERVER_ERROR(500, "后台异常"),

    OPERATION_DATA_ERROR(5001, "操作数据错误"),

    INCONSISTENT_DATA(5002, "前后端数据不一致"),

    NEED_CONTINUE_OPERATE(100, "请用户继续操作"),

    INTERFACE_NOT_FINISH(1000, "接口未实现"),

    ENTITY_PARAM_ERROR(1001, "参数错误"),

    VALIDATE_ERROR(4001, "请求参数校验失败");

    private int code;

    private String message;

    private ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}

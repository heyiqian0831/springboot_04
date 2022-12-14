package com.fuyin.boot.enums;

/**
 * @author 何义祈安
 */

public enum PageType {
    /**
    *@Description 下载组件返回页面类型
    **/
    AB_QUOTATION("ABQuotation","AB股行情页"),

    HK_QUOTATION("HKQuotation","港股行情页"),

    US_QUOTATION("USQuotation","美股行情页"),

    QUOTATION("quotation","东方财富行情页"),

    SEARCH("search","东方财富搜索页");

    private String code;
    private String message;

    PageType(String code, String message) {
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
}

package com.fuyin.boot.enums;

/**
 * @author 何义祈安
 */
public enum StockType {
    /**
    *@Description 股票类型：美股
    **/
    US_STOCK("USStock","美股"),
    /**
     *@Description 股票类型：港股
     **/
    HK_STOCK("HKStock","港股"),
    /**
     *@Description 股票类型：国内AB股
     **/
    AB_STOCK("ABStock","国内AB股"),
    /**
    *@Description 未知股票
    **/
    UNKNOWN("UNKNOWN","未知股票类型");

    private String code;

    private String message;

    StockType(String code, String message) {
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

    public static StockType fromString(String code) {
        //如果名称不为空
        if (code != null) {
            //循环枚举类所有的值
            for (StockType stockType : StockType.values()) {
                //忽略大小写，当参数name等于枚举中的name
                if (code.equalsIgnoreCase(stockType.code)) {
                    //返回枚举中的对象
                    return stockType;
                }
            }
        }
        //如果名字为空，返回默认枚举中的对象
        return null;
    }
}

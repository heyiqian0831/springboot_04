package com.fuyin.boot.enums;

/**
 * @author 何义祈安
 */

public enum DebtTableType {
    /**
     *@Description 债务重组表类型
     **/
    CAPACITY("capacity","负债能力表"),

    PROFITABILITY("profitability","盈利能力表"),

    CASH_FLOW("cashFlow","现金流量表");

    private String code;
    private String message;

    DebtTableType(String code, String message) {
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

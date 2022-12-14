package com.fuyin.boot.enums;

public enum DebtSchemeContentType {
    /**
     *@Description 债务重组表类型
     * assets_settle_debts 以资产清偿债务
     * debt_to_capital 债务转资本
     * modify_debt_conditions 修改其他债务条件
     * combination_three_debt 债务三种方式组合
     **/
    ASSETS_SETTLE_DEBTS("assets_settle_debts","以资产清偿债务"),

    DEBT_TO_CAPITAL("debt_to_capital","债务转资本"),

    MODIFY_DEBT_CONDITIONS("modify_debt_conditions","修改其他债务条件"),

    COMBINATION_THREE_DEBT("combination_three_debt","债务三种方式组合");

    private String code;
    private String message;

    DebtSchemeContentType(String code, String message) {
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

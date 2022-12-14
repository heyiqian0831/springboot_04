package com.fuyin.boot.enums;

/**
 * @author 何义祈安
 */
public enum BusinessTableType {
    /**
     *@Description 业务重组相关表类型
     **/
    DEMAND_STATUS("demand_status","需求状况表"),

    AVAILABILITY("availability","供应状况表"),

    TEAM_STABILITY("team_stability","团队稳定性表"),

    DIAGNOSIS_DESC("diagnosis_desc","业务诊断描述表"),

    GROWTH_INDICATORS("growth_indicators","成长性指标表"),

    VALUATION_INDICATORS("valuation_indicators","估值比较指标表"),

    DUPONT_INDICATORS("dupont_indicators","杜邦指标表");

    private String code;
    private String message;

    BusinessTableType(String code, String message) {
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

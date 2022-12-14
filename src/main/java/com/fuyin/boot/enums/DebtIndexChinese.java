package com.fuyin.boot.enums;

/**
 * @ClassName: DebtIndexChinese
 * @Author: 何义祈安
 * @Description: 债务重组指标中文名称
 * @Date: 2022/11/28 19:30
 * @Version: 1.0
 */
public enum DebtIndexChinese {
    /**
     *@Description 下载组件返回页面类型
     **/
    CURRENT_RATIO ("currentRatio","流动比率"),

    QUICK_RATIO("quickRatio","速动比率"),

    DEBT_ASSET_RATIO("debtAssetRatio","资产负债率"),

    GROSS_PROFIT_RATIO("grossProfitRatio","毛利率"),

    NET_INTEREST_RATE("netInterestRate","净利率"),

    ROE("roe","净资产收益率"),

    RETURN_ON_TOTAL_ASSETS("returnOnTotalAssets","总资产收益率"),

    RETURN_ON_INVESTED_CAPITAL("returnOnInvestedCapital","资本回报率"),

    CASH_SALES_RATIO("cashSalesRatio","销售现金比率"),

    CASH_TO_MATURITY_RATIO("cashToMaturityRatio","现金到期债务比"),

    CASH_FLOW_LIABILITY_RATIO("cashFlowLiabilityRatio","现金流动负债比"),

    NET_PROFIT_CASH_FLOW_RATIO("netProfitCashFlowRatio","净利润现金流比率");

    private String code;
    private String message;

    DebtIndexChinese(String code, String message) {
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

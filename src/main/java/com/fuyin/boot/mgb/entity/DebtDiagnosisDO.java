package com.fuyin.boot.mgb.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 何义祈安
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "debt_diagnosis")
public class DebtDiagnosisDO implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     *   数据url
     **/
    private String detailUrl;
    /**
     *   信息日期（指标公布日期）
     **/
    private Date date;
    /**
     *   股票代码（公司代码）
     **/
    private String code;
    /**
     *   股票名称（公司名称）
     **/
    private String name;
    /**
     *   流动比率：流动资产合计/流动负债合计
     **/
    private Double currentRatio;
    /**
     *   速动比率：（流动资产合计-存货）/流动负债合计
     **/
    private Double quickRatio;
    /**
     *   资产负债率：（负债总额/资产总额）*100%
     **/

    private Double debtAssetRatio;
    /**
     *   毛利率：（营业收入－营业成本）/营业收入
     **/
    private Double grossProfitRatio;
    /**
     *   净利率：净利润÷营业收入
     **/
    private Double netInterestRate;
    /**
     *   净资产收益率：净利润÷净资产
     **/
    private Double roe;
    /**
     *   总资产收益率：净利润÷总资产
     **/
    private Double returnOnTotalAssets;
    /**
     *   资本回报率：(税后利润+财务费用)/投资资本
     **/
    private Double returnOnInvestedCapital;
    /**
     *   销售现金比率：经营活动现金净流量/销售额
     **/
    private Double cashSalesRatio;
    /**
     *   现金到期债务比：经营活动现金净流量/本期到期的债务
     **/
    private Double cashToMaturityRatio;
    /**
     *   现金流动负债比：经营活动现金净流量/期末负债总额
     **/
    private Double cashFlowLiabilityRatio;
    /**
     *   净利润现金流比率：净利润÷经营活动现金流净额
     **/
    private Double netProfitCashFlowRatio;
    /**
     *   创建时间
     **/
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    /**
     *   更新时间
     **/
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    private static final long serialVersionUID = 1L;

}

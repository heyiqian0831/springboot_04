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
@TableName(value = "debt_profitability")
public class DebtProfitabilityDO implements Serializable {
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

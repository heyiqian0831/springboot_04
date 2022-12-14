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
@TableName(value = "debt_capacity")
public class DebtCapacityDO implements Serializable {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCurrentRatio() {
        return currentRatio;
    }

    public void setCurrentRatio(Double currentRatio) {
        this.currentRatio = currentRatio;
    }

    public Double getQuickRatio() {
        return quickRatio;
    }

    public void setQuickRatio(Double quickRatio) {
        this.quickRatio = quickRatio;
    }

    public Double getDebtAssetRatio() {
        return debtAssetRatio;
    }

    public void setDebtAssetRatio(Double debtAssetRatio) {
        this.debtAssetRatio = debtAssetRatio;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

}

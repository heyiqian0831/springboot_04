package com.fuyin.boot.mgb.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "business_growth")
public class BusinessGrowthDO implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String detailUrl;

    private String code;

    private String name;

    private Double basicEpsYear3;

    private Double basicEpsA21;

    private Double basicEpsTtm;

    private Double basicEpsE22;

    private Double basicEpsE23;

    private Double basicEpsE24;

    private Double operatingIncomeYear3;

    private Double operatingIncomeA21;

    private Double operatingIncomeTtm;

    private Double operatingIncomeE22;

    private Double operatingIncomeE23;

    private Double operatingIncomeE24;
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

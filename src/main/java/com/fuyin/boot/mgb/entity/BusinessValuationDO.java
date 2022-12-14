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
@TableName(value = "business_valuation")
public class BusinessValuationDO implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String detailUrl;

    private String code;

    private String name;

    private Double peg;

    private Double priceEarningsA21;

    private Double priceEarningsTtm;

    private Double priceEarningsE22;

    private Double priceEarningsE23;

    private Double priceEarningsE24;

    private Double marketSalesA21;

    private Double marketSalesTtm;

    private Double marketSalesE22;

    private Double marketSalesE23;

    private Double marketSalesE24;
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

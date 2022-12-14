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
@TableName(value = "business_dupont")
public class BusinessDupontDO implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String detailUrl;

    private String code;

    private String name;

    private Double roeYear3;

    private Double roeA19;

    private Double roeE20;

    private Double roeE21;

    private Double netInterestRateYear3;

    private Double netInterestRateA19;

    private Double netInterestRateE20;

    private Double netInterestRateE21;
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

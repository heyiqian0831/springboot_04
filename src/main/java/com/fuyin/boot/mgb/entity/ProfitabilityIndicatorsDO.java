package com.fuyin.boot.mgb.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("profitability_indicators")
public class ProfitabilityIndicatorsDO implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String indexName;

    private String calculationFormula;

    private String standardValue;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    public ProfitabilityIndicatorsDO(String indexName, String calculationFormula, String standardValue) {
        this.indexName = indexName;
        this.calculationFormula = calculationFormula;
        this.standardValue = standardValue;
    }

    private static final long serialVersionUID = 1L;
}

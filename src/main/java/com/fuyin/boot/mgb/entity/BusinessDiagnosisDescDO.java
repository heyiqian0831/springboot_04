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
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("business_diagnosis_desc")
public class BusinessDiagnosisDescDO implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String modular;

    private String totalScore;

    private String description;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    public BusinessDiagnosisDescDO(String modular, String totalScore, String description) {
        this.modular = modular;
        this.totalScore = totalScore;
        this.description = description;
    }

    private static final long serialVersionUID = 1L;
}

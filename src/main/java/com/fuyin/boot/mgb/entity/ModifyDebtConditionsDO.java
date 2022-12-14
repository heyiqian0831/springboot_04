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
@TableName("modify_debt_conditions")
public class ModifyDebtConditionsDO implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String implementation;

    private String resultPreset;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    public ModifyDebtConditionsDO(String implementation, String resultPreset) {
        this.implementation = implementation;
        this.resultPreset = resultPreset;
    }

    private static final long serialVersionUID = 1L;
}

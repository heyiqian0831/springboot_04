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
@TableName("growth_indicators")
public class GrowthIndicatorsDO implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String modular;

    private String index;

    private String scoringCriteria;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    public GrowthIndicatorsDO(String modular, String index, String scoringCriteria) {
        this.modular = modular;
        this.index = index;
        this.scoringCriteria = scoringCriteria;
    }

    private static final long serialVersionUID = 1L;
}

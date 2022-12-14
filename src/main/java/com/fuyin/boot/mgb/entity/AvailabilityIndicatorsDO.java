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
@TableName("availability_indicators")
public class AvailabilityIndicatorsDO implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String index;

    private String scoringCriteria;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    public AvailabilityIndicatorsDO(String index, String scoringCriteria) {
        this.index = index;
        this.scoringCriteria = scoringCriteria;
    }

    private static final long serialVersionUID = 1L;
}

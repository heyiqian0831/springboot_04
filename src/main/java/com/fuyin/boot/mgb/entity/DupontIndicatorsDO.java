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
@TableName("dupont_indicators")
public class DupontIndicatorsDO implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String modular;

    private String index;

    private String scoringCriteria;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    private static final long serialVersionUID = 1L;
}

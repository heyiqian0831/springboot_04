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
@TableName("team_stability_indicators")
public class TeamStabilityIndicatorsDO implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String stability;

    private String scoringCriteria;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    public TeamStabilityIndicatorsDO(String stability, String scoringCriteria) {
        this.stability = stability;
        this.scoringCriteria = scoringCriteria;
    }

    private static final long serialVersionUID = 1L;
}

package com.fuyin.boot.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: OperationAbilityVO
 * @Author: 何义祈安
 * @Description: 业务诊断描述返回前端实体类：经营能力分数+描述； 经营水平分数+描述
 * @Date: 2022/11/27 8:17
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationAbilityVO {
    /**
    *@Description 总分（经营能力分析分数+经营水平分数）
    **/
    private Integer totalScore;
    /**
     *@Description 经营能力分析分数
     **/
    private Integer abilityScore;
    /**
     *@Description 经营能力诊断描述
     **/
    private String abilityDesc;
    /**
     *@Description 经营水平分数
     **/
    private Integer levelScore;
    /**
     *@Description 经营水平诊断描述
     **/
    private String levelDesc;
}

package com.fuyin.boot.model.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName: BussinessIndicatorsAllParam
 * @Author: 何义祈安
 * @Description: 全部业务诊断指标后端接收类
 * @Date: 2022/11/21 20:21
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BusinessIndicatorsAllParam {
    /**
     *  paramId
     **/
    private Long id;
    /**
    *  指标：需求状况表 + 供应状况表 + 成长性指标表 + 估值比较指标表 + 杜邦指标表
    **/
    private String index;
    /**
     *  评分标准：需求状况表 + 供应状况表 + 成长性指标表 + 估值比较指标表 + 杜邦指标表
     **/
    private String scoringCriteria;
    /**
     *  模块：成长性指标表 + 估值比较指标表 + 杜邦指标 + 业务诊断描述设置表
     **/
    private String modular;
    /**
     *  稳定性：团队稳定性表
     **/
    private String stability;
    /**
     *  总分值：业务诊断描述设置表表
     **/
    private String totalScore;
    /**
     *  描述：业务诊断描述设置表表
     **/
    private String description;
    /**
     *  类型：是哪个表的数据
     **/
    private Object type;


}

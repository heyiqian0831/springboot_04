package com.fuyin.boot.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: ProfitabilityResultVO
 * @Author: 何义祈安
 * @Description: TODO
 * @Date: 2022/11/26 11:54
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CashFlowResultVO {
    /**
     *@Description 该数据在数据库中的唯一标识
     **/
    private Long CFD_id;
    /**
     *@Description 指标名称
     **/
    private String index;
    /**
     *@Description 分数
     **/
    private Integer score;
    /**
     *@Description 数值
     **/
    private Double value;
    /**
     *@Description 评分结果
     **/
    private String result;
}

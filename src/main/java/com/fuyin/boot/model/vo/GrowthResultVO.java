package com.fuyin.boot.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: GrowthResultVO
 * @Author: 何义祈安
 * @Description: 业务分析基础指标信息加评分信息返回对象
 * @Date: 2022/11/26 19:35
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrowthResultVO {
    /**
    *@Description DO对象的接口
    **/
    private Long BGD_id;
    /**
     *@Description 模块
     **/
    private String modular;
    /**
     *@Description 指标名称
     **/
    private String index;
    /**
     *@Description 数值
     **/
    private Double value;
    /**
     *@Description 分数
     **/
    private Integer score;
    /**
     *@Description 结果(value拼接 优秀/一般)
     **/
    private String result;
}

package com.fuyin.boot.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: ProfitabilityResultVO
 * @Author: 何义祈安
 * @Description: 带上评分结果的爬取数据对象
 * @Date: 2022/11/26 11:54
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfitabilityResultVO {
    /**
     *@Description id
     **/
    private Long PDO_id;
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

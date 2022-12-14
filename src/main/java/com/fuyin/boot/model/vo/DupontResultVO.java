package com.fuyin.boot.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: DupontResultVO
 * @Author: 何义祈安
 * @Description: 杜邦分析基本指标信息+评分结果
 * @Date: 2022/11/26 19:46
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DupontResultVO {
    /**
     *@Description DO对象的接口
     **/
    private Long BDD_id;
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

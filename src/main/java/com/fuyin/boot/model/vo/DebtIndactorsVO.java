package com.fuyin.boot.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *@ClassName: DebtIndactorsVO
 *@Author: 何义祈安
 *@Description: TODO
 *@Date: 2022/11/26 11:56
 *@Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebtIndactorsVO {
    /**
     *@Description 指标名称
     **/
    private String index;
    /**
     *@Description 数值
     **/
    private Double value;
    /**
     *@Description 评分结果
     **/
    private String result;
}

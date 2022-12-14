package com.fuyin.boot.model.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName: DebtIndicatorsAllParam
 * @Author: 何义祈安
 * @Description: 接收所有债务诊断指标
 * @Date: 2022/11/21 19:39
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DebtIndicatorsAllParam {
    /**
    *@Description id,非数据库表中id
    **/
    private Long id;
    /**
     *@Description 指标名称
     **/
    private String indexName;
    /**
     *@Description 计算公式
     **/
    private String calculationFormula;
    /**
     *@Description 标准值
     **/
    private String standardValue;
    /**
     *@Description 数据来源类型
     **/
    private Object type;
}

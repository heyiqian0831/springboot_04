package com.fuyin.boot.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: DebtResultDescVO
 * @Author: 何义祈安
 * @Description: 债务重组结果描述栏
 * @Date: 2022/11/26 14:01
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebtResultDescVO {
    /**
     *@Description 企业名称
     **/
    private String name;
    /**
    *@Description 债务重组总分数
    **/
    private Integer total;
    /**
     *@Description 负债能力分数
     **/
    private Integer capacity;
    /**
     *@Description 盈利能力表分数
     **/
    private Integer profitbility;
    /**
     *@Description 现金流分数
     **/
    private Integer cashFlow;

    private String desc = "xxx企业债务诊断总分为：24分，负债能力得分为：7分，盈利能力得分为：6分，现金流得分为：11分。";

    public String debtResult(String name,Integer capacity, Integer profitbility, Integer cashFlow) {
        this.capacity = capacity;
        this.profitbility = profitbility;
        this.cashFlow = cashFlow;
        this.total = capacity + profitbility + cashFlow;
        return name + "债务诊断总分为：" + total + "分，" +
                "负债能力得分为：" + this.capacity + "分，" +
                "盈利能力得分为：" + this.profitbility + "分，" +
                "现金流得分为：" + this.cashFlow + "分。";
    }
}

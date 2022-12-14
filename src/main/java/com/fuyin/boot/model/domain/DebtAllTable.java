package com.fuyin.boot.model.domain;

import com.fuyin.boot.mgb.entity.DebtCapacityDO;
import com.fuyin.boot.mgb.entity.DebtCashFlowDO;
import com.fuyin.boot.mgb.entity.DebtProfitabilityDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName: DebtAllTable
 * @Author: 何义祈安
 * @Description: 负债能力指标分析表 + 盈利能力诊断指标表 + 现金流诊断指标表
 * @Date: 2022/11/7 16:33
 * @Version: 1.0
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DebtAllTable {
    /**
     * id
     **/
    private Long id;

    /**
     * 负债能力指标分析表
     **/
    private DebtCapacityDO debtCapacityDO;

    /**
     * 盈利能力诊断指标表
     **/
    private DebtProfitabilityDO debtProfitabilityDO;

    /**
     * 现金流诊断指标表
     **/
    private DebtCashFlowDO debtCashFlowDO;

}

package com.fuyin.boot.model.param;

import com.fuyin.boot.model.vo.CapacityResultVO;
import com.fuyin.boot.model.vo.CashFlowResultVO;
import com.fuyin.boot.model.vo.DebtResultDescVO;
import com.fuyin.boot.model.vo.ProfitabilityResultVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: DebtDataModifiedParam
 * @Author: 何义祈安
 * @Description: 诊断状况中传回来可能被用户修改过的全部指标类
 * @Date: 2022/11/26 17:45
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebtDataModifiedParam {
    /**
    * 负债能力
    **/
    private List<CapacityResultVO> capacity;
    /**
     * 盈利能力
     **/
    private List<ProfitabilityResultVO> profitability;
    /**
     * 现金流分析
     **/
    private List<CashFlowResultVO> cashFlow;
    /**
     * 债务重组指标分数描述
     **/
    private DebtResultDescVO debtDesc;
    /**
     * 用户输入的基本信息
     **/
    private InputEnterpriceParam enterpriceParam;
}

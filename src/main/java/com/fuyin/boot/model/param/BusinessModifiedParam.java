package com.fuyin.boot.model.param;

import com.fuyin.boot.mgb.entity.OperationAbilityDO;
import com.fuyin.boot.model.vo.DupontResultVO;
import com.fuyin.boot.model.vo.GrowthResultVO;
import com.fuyin.boot.model.vo.ValuationResultVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: BusinessModifiedParam
 * @Author: 何义祈安
 * @Description: 业务重组点击生成重组方案按钮传回来的多对象封装的接收对象
 * @Date: 2022/11/26 22:29
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessModifiedParam {
    /**
     * 成长性指标集合
     **/
    private List<GrowthResultVO> growth;
    /**
     * 估值比较指标集合
     **/
    private List<ValuationResultVO> valuation;
    /**
     * 杜邦分析指标集合
     **/
    private List<DupontResultVO> dupont;
    /**
     * 经营能力表对象
     **/
    private OperationAbilityDO operationAbility;
    /**
     * 用户输入的基本信息
     **/
    private InputEnterpriceParam enterpriceParam;
}

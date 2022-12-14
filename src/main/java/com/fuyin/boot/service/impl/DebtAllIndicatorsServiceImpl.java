package com.fuyin.boot.service.impl;

import com.fuyin.boot.enums.DebtTableType;
import com.fuyin.boot.mgb.entity.CapacityIndicatorsDO;
import com.fuyin.boot.mgb.entity.CashFlowIndicatorsDO;
import com.fuyin.boot.mgb.entity.ProfitabilityIndicatorsDO;
import com.fuyin.boot.mgb.mapper.CapacityIndicatorsMapper;
import com.fuyin.boot.mgb.mapper.CashFlowIndicatorsMapper;
import com.fuyin.boot.mgb.mapper.ProfitabilityIndicatorsMapper;
import com.fuyin.boot.model.param.DebtIndicatorsAllParam;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.CapacityIndicatorsService;
import com.fuyin.boot.service.CashFlowIndicatorsService;
import com.fuyin.boot.service.DebtAllIndicatorsService;
import com.fuyin.boot.service.ProfitabilityIndicatorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: DebtAllIndicatorsServiceImpl
 * @Author: 何义祈安
 * @Description: TODO
 * @Date: 2022/11/21 18:12
 * @Version: 1.0
 */
@Service
public class DebtAllIndicatorsServiceImpl implements DebtAllIndicatorsService {
    @Autowired
    private CapacityIndicatorsMapper capacityIndicatorsMapper;
    @Autowired
    private ProfitabilityIndicatorsMapper indicatorsMapper;
    @Autowired
    private CashFlowIndicatorsMapper cashFlowIndicatorsMapper;
    @Autowired
    private CapacityIndicatorsService capacityService;
    @Autowired
    private ProfitabilityIndicatorsService profitabilityService;
    @Autowired
    private CashFlowIndicatorsService cashFlowService;
    /**
    *@Description 实现获取三个表的数据
    **/
    @Override
    public ResponseResult getDebtIndicatorAll() {
        List<CapacityIndicatorsDO> capacityIndicators = capacityIndicatorsMapper.selectList(null);
        List<ProfitabilityIndicatorsDO> profitabilityIndicators = indicatorsMapper.selectList(null);
        List<CashFlowIndicatorsDO> cashFlowIndicators = cashFlowIndicatorsMapper.selectList(null);
        Map<String,Object> map = new HashMap<>();
        map.put(DebtTableType.CAPACITY.getCode(),capacityIndicators);
        map.put(DebtTableType.PROFITABILITY.getCode(),profitabilityIndicators);
        map.put(DebtTableType.CASH_FLOW.getCode(),cashFlowIndicators);
        return new ResponseResult(200,"操作成功",map);
    }

    /**
     *@Description 新增+更新
     **/
    @Override
    public ResponseResult saveOrUpdateAll(List<DebtIndicatorsAllParam> params) {
        List<CapacityIndicatorsDO> capacityIndicators = new ArrayList<>();
        List<ProfitabilityIndicatorsDO> profitabilityIndicators = new ArrayList<>();
        List<CashFlowIndicatorsDO> cashFlowIndicators = new ArrayList<>();
        for (DebtIndicatorsAllParam param : params) {
            String type = (String) param.getType();
            if(type.equals(DebtTableType.CAPACITY.getCode())){
                capacityIndicators.add(
                        new CapacityIndicatorsDO(param.getIndexName(),param.getCalculationFormula(),param.getStandardValue()));
            }
            if(type.equals(DebtTableType.PROFITABILITY.getCode())){
                profitabilityIndicators.add(
                        new ProfitabilityIndicatorsDO(param.getIndexName(),param.getCalculationFormula(),param.getStandardValue()));
            }
            if(type.equals(DebtTableType.CASH_FLOW.getCode())){
                cashFlowIndicators.add(
                        new CashFlowIndicatorsDO(param.getIndexName(),param.getCalculationFormula(),param.getStandardValue()));
            }
            try {
                capacityService.saveOrUpdateBatch(capacityIndicators);
                profitabilityService.saveOrUpdateBatch(profitabilityIndicators);
                cashFlowService.saveOrUpdateBatch(cashFlowIndicators);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("更新失败"+e);
            }

        }
        return null;
    }


}

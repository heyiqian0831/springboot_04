package com.fuyin.boot.service.impl;

import com.fuyin.boot.enums.DebtSchemeContentType;
import com.fuyin.boot.mgb.entity.AssetsSettleDebtsDO;
import com.fuyin.boot.mgb.entity.CombinationThreeDebtDO;
import com.fuyin.boot.mgb.entity.DebtToCapitalDO;
import com.fuyin.boot.mgb.entity.ModifyDebtConditionsDO;
import com.fuyin.boot.mgb.mapper.AssetsSettleDebtsMapper;
import com.fuyin.boot.model.param.DebtSchemeParam;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: DebtSchemeManageServiceImpl
 * @Author: 何义祈安
 * @Description: 债务方案内容管理操作实现类
 * @Date: 2022/11/21 21:26
 * @Version: 1.0
 */
@Service
public class DebtSchemeManageServiceImpl implements DebtSchemeManageService {
    @Autowired
    private AssetsSettleDebtsService assetsSettleDebtsService;
    @Autowired
    private DebtToCapitalService debtToCapitalService;
    @Autowired
    private ModifyDebtConditionsService modifyDebtService;
    @Autowired
    private CombinationThreeDebtService combinationService;

    private List<AssetsSettleDebtsDO> assetsSettleDebts = new ArrayList<>();
    private List<DebtToCapitalDO> debtToCapital = new ArrayList<>();
    private List<ModifyDebtConditionsDO> modifyDebtConditions = new ArrayList<>();
    private List<CombinationThreeDebtDO> combinationThreeDebt = new ArrayList<>();

    /**
    *@Description 更新数据
    *@Param [params]
    *@return com.fuyin.boot.result.ResponseResult
    **/
    @Override
    public ResponseResult saveOrUpdate(List<DebtSchemeParam> params) {
        for (DebtSchemeParam param : params) {
            String type = (String) param.getType();
            if(type.equals(DebtSchemeContentType.ASSETS_SETTLE_DEBTS.getCode())){
                assetsSettleDebts.add(new AssetsSettleDebtsDO(param.getImplementation(), param.getResultPreset()));
            }
            if(type.equals(DebtSchemeContentType.DEBT_TO_CAPITAL.getCode())){
                debtToCapital.add(new DebtToCapitalDO(param.getImplementation(), param.getResultPreset()));
            }
            if(type.equals(DebtSchemeContentType.MODIFY_DEBT_CONDITIONS.getCode())){
                modifyDebtConditions.add(new ModifyDebtConditionsDO(param.getImplementation(), param.getResultPreset()));
            }
            if(type.equals(DebtSchemeContentType.COMBINATION_THREE_DEBT.getCode())){
                combinationThreeDebt.add(new CombinationThreeDebtDO(param.getImplementation(), param.getResultPreset()));
            }
        }
        assetsSettleDebtsService.saveOrUpdateBatch(assetsSettleDebts);
        debtToCapitalService.saveOrUpdateBatch(debtToCapital);
        modifyDebtService.saveOrUpdateBatch(modifyDebtConditions);
        combinationService.saveOrUpdateBatch(combinationThreeDebt);
        return new ResponseResult(200,"操作成功");
    }
}

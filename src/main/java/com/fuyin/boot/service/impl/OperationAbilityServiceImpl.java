package com.fuyin.boot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuyin.boot.mgb.entity.OperationAbilityDO;
import com.fuyin.boot.mgb.mapper.OperationAbilityMapper;
import com.fuyin.boot.model.vo.OperationAbilityVO;
import com.fuyin.boot.service.OperationAbilityService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @ClassName: OperationAbilityServiceImpl
 * @Author: 何义祈安
 * @Description: 经营能力业务逻辑实现类
 * @Date: 2022/11/26 22:56
 * @Version: 1.0
 */
@Service
public class OperationAbilityServiceImpl extends ServiceImpl<OperationAbilityMapper, OperationAbilityDO>
        implements OperationAbilityService {
    /**
    *@Description 补充数据
    **/
    @SneakyThrows
    @Override
    public OperationAbilityDO completeScore(OperationAbilityDO operationAbility) {
        String requirementAnalysis = operationAbility.getRequirementAnalysis();
        if(StringUtils.isNotBlank(requirementAnalysis)){
            if("供不应求".equals(requirementAnalysis)){
                operationAbility.setRaScore(0);
            }
            if("供求平衡".equals(requirementAnalysis)){
                operationAbility.setRaScore(1);
            }
            if("供大于求".equals(requirementAnalysis)){
                operationAbility.setRaScore(2);
            }
        }
        String supplyAnalysis = operationAbility.getSupplyAnalysis();
        if(StringUtils.isNotBlank(supplyAnalysis)){
            if("稳定".equals(supplyAnalysis)){
                operationAbility.setSaScore(0);
            }
            if("适当分散".equals(supplyAnalysis)){
                operationAbility.setSaScore(1);
            }
            if("不稳定".equals(supplyAnalysis)){
                operationAbility.setSaScore(2);
            }
        }
        String teamStabilityAnalysis = operationAbility.getTeamStabilityAnalysis();
        if(StringUtils.isNotBlank(teamStabilityAnalysis)){
            if("低".equals(teamStabilityAnalysis)){
                operationAbility.setTsaScore(0);
            }
            if("中".equals(teamStabilityAnalysis)){
                operationAbility.setTsaScore(1);
            }
            if("高".equals(teamStabilityAnalysis)){
                operationAbility.setTsaScore(2);
            }
        }
        return operationAbility;
    }

}

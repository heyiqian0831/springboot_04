package com.fuyin.boot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fuyin.boot.mgb.entity.BusinessDiagnosisDescDO;
import com.fuyin.boot.model.vo.OperationAbilityVO;

public interface BusinessDiagnosisDescService extends IService<BusinessDiagnosisDescDO> {
    /**
     *@Description 查描述，返回最终结果
     **/
    OperationAbilityVO getResult(Integer levelScore, Integer abilityScore);
}

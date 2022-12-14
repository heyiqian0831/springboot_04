package com.fuyin.boot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fuyin.boot.mgb.entity.OperationAbilityDO;
import com.fuyin.boot.model.vo.OperationAbilityVO;

public interface OperationAbilityService extends IService<OperationAbilityDO> {
    /**
    *@Description 补充分数部分
    **/
    OperationAbilityDO completeScore(OperationAbilityDO operationAbility);

}

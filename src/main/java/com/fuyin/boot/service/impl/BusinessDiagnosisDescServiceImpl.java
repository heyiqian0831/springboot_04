package com.fuyin.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuyin.boot.mgb.entity.BusinessDiagnosisDescDO;
import com.fuyin.boot.mgb.mapper.BusinessDiagnosisDescMapper;
import com.fuyin.boot.model.vo.OperationAbilityVO;
import com.fuyin.boot.service.BusinessDiagnosisDescService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * @ClassName: BusinessDiagnosisDescServiceImpl
 * @Author: 何义祈安
 * @Description: 根据分数，查找描述
 * @Date: 2022/11/21 18:06
 * @Version: 1.0
 */
@Service
public class BusinessDiagnosisDescServiceImpl extends ServiceImpl<BusinessDiagnosisDescMapper,BusinessDiagnosisDescDO>
        implements BusinessDiagnosisDescService {
    /**
     *@Description 查描述，返回最终结果
     **/
    @Override
    public OperationAbilityVO getResult(Integer levelScore, Integer abilityScore) {
        //总分
        OperationAbilityVO oaAbilityVO = new OperationAbilityVO();
        oaAbilityVO.setTotalScore(levelScore+abilityScore);

        BusinessDiagnosisDescDO bdDescDO = new BusinessDiagnosisDescDO();
        LambdaQueryWrapper<BusinessDiagnosisDescDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BusinessDiagnosisDescDO::getModular,"经营能力");
        if(levelScore >= 0 && levelScore < 2){
            wrapper.eq(BusinessDiagnosisDescDO::getTotalScore,"0分-2分");
            bdDescDO = this.getOne(wrapper);
        }else if(levelScore >= 2 && levelScore < 4){
            wrapper.eq(BusinessDiagnosisDescDO::getTotalScore,"2分-4分");
            bdDescDO = this.getOne(wrapper);
        } else if(levelScore >= 4 && levelScore < 6){
            wrapper.eq(BusinessDiagnosisDescDO::getTotalScore,"4分-6分");
            bdDescDO = this.getOne(wrapper);
        }
        if(!Objects.isNull(bdDescDO)){
            oaAbilityVO.setAbilityDesc(bdDescDO.getDescription());
        }

        BusinessDiagnosisDescDO bdDescDO2 = new BusinessDiagnosisDescDO();
        LambdaQueryWrapper<BusinessDiagnosisDescDO> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(BusinessDiagnosisDescDO::getModular,"经营水平");
        if(abilityScore >= 0 && abilityScore < 30){
            wrapper2.eq(BusinessDiagnosisDescDO::getTotalScore,"0分-30分");
            bdDescDO2 = this.getOne(wrapper2);
        }else if(abilityScore >= 30 && abilityScore < 50){
            wrapper2.eq(BusinessDiagnosisDescDO::getTotalScore,"30分-50分");
            bdDescDO2 = this.getOne(wrapper2);
        } else if(abilityScore >= 50 && abilityScore < 66){
            wrapper2.eq(BusinessDiagnosisDescDO::getTotalScore,"50分-66分");
            bdDescDO2 = this.getOne(wrapper2);
        }
        if(!Objects.isNull(bdDescDO2)){
            oaAbilityVO.setLevelDesc(bdDescDO2.getDescription());
        }
        return oaAbilityVO;
    }
}

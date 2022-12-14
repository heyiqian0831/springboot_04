package com.fuyin.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuyin.boot.mgb.entity.*;
import com.fuyin.boot.mgb.mapper.DetailUrlMapper;
import com.fuyin.boot.model.param.BusinessModifiedParam;
import com.fuyin.boot.model.param.InputEnterpriceParam;
import com.fuyin.boot.model.vo.*;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.*;
import com.fuyin.boot.spider.executor.BusinRestructureExecutor;
import com.fuyin.boot.spider.executor.GetDetailPageExecutor;
import com.fuyin.boot.utils.BusinessConvertorUtils;
import com.fuyin.boot.utils.BusinessReversalUtils;
import org.apache.ibatis.builder.ResultMapResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName: BusinessServiceImpl
 * @Author: 何义祈安
 * @Description: 该service对应三个数据表的逻辑操作，三个表是业务重组的三个表，将其service放到这个接口实现类中操作，就不用在Controller中操作了
 * @Date: 2022/11/16 20:59
 * @Version: 1.0
 */
@Service
public class BusinessServiceImpl implements BusinessService {
    @Autowired
    private DetailUrlMapper detailUrlMapper;
    @Autowired
    private GetDetailPageExecutor pageExecutor;
    @Autowired
    private BusinRestructureExecutor restructureExecutor;
    @Autowired
    private BusinessGrowService growService;
    @Autowired
    private BusinessValuationService valuationService;
    @Autowired
    private BusinessDupontService dupontService;
    @Autowired
    private BusinessConvertorUtils businessConvertor;
    @Autowired
    private BusinessReversalUtils businessReversal;
    @Autowired
    private OperationAbilityService operationService;
    @Autowired
    private BusinessDiagnosisDescService diagnosisDescService;

    @Override
    public ResponseResult getBusinData(InputEnterpriceParam enterpriceParam) {
        String name = enterpriceParam.getName();
        try {
            if(Objects.isNull(selectDetailUrl(name))){
                pageExecutor.doCrawl(name);
                if(Objects.isNull(selectDetailUrl(name))){
                    throw new RuntimeException("企业查找失败");
                }else{
                    doRestructureExecutor(name);
                }
            }else{
                doRestructureExecutor(name);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        Map<String,Object> map = getDatas2Map(name,enterpriceParam);
        return new ResponseResult(200,"没有抛出异常，执行成功",map);
    }

    /**
     *@Description 用户点击生成重组方案按钮，后端更新用户修改的指标，计算最终确定的指标的综合分数
     **/
    @Override
    public ResponseResult getDebtScheme(BusinessModifiedParam param) {
        //获取三表数据
        List<GrowthResultVO> growthVOList = param.getGrowth();
        List<ValuationResultVO> valuationVOList = param.getValuation();
        List<DupontResultVO> dupontVOList = param.getDupont();

        //转成DO
        BusinessGrowthDO growthDO = businessReversal.growthReversal(growthVOList);
        BusinessValuationDO valuationDO = businessReversal.valuationReversal(valuationVOList);
        BusinessDupontDO dupontDO = businessReversal.dupontReversal(dupontVOList);

        //三表更新
        if(!Objects.isNull(growthDO)){
            growService.updateById(growthDO);
        }
        if(!Objects.isNull(valuationDO)) {
            valuationService.updateById(valuationDO);
        }
        if(!Objects.isNull(dupontDO)) {
            dupontService.updateById(dupontDO);
        }

        //获取用户返回的经营能力分析数据并填充分数 返回经营能力发分析表
        OperationAbilityDO operationAbility = operationService.completeScore(param.getOperationAbility());
        Integer growthScore = 0;
        Integer valuationScore = 0;
        Integer dupontScore = 0;

        Integer abilityScore = 0;
        Integer levelScore = 0;
        if(!Objects.isNull(growthVOList)) {
            for (GrowthResultVO growthResultVO : growthVOList) {
                growthScore += growthResultVO.getScore();
            }
        }
        if(!Objects.isNull(valuationVOList)) {
            for (ValuationResultVO valuationResultVO : valuationVOList) {
                valuationScore += valuationResultVO.getScore();
            }
        }
        if(!Objects.isNull(dupontVOList)) {
            for (DupontResultVO dupontResultVO : dupontVOList) {
                dupontScore += dupontResultVO.getScore();
            }
        }
        //经营总分
        levelScore = growthScore + valuationScore + dupontScore;
        abilityScore = operationAbility.getRaScore()+operationAbility.getSaScore()+operationAbility.getTsaScore();
        //获取业务诊断结果描述
        OperationAbilityVO oabilityVO = diagnosisDescService.getResult(levelScore,abilityScore);

        Map<String,Object> map = new HashMap<>();
        map.put("growth",growthVOList);
        map.put("valuation",valuationVOList);
        map.put("dupont",dupontVOList);
        map.put("operationAbility",operationAbility);
        map.put("operationAbilityVO",oabilityVO);
        return new ResponseResult(200,"执行成功，执行成功",map);
    }

    /**
    *@Description 查找三张表加三张表的行业平均表
    **/
    private Map<String, Object> getDatas2Map(String name,InputEnterpriceParam enterpriceParam) {
        Map<String,Object> map = new HashMap<>();
        LambdaQueryWrapper<BusinessGrowthDO> wrapper= new LambdaQueryWrapper<>();
        wrapper.eq(BusinessGrowthDO::getName,name);
        BusinessGrowthDO growthDO = growService.getOne(wrapper);
        map.put("growthDO",growthDO);
        LambdaQueryWrapper<BusinessGrowthDO> wrapperAve= new LambdaQueryWrapper<>();
        wrapperAve.eq(BusinessGrowthDO::getName,name+"行业平均");
        BusinessGrowthDO growthAverage = growService.getOne(wrapperAve);
        map.put("growthAverage",growthAverage);


        LambdaQueryWrapper<BusinessValuationDO> wrapperValua = new LambdaQueryWrapper<>();
        wrapperValua.eq(BusinessValuationDO::getName,name);
        BusinessValuationDO valuationDO = valuationService.getOne(wrapperValua);
        map.put("valuationDO",valuationDO);
        LambdaQueryWrapper<BusinessValuationDO> wrapperValuaAve = new LambdaQueryWrapper<>();
        wrapperValuaAve.eq(BusinessValuationDO::getName,name+"行业平均");
        BusinessValuationDO valuationAverage = valuationService.getOne(wrapperValuaAve);
        map.put("valuationAverage",valuationAverage);

        LambdaQueryWrapper<BusinessDupontDO> wrapperDupont = new LambdaQueryWrapper<>();
        wrapperDupont.eq(BusinessDupontDO::getName,name);
        BusinessDupontDO dupontDO = dupontService.getOne(wrapperDupont);
        map.put("dupontDO",dupontDO);
        LambdaQueryWrapper<BusinessDupontDO> wrapperDupontAve = new LambdaQueryWrapper<>();
        wrapperDupontAve.eq(BusinessDupontDO::getName,name+"行业平均");
        BusinessDupontDO dupontAverage = dupontService.getOne(wrapperDupontAve);
        map.put("dupontAverage",dupontAverage);

        return turnDO2VO2Map(map,enterpriceParam);
    }

    /**
    *@Description 将业务重组获得的所有DO类指标转换成VO集合，传入三张DO原始数据表+三张行业平均DO类
    **/
    private Map<String, Object> turnDO2VO2Map(Map<String, Object> map,InputEnterpriceParam enterpriceParam) {
        BusinessGrowthDO growthDO = (BusinessGrowthDO) map.get("growthDO");
        BusinessGrowthDO growthAverage = (BusinessGrowthDO) map.get("growthAverage");
        List<GrowthResultVO> growthResultVOList = businessConvertor.growthConvertor(growthDO, growthAverage);

        BusinessValuationDO valuationDO = (BusinessValuationDO) map.get("valuationDO");
        BusinessValuationDO valuationAverage = (BusinessValuationDO) map.get("valuationAverage");
        List<ValuationResultVO> valuationResultVOS = businessConvertor.valuaConvertor(valuationDO, valuationAverage);

        BusinessDupontDO dupontDO = (BusinessDupontDO) map.get("dupontDO");
        BusinessDupontDO dupontAverage = (BusinessDupontDO) map.get("dupontAverage");
        List<DupontResultVO> dupontResultVOList = businessConvertor.dupontConvertor(dupontDO, dupontAverage);

        Map<String,Object> mapReturn = new HashMap<>();
        mapReturn.put("growth",growthResultVOList);
        mapReturn.put("valuation",valuationResultVOS);
        mapReturn.put("dupont",dupontResultVOList);
        map.put("enterpriceParam",enterpriceParam);
        return mapReturn;
    }

    /**
    *@Description 调用爬虫启动类爬取业务重组的数据
    **/
    private void doRestructureExecutor(String name) {
        try {
            DetailUrlDO detailUrlDO = selectDetailUrl(name);
            restructureExecutor.doCrawl(detailUrlDO.getUrl(), detailUrlDO.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    *@Description 通过名称查找用户详情信息
    **/
    @Override
    public DetailUrlDO selectDetailUrl(String name) {
        LambdaQueryWrapper<DetailUrlDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DetailUrlDO::getName,name);
        return detailUrlMapper.selectOne(wrapper);
    }


}

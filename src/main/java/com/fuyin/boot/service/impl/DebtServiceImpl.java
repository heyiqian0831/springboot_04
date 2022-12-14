package com.fuyin.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fuyin.boot.mgb.entity.*;
import com.fuyin.boot.mgb.mapper.DetailUrlMapper;
import com.fuyin.boot.model.param.DebtDataModifiedParam;
import com.fuyin.boot.model.param.InputEnterpriceParam;
import com.fuyin.boot.model.vo.CapacityResultVO;
import com.fuyin.boot.model.vo.CashFlowResultVO;
import com.fuyin.boot.model.vo.DebtResultDescVO;
import com.fuyin.boot.model.vo.ProfitabilityResultVO;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.*;
import com.fuyin.boot.spider.executor.DebtAllExecutor;
import com.fuyin.boot.spider.executor.GetDetailPageExecutor;
import com.fuyin.boot.utils.DebtConvertorUtils;
import com.fuyin.boot.utils.DebtReversalUtils;
import com.fuyin.boot.utils.Log4AddUtil;
import com.google.common.base.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ClassName: DebtServiceImpl
 * @Author: 何义祈安
 * @Description: 债务重组爬取实现类
 * @Date: 2022/11/15 20:34
 * @Version: 1.0
 */
@Service
public class DebtServiceImpl implements DebtService {
    @Autowired
    private DetailUrlMapper detailUrlMapper;
    @Autowired
    private GetDetailPageExecutor getDetailPageExecutor;
    @Autowired
    private DebtAllExecutor debtAllExecutor;
    @Autowired
    private DebtCapacityService capacityService;
    @Autowired
    private DebtProfitabilityService profitabilityService;
    @Autowired
    private DebtCashFlowService cashFlowService;
    @Autowired
    private Log4AddUtil log4AddUtil;
    @Autowired
    private SystemLogService systemLogService;
    @Autowired
    private DebtConvertorUtils debtConvertorUtils;
    @Autowired
    private DebtReversalUtils debtReversalUtils;

    /**
    *@Description 获取债务重组数据
    *@Param [enterpriceParam]
    *@return com.fuyin.boot.result.ResponseResult
    **/
    @Override
    public ResponseResult getDebtData(InputEnterpriceParam enterpriceParam) {
        //通过企业名称从DetailUrl表中查找是否存在该企业详情页url
        try {
            if(Objects.isNull(getDetailUrl(enterpriceParam))){
                //如果没有，调用GetDetailPageExecutor爬取企业详情页基本信息
                getDetailPageExecutor.doCrawl(enterpriceParam.getName());
                SystemLogDO systemLogDO = log4AddUtil.debtManageLog("添加");
                systemLogDO.setOperationStatus("success");
                systemLogService.insertLog(systemLogDO);
                if(Objects.isNull(getDetailUrl(enterpriceParam))){
                    //如果还没有
                    systemLogDO.setOperationStatus("fail");
                    systemLogService.insertLog(systemLogDO);
                    throw new RuntimeException("企业查找失败");
                }
                //爬取
                doAllExecutor(enterpriceParam);
            }else{
                //爬取
                doAllExecutor(enterpriceParam);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("企业查找失败"+e);
        }
        //获取并加工并打包数据
        Map<String, Object> map = getDebtAll2Map(enterpriceParam);
        return new ResponseResult(200,"操作成功",map);
    }

    /**
    *@Description 生成方案
    *@Param [param]
    *@return com.fuyin.boot.result.ResponseResult
    **/
    @Override
    public ResponseResult getDebtScheme(DebtDataModifiedParam param) {
        //获取每个表的数据list、描述对象、用户输入对象
        List<CapacityResultVO> capacity = param.getCapacity();
        List<ProfitabilityResultVO> profitability = param.getProfitability();
        List<CashFlowResultVO> cashFlow = param.getCashFlow();

        //转成DO
        DebtCapacityDO debtCapacityDO = debtReversalUtils.capaReversal(capacity);
        DebtProfitabilityDO debtProfitabilityDO = debtReversalUtils.profitReversal(profitability);
        DebtCashFlowDO debtCashFlowDO = debtReversalUtils.cashReversal(cashFlow);

        //三表更新
        if(!Objects.isNull(debtCapacityDO)){
            capacityService.updateById(debtCapacityDO);
        }
        if(!Objects.isNull(debtProfitabilityDO)) {
            profitabilityService.updateById(debtProfitabilityDO);
        }
        if(!Objects.isNull(debtCashFlowDO)) {
            cashFlowService.updateById(debtCashFlowDO);
        }
        //更新描述类，最后打包结果成map
        Map<String, Object> map = getDebtAll2Map(param.getEnterpriceParam());
        return new ResponseResult(200,"操作成功",map);
    }

    /**
    *@Description 爬取后通过企业名称查找存进数据库中的数据
    **/
    private Map<String, Object> getDebtAll2Map(InputEnterpriceParam enterpriceParam) {
        //查找负债能力数据
        LambdaQueryWrapper<DebtCapacityDO> wrapperCapacity = new LambdaQueryWrapper<>();
        wrapperCapacity.eq(DebtCapacityDO::getName,enterpriceParam.getName());
        DebtCapacityDO debtCapacity = capacityService.getOne(wrapperCapacity);

        //查找盈利能力数据
        LambdaQueryWrapper<DebtProfitabilityDO> wrapperProfit = new LambdaQueryWrapper<>();
        wrapperProfit.eq(DebtProfitabilityDO::getName,enterpriceParam.getName());
        DebtProfitabilityDO debtProfitability = profitabilityService.getOne(wrapperProfit);

        //查找现金流数据
        LambdaQueryWrapper<DebtCashFlowDO> wrapperCash = new LambdaQueryWrapper<>();
        wrapperCash.eq(DebtCashFlowDO::getName,enterpriceParam.getName());
        DebtCashFlowDO debtCashFlow = cashFlowService.getOne(wrapperCash);

        return turnDO2VO2Map(enterpriceParam,debtCapacity,debtProfitability,debtCashFlow);
    }

    /**
    *@Description 将查到的原始数据做加工，加上评分模块，转成VO 并做map类型存储
    *@Param [debtCapacity, debtProfitability, debtCashFlow]
    *@return java.util.Map<java.lang.String,java.lang.Object>
    **/
    private Map<String, Object> turnDO2VO2Map(InputEnterpriceParam enterpriceParam,DebtCapacityDO debtCapacity, DebtProfitabilityDO debtProfitability, DebtCashFlowDO debtCashFlow) {
        Map<String,Object> map = new HashMap<>();
        List<CapacityResultVO> capacityResultVOList = debtConvertorUtils.capaConvertor(debtCapacity);
        List<ProfitabilityResultVO> profitabilityResulVOtList = debtConvertorUtils.profitConvertor(debtProfitability);
        List<CashFlowResultVO> cashFlowResultVOS = debtConvertorUtils.cashConvertor(debtCashFlow);

        //获取各模块总分（工具类）通过分数获取描述对象
        Integer capaScore = 0;
        Integer profitScore = 0;
        Integer cashScore = 0;
        if(!Objects.isNull(capacityResultVOList)) {
            for (CapacityResultVO capacityResultVO : capacityResultVOList) {
                capaScore += capacityResultVO.getScore();
            }
        }
        if(!Objects.isNull(profitabilityResulVOtList)) {
            for (ProfitabilityResultVO profitabilityResultVO : profitabilityResulVOtList) {
                profitScore += profitabilityResultVO.getScore();
            }
        }
        if(!Objects.isNull(cashFlowResultVOS)) {
            for (CashFlowResultVO cashFlowResultVO : cashFlowResultVOS) {
                cashScore += cashFlowResultVO.getScore();
            }
        }
        DebtResultDescVO debtResultDescVO = new DebtResultDescVO();
        String desc = debtResultDescVO.debtResult(enterpriceParam.getName(),capaScore, profitScore, cashScore);

        map.put("enterpriceParam",enterpriceParam);
        map.put("capacity",capacityResultVOList);
        map.put("profitability",profitabilityResulVOtList);
        map.put("cashFlow",cashFlowResultVOS);
        map.put("debtDesc",desc);
        return map;
    }

    /**
    *@Description DetailUrl有值后，通过名称查找数据
    **/
    private void doAllExecutor(InputEnterpriceParam enterpriceParam) {
        try {
            DetailUrlDO detailUrlDO = getDetailUrl(enterpriceParam);
            debtAllExecutor.doCrawl(detailUrlDO.getUrl(),detailUrlDO.getType(),"capacity");
            debtAllExecutor.doCrawl(detailUrlDO.getUrl(),detailUrlDO.getType(),"profitability");
            debtAllExecutor.doCrawl(detailUrlDO.getUrl(),detailUrlDO.getType(),"cashFlow");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    *@Description 通过企业名称查询DetailUrl表
    *@Param [enterpriceParam]
    *@return com.fuyin.boot.mgb.entity.DetailUrlDO
    **/
    private DetailUrlDO getDetailUrl(InputEnterpriceParam enterpriceParam) {
        LambdaQueryWrapper<DetailUrlDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DetailUrlDO::getName,enterpriceParam.getName());
        return detailUrlMapper.selectOne(wrapper);
    }
}

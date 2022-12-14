package com.fuyin.boot.service.impl;

import com.fuyin.boot.enums.BusinessTableType;
import com.fuyin.boot.mgb.entity.*;
import com.fuyin.boot.mgb.mapper.*;
import com.fuyin.boot.model.param.BusinessIndicatorsAllParam;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: BusinessAllIndicatorsServiceImpl
 * @Author: 何义祈安
 * @Description: 内容管理--业务指标管理接口实现类
 * @Date: 2022/11/21 19:23
 * @Version: 1.0
 */
@Service
public class BusinessAllIndicatorsServiceImpl implements BusinessAllIndicatorsService {
    @Autowired
    private DemandStatusIndicatorsService demandStatusService;
    @Autowired
    private AvailabilityIndicatorsService availabilityService;
    @Autowired
    private TeamStabilityIndicatorsService teamStabilityService;
    @Autowired
    private BusinessDiagnosisDescService businessDiagnosisDescService;
    @Autowired
    private GrowthIndicatorsService growthService;
    @Autowired
    private ValuationIndicatorsService valuationService;
    @Autowired
    private CashFlowIndicatorsService cashFlowService;

    private List<GrowthIndicatorsDO> growth = new ArrayList<>();
    private List<ValuationIndicatorsDO> valuation = new ArrayList<>();
    private List<CashFlowIndicatorsDO> cashFlow = new ArrayList<>();
    private List<DemandStatusIndicatorsDO> demandStatus = new ArrayList<>();
    private List<AvailabilityIndicatorsDO> availability = new ArrayList<>();
    private List<TeamStabilityIndicatorsDO> teamStability = new ArrayList<>();
    private List<BusinessDiagnosisDescDO> diagnosisDesc = new ArrayList<>();

    /**
    *@Description 获取所有业务重组指标
    **/
    @Override
    public ResponseResult getAll() {
        demandStatus = demandStatusService.list(null);
        availability = availabilityService.list(null);
        teamStability = teamStabilityService.list(null);
        diagnosisDesc = businessDiagnosisDescService.list(null);
        growth = growthService.list(null);
        valuation = valuationService.list(null);
        cashFlow = cashFlowService.list(null);
        Map<String,Object> map = new HashMap<>();
        map.put(BusinessTableType.DEMAND_STATUS.getCode(),demandStatus);
        map.put(BusinessTableType.AVAILABILITY.getCode(),availability);
        map.put(BusinessTableType.TEAM_STABILITY.getCode(),teamStability);
        map.put(BusinessTableType.DIAGNOSIS_DESC.getCode(),diagnosisDesc);
        map.put(BusinessTableType.GROWTH_INDICATORS.getCode(),growth);
        map.put(BusinessTableType.VALUATION_INDICATORS.getCode(),valuation);
        map.put(BusinessTableType.DUPONT_INDICATORS.getCode(),cashFlow);
        return new ResponseResult(200,"操作成功",map);
    }

    @Override
    public ResponseResult saveOrUpdateAl(List<BusinessIndicatorsAllParam> params) {
        //分类
        for (BusinessIndicatorsAllParam param : params) {
            Object type = param.getType();
            if (type.equals(BusinessTableType.DEMAND_STATUS.getCode())) {
                demandStatus.add(new DemandStatusIndicatorsDO(param.getIndex(),param.getScoringCriteria()));
            }
            if (type.equals(BusinessTableType.AVAILABILITY.getCode())) {
                availability.add(new AvailabilityIndicatorsDO(param.getIndex(),param.getScoringCriteria()));
            }
            if (type.equals(BusinessTableType.TEAM_STABILITY.getCode())) {
                teamStability.add(new TeamStabilityIndicatorsDO(param.getStability(),param.getScoringCriteria()));
            }
            if (type.equals(BusinessTableType.DIAGNOSIS_DESC.getCode())) {
                diagnosisDesc.add(new BusinessDiagnosisDescDO(param.getModular(),param.getTotalScore(),param.getDescription()));
            }
            if (type.equals(BusinessTableType.GROWTH_INDICATORS.getCode())) {
                growth.add(new GrowthIndicatorsDO(param.getModular(),param.getIndex(),param.getScoringCriteria()));
            }
            if (type.equals(BusinessTableType.VALUATION_INDICATORS.getCode())) {
                valuation.add(new ValuationIndicatorsDO(param.getModular(),param.getIndex(),param.getScoringCriteria()));
            }
            if (type.equals(BusinessTableType.DUPONT_INDICATORS.getCode())) {
                cashFlow.add(new CashFlowIndicatorsDO(param.getModular(), param.getIndex(),param.getScoringCriteria()));
            }
        }
        //存
        try {
            demandStatusService.saveOrUpdateBatch(demandStatus);
            availabilityService.saveOrUpdateBatch(availability);
            teamStabilityService.saveOrUpdateBatch(teamStability);
            businessDiagnosisDescService.saveOrUpdateBatch(diagnosisDesc);
            growthService.saveOrUpdateBatch(growth);
            valuationService.saveOrUpdateBatch(valuation);
            cashFlowService.saveOrUpdateBatch(cashFlow);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("更新失败"+e);
        }
        return new ResponseResult(200,"操作成功");

    }
}

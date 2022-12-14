package com.fuyin.boot.utils;

import com.fuyin.boot.enums.DebtIndexChinese;
import com.fuyin.boot.mgb.entity.DebtCapacityDO;
import com.fuyin.boot.mgb.entity.DebtCashFlowDO;
import com.fuyin.boot.mgb.entity.DebtProfitabilityDO;
import com.fuyin.boot.model.vo.CapacityResultVO;
import com.fuyin.boot.model.vo.CashFlowResultVO;
import com.fuyin.boot.model.vo.ProfitabilityResultVO;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: DebtConvertorUtils
 * @Author: 何义祈安
 * @Description: 债务重组爬取数据DO类型转为VO类型
 * @Date: 2022/11/26 11:57
 * @Version: 1.0
 */
@Component
public class DebtConvertorUtils {

    /**
    *@Description 负债能力诊断指标:
    **/
    @SneakyThrows
    public List<CapacityResultVO> capaConvertor(DebtCapacityDO debtCapacityDO){
        List<CapacityResultVO> capacityResultList = new ArrayList<>();
        if(!Objects.isNull(debtCapacityDO)) {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(debtCapacityDO);
            for (int i = 0; i < descriptors.length; i++) {
                CapacityResultVO capacityResultVO = new CapacityResultVO();
                capacityResultVO.setCDO_id(debtCapacityDO.getId());
                String name = descriptors[i].getName();
                if (!Objects.isNull(propertyUtilsBean.getNestedProperty(debtCapacityDO, name))) {
                    String value = propertyUtilsBean.getNestedProperty(debtCapacityDO, name).toString();
                    if (StringUtils.isNoneBlank(name, value)) {
                        switch (name) {
                            case "currentRatio": {
                                capacityResultVO.setIndex(DebtIndexChinese.CURRENT_RATIO.getMessage());
                                capacityResultVO.setValue(Double.valueOf(value));
                                if (Double.parseDouble(value) > 2.00) {
                                    capacityResultVO.setResult("3分(较好)");
                                    capacityResultVO.setScore(3);
                                }
                                if (Double.parseDouble(value) == 2.00) {
                                    capacityResultVO.setResult("2分(一般)");
                                    capacityResultVO.setScore(2);
                                }
                                if (Double.parseDouble(value) < 2.00) {
                                    capacityResultVO.setResult("1分(较差)");
                                    capacityResultVO.setScore(1);
                                }
                                capacityResultList.add(capacityResultVO);
                                break;
                            }
                            case "quickRatio": {
                                capacityResultVO.setIndex(DebtIndexChinese.QUICK_RATIO.getMessage());
                                capacityResultVO.setValue(Double.valueOf(value));
                                if (Double.parseDouble(value) > 1.25) {
                                    capacityResultVO.setResult("3分(较好)");
                                    capacityResultVO.setScore(3);
                                }
                                if (Double.parseDouble(value) == 1.25) {
                                    capacityResultVO.setResult("2分(一般)");
                                    capacityResultVO.setScore(2);
                                }
                                if (Double.parseDouble(value) < 1.25) {
                                    capacityResultVO.setResult("1分(较差)");
                                    capacityResultVO.setScore(1);
                                }
                                capacityResultList.add(capacityResultVO);
                                break;
                            }
                            case "debtAssetRatio": {
                                capacityResultVO.setIndex(DebtIndexChinese.DEBT_ASSET_RATIO.getMessage());
                                capacityResultVO.setValue(Double.valueOf(value));
                                if (Double.parseDouble(value) >= 0.6 && Double.parseDouble(value) <= 0.7) {
                                    capacityResultVO.setResult("3分(合理)");
                                    capacityResultVO.setScore(3);
                                }
                                if (Double.parseDouble(value) > 0.7) {
                                    capacityResultVO.setResult("1分(不合理,负债过高)");
                                    capacityResultVO.setScore(1);
                                }
                                if (Double.parseDouble(value) < 0.6) {
                                    capacityResultVO.setResult("1分(不合理,负债过低)");
                                    capacityResultVO.setScore(1);
                                }
                                capacityResultList.add(capacityResultVO);
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                    }
                }
            }
            return capacityResultList;
        }
        return null;
    }

    /**
    *@Description 盈利能力诊断指标：
    **/
    @SneakyThrows
    public List<ProfitabilityResultVO> profitConvertor(DebtProfitabilityDO debtProfitabilityDO){
        if(Objects.isNull(debtProfitabilityDO)){
            return null;
        }
        List<ProfitabilityResultVO> profitabilityResultList = new ArrayList<>();
        PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
        PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(debtProfitabilityDO);
        for (int i = 0; i < descriptors.length; i++) {
            ProfitabilityResultVO profitabilityResult = new ProfitabilityResultVO();
            profitabilityResult.setPDO_id(debtProfitabilityDO.getId());
            String name = descriptors[i].getName();
            if(!Objects.isNull(propertyUtilsBean.getNestedProperty(debtProfitabilityDO, name))) {
                String value = propertyUtilsBean.getNestedProperty(debtProfitabilityDO, name).toString();
                if (StringUtils.isNoneBlank(name, value)) {
                    switch (name) {
                        case "grossProfitRatio": {
                            profitabilityResult.setIndex(DebtIndexChinese.GROSS_PROFIT_RATIO.getMessage());
                            profitabilityResult.setValue(Double.valueOf(value));
                            if (Double.parseDouble(value) > 0.15) {
                                profitabilityResult.setResult("3分(较好)");
                                profitabilityResult.setScore(3);
                            }
                            if (Double.parseDouble(value) == 0.15) {
                                profitabilityResult.setResult("2分(一般)");
                                profitabilityResult.setScore(2);
                            }
                            if (Double.parseDouble(value) < 0.15) {
                                profitabilityResult.setResult("1分(较差)");
                                profitabilityResult.setScore(1);
                            }
                            profitabilityResultList.add(profitabilityResult);
                            break;
                        }
                        case "netInterestRate": {
                            profitabilityResult.setIndex(DebtIndexChinese.NET_INTEREST_RATE.getMessage());
                            profitabilityResult.setValue(Double.valueOf(value));
                            if (Double.parseDouble(value) > 0.4) {
                                profitabilityResult.setResult("3分(较好)");
                                profitabilityResult.setScore(3);
                            }
                            if (Double.parseDouble(value) == 0.3) {
                                profitabilityResult.setResult("2分(一般)");
                                profitabilityResult.setScore(2);
                            }
                            if (Double.parseDouble(value) < 0.3) {
                                profitabilityResult.setResult("1分(较差)");
                                profitabilityResult.setScore(1);
                            }
                            profitabilityResultList.add(profitabilityResult);
                            break;
                        }
                        case "returnOnTotalAssets": {
                            profitabilityResult.setIndex(DebtIndexChinese.RETURN_ON_TOTAL_ASSETS.getMessage());
                            profitabilityResult.setValue(Double.valueOf(value));
                            if (Double.parseDouble(value) > 0.08) {
                                profitabilityResult.setResult("3分(较好)");
                                profitabilityResult.setScore(3);
                            }
                            if (Double.parseDouble(value) == 0.08) {
                                profitabilityResult.setResult("2分(一般)");
                                profitabilityResult.setScore(2);
                            }
                            if (Double.parseDouble(value) < 0.08) {
                                profitabilityResult.setResult("1分(较差)");
                                profitabilityResult.setScore(1);
                            }
                            profitabilityResultList.add(profitabilityResult);
                            break;
                        }
                        case "returnOnInvestedCapital": {
                            profitabilityResult.setIndex(DebtIndexChinese.RETURN_ON_INVESTED_CAPITAL.getMessage());
                            profitabilityResult.setValue(Double.valueOf(value));
                            if (Double.parseDouble(value) > 0.1) {
                                profitabilityResult.setResult("3分(较好)");
                                profitabilityResult.setScore(3);
                            }
                            if (Double.parseDouble(value) == 0.1) {
                                profitabilityResult.setResult("2分(一般)");
                                profitabilityResult.setScore(2);
                            }
                            if (Double.parseDouble(value) < 0.1) {
                                profitabilityResult.setResult("1分(较差)");
                                profitabilityResult.setScore(1);
                            }
                            profitabilityResultList.add(profitabilityResult);
                            break;
                        }
                        case "roe": {
                            profitabilityResult.setIndex(DebtIndexChinese.ROE.getMessage());
                            profitabilityResult.setValue(Double.valueOf(value));
                            if (Double.parseDouble(value) > 0.1) {
                                profitabilityResult.setResult("3分(较好)");
                                profitabilityResult.setScore(3);
                            }
                            if (Double.parseDouble(value) == 0.1) {
                                profitabilityResult.setResult("2分(一般)");
                                profitabilityResult.setScore(2);
                            }
                            if (Double.parseDouble(value) < 0.1) {
                                profitabilityResult.setResult("1分(较差)");
                                profitabilityResult.setScore(1);
                            }
                            profitabilityResultList.add(profitabilityResult);
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                }
            }
        }
        return profitabilityResultList;
    }

    /**
    *@Description 现金流诊断指标：
    **/
    @SneakyThrows
    public List<CashFlowResultVO> cashConvertor(DebtCashFlowDO debtCashFlowDO) {
        List<CashFlowResultVO> cashFlowResultList = new ArrayList<>();
        if (!Objects.isNull(debtCashFlowDO)) {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(debtCashFlowDO);
            for (int i = 0; i < descriptors.length; i++) {
                CashFlowResultVO cashFlowResultVO = new CashFlowResultVO();
                cashFlowResultVO.setCFD_id(debtCashFlowDO.getId());
                String name = descriptors[i].getName();
                if (!Objects.isNull(propertyUtilsBean.getNestedProperty(debtCashFlowDO, name))) {
                    String value = propertyUtilsBean.getNestedProperty(debtCashFlowDO, name).toString();
                    if (StringUtils.isNoneBlank(name, value)) {
                        switch (name) {
                            case "cashSalesRatio": {
                                cashFlowResultVO.setIndex(DebtIndexChinese.CASH_SALES_RATIO.getMessage());
                                cashFlowResultVO.setValue(Double.valueOf(value));
                                if (Double.parseDouble(value) > 0.2) {
                                    cashFlowResultVO.setResult("3分(较好)");
                                    cashFlowResultVO.setScore(3);
                                }
                                if (Double.parseDouble(value) == 0.2) {
                                    cashFlowResultVO.setResult("2分(一般)");
                                    cashFlowResultVO.setScore(2);
                                }
                                if (Double.parseDouble(value) < 0.2) {
                                    cashFlowResultVO.setResult("1分(较差)");
                                    cashFlowResultVO.setScore(1);
                                }
                                cashFlowResultList.add(cashFlowResultVO);
                                break;
                            }
                            case "cashToMaturityRatio": {
                                cashFlowResultVO.setIndex(DebtIndexChinese.CASH_TO_MATURITY_RATIO.getMessage());
                                cashFlowResultVO.setValue(Double.valueOf(value));
                                if (Double.parseDouble(value) > 1.5) {
                                    cashFlowResultVO.setResult("3分(较好)");
                                    cashFlowResultVO.setScore(3);
                                }
                                if (Double.parseDouble(value) == 1.5) {
                                    cashFlowResultVO.setResult("2分(一般)");
                                    cashFlowResultVO.setScore(2);
                                }
                                if (Double.parseDouble(value) < 1.5) {
                                    cashFlowResultVO.setResult("1分(较差)");
                                    cashFlowResultVO.setScore(1);
                                }
                                cashFlowResultList.add(cashFlowResultVO);
                                break;
                            }
                            case "cashFlowLiabilityRatio": {
                                cashFlowResultVO.setIndex(DebtIndexChinese.CASH_FLOW_LIABILITY_RATIO.getMessage());
                                cashFlowResultVO.setValue(Double.valueOf(value));
                                if (Double.parseDouble(value) > 0.25) {
                                    cashFlowResultVO.setResult("3分(较好)");
                                    cashFlowResultVO.setScore(3);
                                }
                                if (Double.parseDouble(value) == 0.25) {
                                    cashFlowResultVO.setResult("2分(一般)");
                                    cashFlowResultVO.setScore(2);
                                }
                                if (Double.parseDouble(value) < 0.25) {
                                    cashFlowResultVO.setResult("1分(较差)");
                                    cashFlowResultVO.setScore(1);
                                }
                                cashFlowResultList.add(cashFlowResultVO);
                                break;
                            }
                            case "netProfitCashFlowRatio": {
                                cashFlowResultVO.setIndex(DebtIndexChinese.NET_PROFIT_CASH_FLOW_RATIO.getMessage());
                                cashFlowResultVO.setValue(Double.valueOf(value));
                                if (Double.parseDouble(value) > 0.02) {
                                    cashFlowResultVO.setResult("3分(较好)");
                                    cashFlowResultVO.setScore(3);
                                }
                                if (Double.parseDouble(value) == 0.02) {
                                    cashFlowResultVO.setResult("2分(一般)");
                                    cashFlowResultVO.setScore(2);
                                }
                                if (Double.parseDouble(value) < 0.02) {
                                    cashFlowResultVO.setResult("1分(较差)");
                                    cashFlowResultVO.setScore(1);
                                }
                                cashFlowResultList.add(cashFlowResultVO);
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                    }
                }
            }
            return cashFlowResultList;
        }
        return null;
    }


}

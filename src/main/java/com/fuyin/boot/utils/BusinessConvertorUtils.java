package com.fuyin.boot.utils;

import com.fuyin.boot.mgb.entity.BusinessDupontDO;
import com.fuyin.boot.mgb.entity.BusinessGrowthDO;
import com.fuyin.boot.mgb.entity.BusinessValuationDO;
import com.fuyin.boot.mgb.entity.DebtCapacityDO;
import com.fuyin.boot.model.vo.CapacityResultVO;
import com.fuyin.boot.model.vo.DupontResultVO;
import com.fuyin.boot.model.vo.GrowthResultVO;
import com.fuyin.boot.model.vo.ValuationResultVO;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: BusinessConvertorUtils
 * @Author: 何义祈安
 * @Description: 业务重组相关表DO转VO
 * @Date: 2022/11/26 19:59
 * @Version: 1.0
 */
@Component
public class BusinessConvertorUtils {
    /**
     *@Description 成长性指标转换
     **/
    @SneakyThrows
    public List<GrowthResultVO> growthConvertor(BusinessGrowthDO growth,BusinessGrowthDO growthAverage){
        List<GrowthResultVO> growthResultVOList = new ArrayList<>();
        //DO对象判空
        if(!Objects.isNull(growth)) {
            //遍历growthDO的成员变量
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(growth);
            for (int i = 0; i < descriptors.length; i++) {
                //创建VO对象
                GrowthResultVO growthResult = new GrowthResultVO();
                //传DO对象的id,做唯一标识
                growthResult.setBGD_id(growth.getId());
                //获取DO对象中成员变量名
                String name = descriptors[i].getName();
                growthResult.setIndex(name);
                //判空条件，该变量名的属性不能为空，防止空指针异常
                if (!Objects.isNull(propertyUtilsBean.getNestedProperty(growth, name))) {
                    //获取成员变量为name的值
                    String value = propertyUtilsBean.getNestedProperty(growth, name).toString();
                    String growAve = propertyUtilsBean.getNestedProperty(growthAverage, name).toString();
                    //成员变量名和属性值都不为空，则传入VO中
                    if (StringUtils.isNoneBlank(name, value)) {
                        switch (name) {
                            case "basicEpsYear3":
                            case "basicEpsA21":
                            case "basicEpsE22":
                            case "basicEpsE23":
                            case "basicEpsE24": {
                                double v = Double.parseDouble(value);
                                double v1 = Double.parseDouble(growAve);
                                growthResult.setValue(Double.valueOf(value));
                                growthResult.setModular("基本每股收益增长率");
                                if (v >= v1) {
                                    growthResult.setResult(value+"【优秀】");
                                    growthResult.setScore(2);
                                }
                                if (v < v1) {
                                    growthResult.setResult(value+"【一般】");
                                    growthResult.setScore(1);
                                }
                                growthResultVOList.add(growthResult);
                                break;
                            }
                            case "operatingIncomeYear3":
                            case "operatingIncomeA21":
                            case "operatingIncomeE22":
                            case "operatingIncomeE23":
                            case "operatingIncomeE24": {
                                double v = Double.parseDouble(value);
                                double v1 = Double.parseDouble(growAve);
                                growthResult.setValue(Double.valueOf(value));
                                growthResult.setModular("营业收入增长率");
                                if (v >= v1) {
                                    growthResult.setResult(value+"【优秀】");
                                    growthResult.setScore(2);
                                }
                                if (v < v1) {
                                    growthResult.setResult(value+"【一般】");
                                    growthResult.setScore(1);
                                }
                                growthResultVOList.add(growthResult);
                                break;
                            }
                            case "basicEpsTtm": {
                                double v = Double.parseDouble(value);
                                double v1 = Double.parseDouble(growAve);
                                growthResult.setValue(Double.valueOf(value));
                                growthResult.setModular("基本每股收益增长率");
                                if (v < v1) {
                                    growthResult.setResult(value+"【优秀】");
                                    growthResult.setScore(2);
                                }
                                if (v >= v1) {
                                    growthResult.setResult(value+"【一般】");
                                    growthResult.setScore(1);
                                }
                                growthResultVOList.add(growthResult);
                                break;
                            }
                            case "operatingIncomeTtm": {
                                double v = Double.parseDouble(value);
                                double v1 = Double.parseDouble(growAve);
                                growthResult.setValue(Double.valueOf(value));
                                growthResult.setModular("营业收入增长率");
                                if (v < v1) {
                                    growthResult.setResult(value+"【优秀】");
                                    growthResult.setScore(2);
                                }
                                if (v >= v1) {
                                    growthResult.setResult(value+"【一般】");
                                    growthResult.setScore(1);
                                }
                                growthResultVOList.add(growthResult);
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                    }
                }
            }
            return growthResultVOList;
        }
        return null;
    }
    /**
     *@Description 估值指标
     **/
    @SneakyThrows
    public List<ValuationResultVO> valuaConvertor(BusinessValuationDO valuation, BusinessValuationDO valuationAverage){
        List<ValuationResultVO> valuationResultVOList = new ArrayList<>();
        //DO对象判空
        if(!Objects.isNull(valuation)) {
            //遍历growthDO的成员变量
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(valuation);
            for (int i = 0; i < descriptors.length; i++) {
                //创建VO对象
                ValuationResultVO valuationResult = new ValuationResultVO();
                //传DO对象的id,做唯一标识
                valuationResult.setBVD_id(valuation.getId());
                //获取DO对象中成员变量名
                String name = descriptors[i].getName();
                valuationResult.setIndex(name);
                //判空条件，该变量名的属性不能为空，防止空指针异常
                if (!Objects.isNull(propertyUtilsBean.getNestedProperty(valuation, name))) {
                    //获取成员变量为name的值
                    String value = propertyUtilsBean.getNestedProperty(valuation, name).toString();
                    String growAve = propertyUtilsBean.getNestedProperty(valuationAverage, name).toString();
                    //成员变量名和属性值都不为空，则传入VO中
                    if (StringUtils.isNoneBlank(name, value)) {

                        switch (name) {
                            case "peg": {
                                double v = Double.parseDouble(value);
                                double v1 = Double.parseDouble(growAve);
                                valuationResult.setValue(Double.valueOf(value));
                                valuationResult.setModular("PEG");
                                if (v >= v1) {
                                    valuationResult.setResult(value+"【优秀】");
                                    valuationResult.setScore(2);
                                }
                                if (v < v1) {
                                    valuationResult.setResult(value+"【一般】");
                                    valuationResult.setScore(1);
                                }
                                valuationResultVOList.add(valuationResult);
                                break;
                            }
                            case "priceEarningsA21":
                            case "priceEarningsE22":
                            case "priceEarningsE23":
                            case "priceEarningsE24": {
                                double v = Double.parseDouble(value);
                                double v1 = Double.parseDouble(growAve);
                                valuationResult.setValue(Double.valueOf(value));
                                valuationResult.setModular("市盈率");
                                if (v >= v1) {
                                    valuationResult.setResult(value+"【优秀】");
                                    valuationResult.setScore(2);
                                }
                                if (v < v1) {
                                    valuationResult.setResult(value+"【一般】");
                                    valuationResult.setScore(1);
                                }
                                valuationResultVOList.add(valuationResult);
                                break;
                            }
                            case "marketSalesA21":
                            case "marketSalesE22":
                            case "marketSalesE23": {
                                double v = Double.parseDouble(value);
                                double v1 = Double.parseDouble(growAve);
                                valuationResult.setValue(Double.valueOf(value));
                                valuationResult.setModular("市销率");
                                if (v >= v1) {
                                    valuationResult.setResult(value+"【优秀】");
                                    valuationResult.setScore(2);
                                }
                                if (v < v1) {
                                    valuationResult.setResult(value+"【一般】");
                                    valuationResult.setScore(1);
                                }
                                valuationResultVOList.add(valuationResult);
                                break;
                            }
                            case "priceEarningsTtm": {
                                double v = Double.parseDouble(value);
                                double v1 = Double.parseDouble(growAve);
                                valuationResult.setValue(Double.valueOf(value));
                                valuationResult.setModular("市盈率");
                                if (v < v1) {
                                    valuationResult.setResult(value+"【优秀】");
                                    valuationResult.setScore(2);
                                }
                                if (v >= v1) {
                                    valuationResult.setResult(value+"【一般】");
                                    valuationResult.setScore(1);
                                }
                                valuationResultVOList.add(valuationResult);
                                break;
                            }
                            case "marketSalesTtm": {
                                double v = Double.parseDouble(value);
                                double v1 = Double.parseDouble(growAve);
                                valuationResult.setValue(Double.valueOf(value));
                                valuationResult.setModular("市销率");
                                if (v < v1) {
                                    valuationResult.setResult(value+"【优秀】");
                                    valuationResult.setScore(2);
                                }
                                if (v >= v1) {
                                    valuationResult.setResult(value+"【一般】");
                                    valuationResult.setScore(1);
                                }
                                valuationResultVOList.add(valuationResult);
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                    }
                }
            }
            return valuationResultVOList;
        }
        return null;
    }

    /**
     *@Description 杜邦分析指标
     **/
    @SneakyThrows
    public List<DupontResultVO> dupontConvertor(BusinessDupontDO dupont, BusinessDupontDO dupontAverage){
        List<DupontResultVO> dupontResultVOList = new ArrayList<>();
        //DO对象判空
        if(!Objects.isNull(dupont)) {
            //遍历growthDO的成员变量
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(dupont);
            for (int i = 0; i < descriptors.length; i++) {
                //创建VO对象
                DupontResultVO dupontResult = new DupontResultVO();
                //传DO对象的id,做唯一标识
                dupontResult.setBDD_id(dupont.getId());
                //获取DO对象中成员变量名
                String name = descriptors[i].getName();
                dupontResult.setIndex(name);
                //判空条件，该变量名的属性不能为空，防止空指针异常
                if (!Objects.isNull(propertyUtilsBean.getNestedProperty(dupont, name))) {
                    //获取成员变量为name的值
                    String value = propertyUtilsBean.getNestedProperty(dupont, name).toString();
                    String growAve = propertyUtilsBean.getNestedProperty(dupontAverage, name).toString();
                    //成员变量名和属性值都不为空，则传入VO中
                    if (StringUtils.isNoneBlank(name, value)) {

                        switch (name) {
                            case "roeYear3":
                            case "roeA19":
                            case "roeE20":
                            case "roeE21": {
                                double v = Double.parseDouble(value);
                                double v1 = Double.parseDouble(growAve);
                                dupontResult.setValue(Double.valueOf(value));
                                dupontResult.setModular("ROE");
                                if (v >= v1) {
                                    dupontResult.setResult(value+"【优秀】");
                                    dupontResult.setScore(2);
                                }
                                if (v < v1) {
                                    dupontResult.setResult(value+"【一般】");
                                    dupontResult.setScore(1);
                                }
                                dupontResultVOList.add(dupontResult);
                                break;
                            }
                            case "netInterestRateYear3":
                            case "netInterestRateA19":
                            case "netInterestRateE20":
                            case "netInterestRateE21": {
                                double v = Double.parseDouble(value);
                                double v1 = Double.parseDouble(growAve);
                                dupontResult.setValue(Double.valueOf(value));
                                dupontResult.setModular("净利率");
                                if (v >= v1) {
                                    dupontResult.setResult(value+"【优秀】");
                                    dupontResult.setScore(2);
                                }
                                if (v < v1) {
                                    dupontResult.setResult(value+"【一般】");
                                    dupontResult.setScore(1);
                                }
                                dupontResultVOList.add(dupontResult);
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                    }
                }
            }
            return dupontResultVOList;
        }
        return null;
    }


}

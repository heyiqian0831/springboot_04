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
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: BusinessReversalUtils
 * @Author: 何义祈安
 * @Description: 业务重组相关表List<VO>转DO
 * @Date: 2022/11/26 19:59
 * @Version: 1.0
 */
@Component
public class BusinessReversalUtils {

    @SneakyThrows
    public BusinessGrowthDO growthReversal(List<GrowthResultVO> growthResults) {
        BusinessGrowthDO growthDO = new BusinessGrowthDO();
        //capacityResults判空
        if (!Objects.isNull(growthResults)) {
            //id
            growthDO.setId(growthResults.get(0).getBGD_id());
            //遍历capacityResults
            for (GrowthResultVO growthResult : growthResults) {
                //防止空指针异常
                if (!Objects.isNull(growthResult.getValue())) {
                    //遍历DebtCapacityDO的成员变量
                    PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
                    PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(growthDO);
                    for (int i = 0; i < descriptors.length; i++) {
                        String name = descriptors[i].getName();
                        if (name.equals(growthResult.getIndex())) {
                            propertyUtilsBean.setNestedProperty(growthDO, name, growthResult.getValue());
                        }
                    }
                }
            }
        }
        return growthDO;
    }
    @SneakyThrows
    public BusinessValuationDO valuationReversal(List<ValuationResultVO> valuationResults) {
        BusinessValuationDO valuationDO = new BusinessValuationDO();
        //capacityResults判空
        if (!Objects.isNull(valuationResults)) {
            //id
            valuationDO.setId(valuationResults.get(0).getBVD_id());
            //遍历capacityResults
            for (ValuationResultVO valuationResult : valuationResults) {
                //防止空指针异常
                if (!Objects.isNull(valuationResult.getValue())) {
                    //遍历DebtCapacityDO的成员变量
                    PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
                    PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(valuationDO);
                    for (int i = 0; i < descriptors.length; i++) {
                        String name = descriptors[i].getName();
                        if (name.equals(valuationResult.getIndex())) {
                            propertyUtilsBean.setNestedProperty(valuationDO, name, valuationResult.getValue());
                        }
                    }
                }
            }
        }
        return valuationDO;
    }
    @SneakyThrows
    public BusinessDupontDO dupontReversal(List<DupontResultVO> dupontResults) {
        BusinessDupontDO dupontDO = new BusinessDupontDO();
        //capacityResults判空
        if (!Objects.isNull(dupontResults)) {
            //id
            dupontDO.setId(dupontResults.get(0).getBDD_id());
            //遍历capacityResults
            for (DupontResultVO dupontResult : dupontResults) {
                //防止空指针异常
                if (!Objects.isNull(dupontResult.getValue())) {
                    //遍历DebtCapacityDO的成员变量
                    PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
                    PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(dupontDO);
                    for (int i = 0; i < descriptors.length; i++) {
                        String name = descriptors[i].getName();
                        if (name.equals(dupontResult.getIndex())) {
                            propertyUtilsBean.setNestedProperty(dupontDO, name, dupontResult.getValue());
                        }
                    }
                }
            }
        }
        return dupontDO;
    }
}

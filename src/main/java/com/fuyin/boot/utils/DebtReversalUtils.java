package com.fuyin.boot.utils;

import com.fuyin.boot.mgb.entity.DebtCapacityDO;
import com.fuyin.boot.mgb.entity.DebtCashFlowDO;
import com.fuyin.boot.mgb.entity.DebtProfitabilityDO;
import com.fuyin.boot.model.vo.CapacityResultVO;
import com.fuyin.boot.model.vo.CashFlowResultVO;
import com.fuyin.boot.model.vo.ProfitabilityResultVO;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: DebtReversalUtils
 * @Author: 何义祈安
 * @Description: 债务重组爬取数据VO类型转为DO类型
 * @Date: 2022/11/26 18:09
 * @Version: 1.0
 */
@Component
public class DebtReversalUtils {

    @SneakyThrows
    public DebtCapacityDO capaReversal(List<CapacityResultVO> capacityResults) {
        DebtCapacityDO debtCapacityDO = new DebtCapacityDO();
        //capacityResults判空
        if (!Objects.isNull(capacityResults)) {
            //id
            debtCapacityDO.setId(capacityResults.get(0).getCDO_id());
            //遍历capacityResults
            for (CapacityResultVO capacityResult : capacityResults) {
                //防止空指针异常
                if (!Objects.isNull(capacityResult.getValue())) {
                    //遍历DebtCapacityDO的成员变量
                    PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
                    PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(debtCapacityDO);
                    for (int i = 0; i < descriptors.length; i++) {
                        String name = descriptors[i].getName();
                        if (name.equals(capacityResult.getIndex())) {
                            propertyUtilsBean.setNestedProperty(debtCapacityDO, name, capacityResult.getValue());
                        }
                    }
                }
            }
        }
        return debtCapacityDO;
    }

    @SneakyThrows
    public DebtProfitabilityDO profitReversal(List<ProfitabilityResultVO> profitabilityResults) {
        DebtProfitabilityDO profitabilityDO = new DebtProfitabilityDO();
        //profitabilityResults判空
        if (!Objects.isNull(profitabilityResults)) {
            //id
            profitabilityDO.setId(profitabilityResults.get(0).getPDO_id());
            //遍历profitabilityResults
            for (ProfitabilityResultVO profitabilityResult : profitabilityResults) {
                //防止空指针异常
                if (!Objects.isNull(profitabilityResult.getValue())) {
                    PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
                    PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(profitabilityDO);
                    for (int i = 0; i < descriptors.length; i++) {
                        String name = descriptors[i].getName();
                        if (name.equals(profitabilityResult.getIndex())) {
                            propertyUtilsBean.setNestedProperty(profitabilityDO, name, profitabilityResult.getValue());
                        }
                    }
                }
            }
        }
        return profitabilityDO;
    }

    @SneakyThrows
    public DebtCashFlowDO cashReversal(List<CashFlowResultVO> cashFlowResults) {
        DebtCashFlowDO debtCashFlowDO = new DebtCashFlowDO();
        //capacityResults判空
        if (!Objects.isNull(cashFlowResults)) {
            //id
            debtCashFlowDO.setId(cashFlowResults.get(0).getCFD_id());
            //遍历capacityResults
            for (CashFlowResultVO cashFlowResult : cashFlowResults) {
                //防止空指针异常
                if (!Objects.isNull(cashFlowResult.getValue())) {
                    //遍历DebtCashFlowDO的成员变量
                    PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
                    PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(debtCashFlowDO);
                    for (int i = 0; i < descriptors.length; i++) {
                        String name = descriptors[i].getName();
                        if (name.equals(cashFlowResult.getIndex())) {
                            propertyUtilsBean.setNestedProperty(debtCashFlowDO, name, cashFlowResult.getValue());
                        }
                    }
                }
            }
        }
        return debtCashFlowDO;
    }

}

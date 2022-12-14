package com.fuyin.boot.controller;

import com.fuyin.boot.model.param.DebtDataModifiedParam;
import com.fuyin.boot.model.param.InputEnterpriceParam;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.DebtService;
import com.fuyin.boot.service.impl.DebtServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: DebtDetailsController
 * @Author: 何义祈安
 * @Description: 债务重组控制类
 * @Date: 2022/11/15 20:21
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/debt")
public class DebtController {
    @Autowired
    private DebtService debtService;

    /**
    *@Description 用户通过债务重组爬取企业指标数据
     * 权限：debt:getDebtData:select
    **/
    @PostMapping(value = "/getDebtData")
    @PreAuthorize("hasAuthority('user:restructure:debt')")
    public ResponseResult getDebtData(@RequestBody InputEnterpriceParam enterpriceParam){
        return debtService.getDebtData(enterpriceParam);
    }
    /**
     *@Description 获取经用户确认的债务重组数据，生成方案
     **/
    @PostMapping(value = "/getDebtScheme")
    @PreAuthorize("hasAuthority('user:restructure:debt')")
    public ResponseResult getDebtScheme(@RequestBody DebtDataModifiedParam param){
        return debtService.getDebtScheme(param);
    }

}

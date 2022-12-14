package com.fuyin.boot.controller;

import com.fuyin.boot.model.param.DebtIndicatorsAllParam;
import com.fuyin.boot.model.param.InputEnterpriceParam;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.DebtAllIndicatorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName: DebtIndicatorsController
 * @Author: 何义祈安
 * @Description: 内容管理--债务诊断指标管理接口类
 * @Date: 2022/11/21 15:39
 * @Version: 1.0
 */
@Controller
@RequestMapping(value = "debtIndicators")
public class DebtIndicatorsController {
    @Autowired
    private DebtAllIndicatorsService debtAllService;

    /**
     *@Description 获取全部债务诊断指标，操作三张表
     **/
    @RequestMapping(value = "/getDebtIndicatorAll")
    @PreAuthorize("hasAuthority('admin:management:programme')")
    public ResponseResult getDebtIndicatorAll(){
        return debtAllService.getDebtIndicatorAll();
    }

    @PostMapping(value = "/saveOrUpdateAll")
    @PreAuthorize("hasAuthority('admin:management:programme')")
    public ResponseResult saveOrUpdateAll(@RequestBody List<DebtIndicatorsAllParam> params){
        return debtAllService.saveOrUpdateAll(params);
    }

}

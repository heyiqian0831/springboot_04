package com.fuyin.boot.controller;

import com.fuyin.boot.model.param.BusinessIndicatorsAllParam;
import com.fuyin.boot.model.param.DebtIndicatorsAllParam;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.BusinessAllIndicatorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @ClassName: BusinessIndicatorsController
 * @Author: 何义祈安
 * @Description: 让爱从头诊断指标管理接口类
 * @Date: 2022/11/21 15:45
 * @Version: 1.0
 */
@Controller
@RequestMapping(value = "/businessIndicators")
public class BusinessIndicatorsController {
    @Autowired
    private BusinessAllIndicatorsService allIndicatorsService;

    @RequestMapping(value = "/getAllIndicators")
    @PreAuthorize("hasAuthority('admin:management:programme')")
    public ResponseResult getAllIndicators(){
        return allIndicatorsService.getAll();
    }

    @PostMapping(value = "/saveOrUpdateAll")
    @PreAuthorize("hasAuthority('admin:management:programme')")
    public ResponseResult saveOrUpdateAll(
            @RequestBody List<BusinessIndicatorsAllParam> params){
        return allIndicatorsService.saveOrUpdateAl(params);
    }

}

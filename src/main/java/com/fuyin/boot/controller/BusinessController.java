package com.fuyin.boot.controller;

import com.fuyin.boot.mgb.entity.DetailUrlDO;
import com.fuyin.boot.model.param.BusinessModifiedParam;
import com.fuyin.boot.model.param.DebtDataModifiedParam;
import com.fuyin.boot.model.param.InputEnterpriceParam;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @ClassName: BusinessDetailsController
 * @Author: 何义祈安
 * @Description: 业务重组控制类
 * @Date: 2022/11/15 20:21
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/business")
public class BusinessController {
    @Autowired
    private BusinessService businessService;

    /**
     *@Description 业务重组接口
     * 权限：business:getBusinData:sleect
     **/
    @PostMapping(value = "/getBusinData")
    @PreAuthorize("hasAuthority('user:restructure:business')")
    public ResponseResult getBusinData(@RequestBody InputEnterpriceParam enterpriceParam){
        return businessService.getBusinData(enterpriceParam);
    }

    /**
     *@Description 获取经用户确认的债务重组数据，生成方案
     **/
    @PostMapping(value = "/getDebtScheme")
    @PreAuthorize("hasAuthority('user:restructure:business')")
    public ResponseResult getDebtScheme(@RequestBody BusinessModifiedParam param){
        return businessService.getDebtScheme(param);
    }

    @PostMapping(value = "/businessTest")
    public ResponseResult businessTest(@RequestBody InputEnterpriceParam enterpriceParam){
        System.out.println(enterpriceParam);
        return new ResponseResult(200,"chenggong");
    }


}

package com.fuyin.boot.controller;

import com.fuyin.boot.model.param.DebtSchemeParam;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.DebtSchemeManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @ClassName: DebtSchemeManageController
 * @Author: 何义祈安
 * @Description: 债务方案内容管理控制层
 * @Date: 2022/11/21 21:18
 * @Version: 1.0
 */
@Controller
@RequestMapping(value = "DebtScheme")
public class DebtSchemeManageController {
    @Autowired
    private DebtSchemeManageService debtSchemeManage;

    @RequestMapping(value = "/saveOrUpdate")
    @PreAuthorize("hasAuthority('admin:management:programme')")
    public ResponseResult saveOrUpdate(@RequestBody List<DebtSchemeParam> params){
        return debtSchemeManage.saveOrUpdate(params);
    }

}

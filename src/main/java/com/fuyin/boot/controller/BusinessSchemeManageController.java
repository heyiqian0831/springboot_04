package com.fuyin.boot.controller;

import com.fuyin.boot.mgb.entity.BusinessSolutionContentDO;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.BusinessSchemeManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @ClassName: BusinessSchemeManageController
 * @Author: 何义祈安
 * @Description:  业务重组方案内容管理
 * @Date: 2022/11/21 21:19
 * @Version: 1.0
 */
@Controller
@RequestMapping(value = "/businessScheme")
public class BusinessSchemeManageController {
    @Autowired
    private BusinessSchemeManageService businessSchemeService;

    @RequestMapping(value = "/saveOrUpdate")
    @PreAuthorize("hasAuthority('admin:management:programme')")
    public ResponseResult saveOrUpdate(@RequestBody List<BusinessSolutionContentDO> businessSolution){
        return businessSchemeService.saveOrUpdate(businessSolution);
    }
}

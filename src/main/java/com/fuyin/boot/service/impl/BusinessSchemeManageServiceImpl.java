package com.fuyin.boot.service.impl;

import com.fuyin.boot.mgb.entity.BusinessSolutionContentDO;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.BusinessSchemeManageService;
import com.fuyin.boot.service.BusinessSolutionContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: BusinessSchemeManageServiceImpl
 * @Author: 何义祈安
 * @Description: 业务重组方案内容管理接口方法实现类
 * @Date: 2022/11/21 22:18
 * @Version: 1.0
 */
@Service
public class BusinessSchemeManageServiceImpl implements BusinessSchemeManageService {
    @Autowired
    private BusinessSolutionContentService busSolutionService;

    /**
     *@Description 更新数据
     *@Param []
     *@return com.fuyin.boot.result.ResponseResult
     **/
    @Override
    public ResponseResult saveOrUpdate(List<BusinessSolutionContentDO> businessSolution) {
        busSolutionService.saveOrUpdateBatch(businessSolution);
        return new ResponseResult(200,"操作成功");
    }


}

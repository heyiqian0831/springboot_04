package com.fuyin.boot.service;

import com.fuyin.boot.mgb.entity.BusinessSolutionContentDO;
import com.fuyin.boot.result.ResponseResult;

import java.util.List;

public interface BusinessSchemeManageService {
    /**
     *@Description 更新数据
     **/
    ResponseResult saveOrUpdate(List<BusinessSolutionContentDO> businessSolution);


}

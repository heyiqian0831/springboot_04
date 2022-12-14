package com.fuyin.boot.service;

import com.fuyin.boot.model.param.BusinessIndicatorsAllParam;
import com.fuyin.boot.result.ResponseResult;

import java.util.List;

/**
 * @author 何义祈安
 */
public interface BusinessAllIndicatorsService {
    /**
    *@Description 获取所有有关业务重组的指标
    **/
    ResponseResult getAll();
    /**
     *@Description 确认提交操作方法接口
     **/
    ResponseResult saveOrUpdateAl(List<BusinessIndicatorsAllParam> params);
}

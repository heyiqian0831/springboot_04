package com.fuyin.boot.service;

import com.fuyin.boot.model.param.DebtIndicatorsAllParam;
import com.fuyin.boot.result.ResponseResult;

import java.util.List;

/**
 * @author 何义祈安
 */
public interface DebtAllIndicatorsService {
    /**
    *@Description 获取
    **/
    default ResponseResult getDebtIndicatorAll(){return null;}
    /**
     *@Description 新增和更新同时操作
     **/
    ResponseResult saveOrUpdateAll(List<DebtIndicatorsAllParam> params);
}

package com.fuyin.boot.service;

import com.fuyin.boot.model.param.DebtDataModifiedParam;
import com.fuyin.boot.model.param.InputEnterpriceParam;
import com.fuyin.boot.result.ResponseResult;

public interface DebtService {

    /**
    *@Description 获取债务重组数据
    **/
    ResponseResult getDebtData(InputEnterpriceParam enterpriceParam);
    /**
     *@Description 生成方案
     **/
    ResponseResult getDebtScheme(DebtDataModifiedParam param);
}

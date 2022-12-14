package com.fuyin.boot.service;

import com.fuyin.boot.mgb.entity.DetailUrlDO;
import com.fuyin.boot.model.param.BusinessModifiedParam;
import com.fuyin.boot.model.param.InputEnterpriceParam;
import com.fuyin.boot.result.ResponseResult;

public interface BusinessService {
    /**
    *@Description 获取详情页数据
    **/
    default ResponseResult getBusinData(InputEnterpriceParam enterpriceParam){return null;}

    /**
    *@Description 通过名称查询数据库，返回DetailUrl对象
    **/
    default DetailUrlDO selectDetailUrl(String name){return null;}

    /**
     *@Description 用户点击生成重组方案按钮，后端更新用户修改的指标，计算最终确定的指标的综合分数
     **/
    ResponseResult getDebtScheme(BusinessModifiedParam param);
}

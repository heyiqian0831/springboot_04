package com.fuyin.boot.service;

import com.fuyin.boot.model.param.DebtSchemeParam;
import com.fuyin.boot.result.ResponseResult;

import java.util.List;

/**
 * @author 何义祈安
 */
public interface DebtSchemeManageService {
    /**
    *@Description 更新数据
    **/
    default ResponseResult saveOrUpdate(List<DebtSchemeParam> params){return null;}
}

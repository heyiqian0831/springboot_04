package com.fuyin.boot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fuyin.boot.mgb.entity.ResourceDO;
import com.fuyin.boot.model.param.GetResourceByAdmin;
import com.fuyin.boot.result.ResponseResult;

/**
 * @author 何义祈安
 */
public interface ResourceService extends IService<ResourceDO> {
    /**
    *@Description 查权限
    **/
    ResponseResult getResource(GetResourceByAdmin getResourceByAdmin);
}

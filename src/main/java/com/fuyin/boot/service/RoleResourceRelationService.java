package com.fuyin.boot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fuyin.boot.mgb.entity.RoleResourceRelationDO;
import com.fuyin.boot.model.param.UpdateRoleWithResource;
import com.fuyin.boot.result.ResponseResult;

/**
 * @author 何义祈安
 */
public interface RoleResourceRelationService extends IService<RoleResourceRelationDO> {
    /**
     *@Description 编辑提交--修改角色对应权限
     **/
    ResponseResult updateRoleResource(UpdateRoleWithResource updateRoleWithResource);
}

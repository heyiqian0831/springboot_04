package com.fuyin.boot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fuyin.boot.mgb.entity.RoleDO;
import com.fuyin.boot.model.param.RoleNewInsertParam;
import com.fuyin.boot.model.param.RoleSelectPage;
import com.fuyin.boot.model.param.UpdateRoleWithResource;
import com.fuyin.boot.result.ResponseResult;

/**
 * @author 何义祈安
 */
public interface RoleService extends IService<RoleDO> {
    /**
     *@Description 获取所有角色
     **/
    ResponseResult getRoleAll();
    /**
     *@Description 分页查询
     **/
    ResponseResult getRolePage(RoleSelectPage roleSelectPage);
    /**
     *@Description 查询某一个角色
     **/
    ResponseResult getRoleOne(String name);
    /**
     *@Description 新增角色
     **/
    ResponseResult insertRole(RoleNewInsertParam roleNewInsertParam);
    /**
     *@Description 更新角色
     **/
    ResponseResult updateRoleResource(UpdateRoleWithResource updateRoleWithResource);
    /**
     *@Description 删除角色
     **/
    ResponseResult deleteRole(UpdateRoleWithResource updateRoleWithResource);


}

package com.fuyin.boot.controller;

import com.fuyin.boot.model.param.UpdateRoleWithResource;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.RoleResourceRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: RoleResourceRelationController
 * @Author: 何义祈安
 * @Description: 角色权限操作控制层
 * @Date: 2022/11/19 18:36
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/roleResoutce")
public class RoleResourceRelationController {
    @Autowired
    private RoleResourceRelationService roleResource;

    /**
     *@Description 编辑提交--修改角色对应权限,返回参数：用户id,权限desc,根据id和权限更新role_resource_relation表
     * @return 返回该角色拥有的权限数量
     **/
    @PostMapping(value = "/updateRoleResource")
    @PreAuthorize("hasAuthority('admin:management:role')")
    public ResponseResult updateRoleResource(@RequestBody UpdateRoleWithResource updateRoleWithResource){
        return roleResource.updateRoleResource(updateRoleWithResource);
    }

}

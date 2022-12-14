package com.fuyin.boot.controller;

import com.fuyin.boot.mgb.entity.RoleDO;
import com.fuyin.boot.model.param.InsertUserParam;
import com.fuyin.boot.model.param.RoleNewInsertParam;
import com.fuyin.boot.model.param.RoleSelectPage;
import com.fuyin.boot.model.param.UpdateRoleWithResource;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName RoleController
 * @Description 角色管理控制层
 * @Author 何义祈安
 * @Date 2022/9/14 16:49
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    /**
     *@Description 获取所有角色信息
     **/
    @PostMapping(value = "/getAllRole")
    @PreAuthorize("hasAuthority('admin:management:role')")
    public ResponseResult getAllRole(){
        return roleService.getRoleAll();
    }

    /**
    *@Description 角色管理分页查询
    **/
    @PostMapping(value = "/getRolePage")
    @PreAuthorize("hasAuthority('admin:management:role')")
    public ResponseResult getRolePage(@RequestBody RoleSelectPage roleSelectPage){
        return roleService.getRolePage(roleSelectPage);
    }

    /**
    *@Description 根据角色名称查询
    **/
    @PostMapping(value = "/getRoleOne")
    @PreAuthorize("hasAuthority('admin:management:role')")
    public ResponseResult getRoleOne(@RequestBody String name){
        return roleService.getRoleOne(name);
    }

    /**
     *@Description 新增角色,传入参数：角色基本信息+权限信息 角色名、角色描述、权限名称
     **/
    @PostMapping(value = "/insertRole")
    @PreAuthorize("hasAuthority('admin:management:role')")
    public ResponseResult insertRole(@RequestBody RoleNewInsertParam roleNewInsertParam){
        return roleService.insertRole(roleNewInsertParam);
    }

    /**
     *@Description 删除角色（逻辑删除）
     **/
    @PostMapping(value = "/deleteRole")
    @PreAuthorize("hasAuthority('admin:management:role')")
    public ResponseResult deleteRole(@RequestBody UpdateRoleWithResource updateRoleWithResource){
        return roleService.deleteRole(updateRoleWithResource);
    }
}

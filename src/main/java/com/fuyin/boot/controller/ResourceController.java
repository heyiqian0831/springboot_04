package com.fuyin.boot.controller;

import com.fuyin.boot.mgb.entity.ResourceDO;
import com.fuyin.boot.model.param.GetResourceByAdmin;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: ResourceController
 * @Author: 何义祈安
 * @Description: 权限操作控制层
 * @Date: 2022/11/19 16:16
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    /**
     *@Description 角色管理，点击编辑 --- 查询该角色权限
     * 接收前端参数：角色唯一标识id
     **/
    @PostMapping(value = "/getResoursceByRoleId")
    @PreAuthorize("hasAuthority('admin:management:role')")
    public ResponseResult getRoleOne(@RequestBody GetResourceByAdmin getResourceByAdmin){
        return resourceService.getResource(getResourceByAdmin);
    }
}

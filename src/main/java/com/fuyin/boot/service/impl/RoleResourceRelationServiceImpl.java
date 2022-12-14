package com.fuyin.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuyin.boot.mgb.entity.ResourceDO;
import com.fuyin.boot.mgb.entity.RoleResourceRelationDO;
import com.fuyin.boot.mgb.mapper.RoleResourceRelationMapper;
import com.fuyin.boot.model.param.UpdateRoleWithResource;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.ResourceService;
import com.fuyin.boot.service.RoleResourceRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: RoleResourceRelationServiceImpl
 * @Author: 何义祈安
 * @Description: TODO
 * @Date: 2022/11/19 17:53
 * @Version: 1.0
 */
@Service
public class RoleResourceRelationServiceImpl extends ServiceImpl<RoleResourceRelationMapper,RoleResourceRelationDO> implements RoleResourceRelationService {
    @Autowired
    private ResourceService resourceService;
    /**
     *修改角色对应权限,参数：角色id,角色描述，权限desc
     **/
    @Override
    public ResponseResult updateRoleResource(UpdateRoleWithResource updateRoleWithResource) {
        //先删除旧的数据
        LambdaQueryWrapper<RoleResourceRelationDO> wrapperRemove = new LambdaQueryWrapper<>();
        wrapperRemove.eq(RoleResourceRelationDO::getRoleId,updateRoleWithResource.getRoleId());
        this.remove(wrapperRemove);
        //获取权限信息
        List<String> resource = updateRoleWithResource.getResource();
        List<Long> resourceId = new ArrayList<>();
        for (String s : resource) {
            LambdaQueryWrapper<ResourceDO> wrapperResource = new LambdaQueryWrapper<>();
            wrapperResource.eq(ResourceDO::getDescription,s);
            ResourceDO resourceDO = resourceService.getOne(wrapperResource);
            if(!Objects.isNull(resourceDO)){
                resourceId.add(resourceDO.getId());
            }
        }
        for (Long aLong : resourceId) {
            RoleResourceRelationDO roleResource = new RoleResourceRelationDO();
            roleResource.setRoleId(updateRoleWithResource.getRoleId());
            roleResource.setResourceId(aLong);
            this.saveOrUpdate(roleResource);
        }
        return new ResponseResult(200,"操作成功",this.count(wrapperRemove));
    }
}

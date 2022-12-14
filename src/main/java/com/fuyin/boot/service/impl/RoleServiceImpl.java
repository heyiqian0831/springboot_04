package com.fuyin.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuyin.boot.mgb.entity.ResourceDO;
import com.fuyin.boot.mgb.entity.RoleDO;
import com.fuyin.boot.mgb.entity.RoleResourceRelationDO;
import com.fuyin.boot.mgb.mapper.RoleMapper;
import com.fuyin.boot.model.param.RoleNewInsertParam;
import com.fuyin.boot.model.param.RoleSelectPage;
import com.fuyin.boot.model.param.UpdateRoleWithResource;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.ResourceService;
import com.fuyin.boot.service.RoleResourceRelationService;
import com.fuyin.boot.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

/**
 * @ClassName: RoleServiceImpl
 * @Author: 何义祈安
 * @Description: 角色接口实现类
 * @Date: 2022/11/18 20:07
 * @Version: 1.0
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper,RoleDO> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private RoleResourceRelationService roleResourceService;

    /**
    *@Description 查询所有
    **/
    @Override
    public ResponseResult getRoleAll() {
        Map<String,Object> map = new HashMap<>();
        try {
            List<RoleDO> roleDOS = roleMapper.selectList(null);
            map.put("date",roleDOS);
        } catch (Exception e) {
            throw new RuntimeException("查询出错"+e);
        }
        return new ResponseResult(200,"操作成功",map);
    }
    /**
     *@Description 分页查询
     **/
    @Override
    public ResponseResult getRolePage(RoleSelectPage roleSelectPage) {
        IPage<RoleDO> page = new Page<>();
        //设置每页条数
        page.setSize(roleSelectPage.getSize());
        //设置查询第几页
        page.setCurrent(roleSelectPage.getCurrent());
        IPage<RoleDO> rolePage = roleMapper.selectPage(page, null);
        return new ResponseResult(200,"操作成功",rolePage.getRecords());
    }

    /**
     *@Description 根据角色名称查询角色
     **/
    @Override
    public ResponseResult getRoleOne(String name) {
        RoleDO roleDO = roleMapper.selectOne(lambdaUpdate().getWrapper().eq(RoleDO::getDescription, name));
        if (Objects.isNull(roleDO)){
            throw new RuntimeException("查询出错");
        }
        return new ResponseResult(200,"查询成功",roleDO);
    }

    /**
     *@Description 新增角色,传入参数：角色基本信息+权限信息 角色名、角色描述、权限名称
     **/
    @Override
    public ResponseResult insertRole(RoleNewInsertParam roleNewInsertParam) {
        RoleDO roleDO = new RoleDO();
        RoleResourceRelationDO roleResource = new RoleResourceRelationDO();
        roleDO.setName(roleNewInsertParam.getRoleName());
        roleDO.setDescription(roleNewInsertParam.getRoleDesc());
        try {
            boolean b = this.saveOrUpdate(roleDO);
            if(!b){
                throw new RuntimeException("新增失败");
            }
            System.out.println("能不能拿到id"+roleDO.getId());
            roleResource.setRoleId(roleDO.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //获取权限信息
        List<String> resource = roleNewInsertParam.getResource();
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
            roleResource.setResourceId(aLong);
            roleResourceService.saveOrUpdate(roleResource);
        }
        return new ResponseResult(200,"操作成功",roleDO);
    }

    /**
     *@Description 编辑提交--修改角色对应权限,修改角色对应权限，返回参数：用户id,权限desc,根据id和权限更新role_resource_relation表
     **/
    @Override
    public ResponseResult updateRoleResource(UpdateRoleWithResource updateRoleWithResource) {
        return null;
    }

    @Override
    public ResponseResult deleteRole(UpdateRoleWithResource updateRoleWithResource) {
        boolean b = this.removeById(updateRoleWithResource.getRoleId());
        return new ResponseResult(200,"操作成功");
    }
}

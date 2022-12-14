package com.fuyin.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuyin.boot.mgb.entity.ResourceDO;
import com.fuyin.boot.mgb.entity.RoleDO;
import com.fuyin.boot.mgb.mapper.ResourceMapper;
import com.fuyin.boot.mgb.mapper.RoleMapper;
import com.fuyin.boot.model.domain.RoleResource;
import com.fuyin.boot.model.param.GetResourceByAdmin;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.ResourceService;
import com.fuyin.boot.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * @ClassName: ResourceServiceImpl
 * @Author: 何义祈安
 * @Description: TODO
 * @Date: 2022/11/19 17:20
 * @Version: 1.0
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper,ResourceDO> implements ResourceService {
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private RoleMapper roleMapper;

    /**
     *@Description 查权限，多表联查
     **/
    @Override
    public ResponseResult getResource(GetResourceByAdmin getResourceByAdmin) {
        List<RoleResource> resourceByRoleId = resourceMapper.getResourceByRoleId(getResourceByAdmin.getId());
        if(Objects.isNull(resourceByRoleId)){
            //角色不能没有权限而del_flag又是0
            //获取角色逻辑状态
            LambdaQueryWrapper<RoleDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(RoleDO::getId,getResourceByAdmin.getId());
            RoleDO role = roleMapper.selectOne(wrapper);
            //如果是0，说明正常状态，但是又查不到权限，数据库有问题
            if(role.getDelFlag()==0){
                throw new RuntimeException("角色没有权限，数据库有问题");
            }
        }
        return new ResponseResult(200,"操作成功",resourceByRoleId);
    }
}

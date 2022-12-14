package com.fuyin.boot.mgb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuyin.boot.mgb.entity.ResourceDO;
import com.fuyin.boot.model.domain.RoleResource;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
/**
 * @author 何义祈安
 */
@Mapper
public interface ResourceMapper extends BaseMapper<ResourceDO> {
    /**
     *@Description 根据用户id查询用户权限
     **/
    List<String> selectNameByUserPrimaryKey(Long id);
    /**
     *@Description 根据管理员id查询用户权限
     **/
    List<String> selectNameByAdminPrimaryKey(Long id);
    /**
    *@Description 根据Role.id查询权限
    **/
    List<RoleResource> getResourceByRoleId(Long id);
}

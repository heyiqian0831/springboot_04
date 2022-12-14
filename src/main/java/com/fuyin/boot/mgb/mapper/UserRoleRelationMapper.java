package com.fuyin.boot.mgb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuyin.boot.mgb.entity.UserRoleRelationDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 何义祈安
 */
@Mapper
public interface UserRoleRelationMapper extends BaseMapper<UserRoleRelationDO> {
    int insertCommonUserRoleRelation(Long userId);

}

package com.fuyin.boot.mgb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuyin.boot.mgb.entity.RoleDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectKey;

/**
 * @author 何义祈安
 ***
 *@Description
 *@Param
 *@return
 **/
@Mapper
public interface RoleMapper extends BaseMapper<RoleDO> {
    @Override
    @SelectKey(statement="select LAST_INSERT_ID()",keyProperty = "id",before = false,resultType = Long.class)
    int insert(RoleDO entity);
}

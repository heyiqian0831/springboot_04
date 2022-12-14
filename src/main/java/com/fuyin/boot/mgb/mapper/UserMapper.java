package com.fuyin.boot.mgb.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.fuyin.boot.mgb.entity.UserDO;
import com.fuyin.boot.model.domain.UserAndRoles;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 何义祈安
 */
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

    /**
    *@Description 动态更新用户昵称、手机号
    **/
    int updateUsernamePhoneNumber(UserDO userDO);


    List<UserAndRoles> getAllUserAllRole(@Param(Constants.WRAPPER) Wrapper<UserAndRoles> queryWrapper);
}

package com.fuyin.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fuyin.boot.domain.LoginUser;
import com.fuyin.boot.mgb.entity.UserDO;
import com.fuyin.boot.mgb.mapper.ResourceMapper;
import com.fuyin.boot.mgb.mapper.UserMapper;
import com.fuyin.boot.mgb.mapper.UserRoleRelationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName UserDetailsServiceImpl
 * @Description
 * @Author 何义祈安
 * @Date 2022/8/15 16:18
 * @Version 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private UserRoleRelationMapper userRoleRelationMapper;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        //根据用户名查询用户信息
//        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(UserDO::getUsername,username);
//        UserDO userDO = userMapper.selectOne(wrapper);

//        UserDOExample userDOExample = new UserDOExample();
//        userDOExample.createCriteria().andUsernameEqualTo(username);
//        List<UserDO> userDOList = userMapper.selectByExample(userDOExample);

        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        wrapper.eq("phone_Number",phoneNumber);
        List<UserDO> userDOS = userMapper.selectList(wrapper);

        //如果查询不到数据就通过抛出异常来给出提示
        if (CollectionUtils.isEmpty(userDOS) || userDOS.size() > 1) {
            throw new RuntimeException("数据未找到");
        }

        if(Objects.isNull(userDOS.get(0))){
            throw new RuntimeException("用户名或密码错误");
        }
        UserDO userDO = userDOS.get(0);
        System.out.println(userDO.getPassword());
        //TODO 根据用户查询权限信息 添加到LoginUser中, 用ResourcesMapper
//        List<String> list = new ArrayList<>();
//        list.add("hello");

        //Arrays.asList("hello"): 直接生成一个list集合
//        List<String> list = new ArrayList<>(Arrays.asList("hello"));

        //通过查询menuMapper
//        List<String> list = menuMapper.selectPermsByUserId(user.getId());

        //通过用户ID查询用户所拥有的角色id，查user_role_relation
//        UserRoleRelationDOExample userRoleRelationDOExample = new UserRoleRelationDOExample();
//        userRoleRelationDOExample.createCriteria().andUserIdEqualTo(userDO.getId());
//        List<UserRoleRelationDO> userRoleRelationDOS = userRoleRelationMapper.selectByExample(userRoleRelationDOExample);
        //通过用户的角色id查用户角色信息，查role
        //通过用户角色查用户权限id，查role_resource_relation
        //通过用户权限id查用户权限信息，查resource
        List<String> list = resourceMapper.selectNameByUserPrimaryKey(userDO.getId());
        //封装成UserDetails对象返回
        return new LoginUser(userDO,list);
    }

    /**
    *@Description 用于JWTFilter中的加载用户
    **/
    public UserDetails loadUser(String username) throws UsernameNotFoundException {
        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        List<UserDO> userDOS = userMapper.selectList(wrapper);
        //如果查询不到数据就通过抛出异常来给出提示
        if (CollectionUtils.isEmpty(userDOS) || userDOS.size() > 1) {
            throw new RuntimeException("数据未找到");
        }

        if(Objects.isNull(userDOS.get(0))){
            throw new RuntimeException("用户名或密码错误");
        }
        UserDO userDO = userDOS.get(0);
        List<String> list = resourceMapper.selectNameByUserPrimaryKey(userDO.getId());
        //封装成UserDetails对象返回
        return new LoginUser(userDO,list);
    }

}

package com.fuyin.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fuyin.boot.domain.LoginUser;
import com.fuyin.boot.mgb.entity.UserDO;
import com.fuyin.boot.mgb.mapper.UserMapper;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.LoginService;
import com.fuyin.boot.utils.JwtTokenUtil;
import com.fuyin.boot.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName LoginServiceImpl
 * @Description
 * @Author 何义祈安
 * @Date 2022/8/16 12:02
 * @Version 1.0
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Override
    public ResponseResult login(UserDO userDO) {
        //将用户输入的用户名和密码封装成authentication对象
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDO.getPhoneNumber(),userDO.getPassword());
        //authenticationManager调用userDetailService认证账号密码
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //！！这里的认证通过是因为authenticationManager调用userDetailsServiceImpl进行用户密码查询，userDetailsServiceImpl没有抛出异常说明查询成功
        // 就会返回userDetails对象，封装着查询到的用户名密码认证信息等，接着再到AbstractUserDetailsAuthenticationProvider接口实现类进行验证，如果验证成功
        // 就会将查到的用户信息（userDetails）封装成 authentication对象返回给 authenticationManager
        // 所以如果authentication 不为空就是验证成功

        //判断是否验证成功，成功则返回token，不成功抛出异常
//        if(Objects.isNull(authenticate)){
//            throw new RuntimeException("用户名或密码错误");
//        }

        //获取登录成功的用户对象
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        //登录成功，更新用户的最新登录时间
        UserDO userDO1 = loginUser.getUserDO();
////        if(setUserLastLoginTime(userDO1) != 1){
////            throw new RuntimeException("更细登录失败失败");
////        }
//
//        //获取用户id，把完整的用户信息存入redis，userid作为key
//        String userId = loginUser.getUserDO().getId().toString();
//        redisCache.setCacheObject("login:"+userId,loginUser);
//
//        //将UserDetails类型的用户信息、是否为管理员、用户id、用户最后登录时间生成token
//        String token = JwtUtil.createJWT(userId);
////        String token = jwtTokenUtil.generateToken(loginUser, false, userDO1.getId(), userDO1.getLastLoginTime());
        Map<String,Object> map = new HashMap<>();
//        map.put("token",token);
        map.put("??",userDO1);
//
//        //把token响应给前端
        return new ResponseResult(200,"登陆成功",map);
    }

    @Override
    public int setUserLastLoginTime(UserDO userDO1) {
        //选择性更新
        UserDO userIdAndTime = new UserDO();
        userIdAndTime.setId(userDO1.getId());
        userIdAndTime.setLastLoginTime(new Date());
        //设置更新条件
        UpdateWrapper<UserDO> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",userDO1.getId());
        //用mymabits进行选择更新的时候，传入的实体对象里只能有要更新的属性有值，不能有其他的
        return userMapper.update(userIdAndTime, wrapper);
    }

    @Override
    public String test() {
        return "testLoginService";
    }


    @Override
    public ResponseResult logout() {
        //获取SecurityContextHolder
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        if(Objects.isNull(loginUser)){
            throw new RuntimeException("SecurityContextHolder获取失败");
        }
        //从SecurityContextHolder中获取userid, 并从redis中删除用户认证信息
        Long userid = loginUser.getUserDO().getId();
        redisCache.deleteObject("login:"+userid);
        //返回操作成功信息
        return new ResponseResult(200,"注销成功",userid);
    }



}

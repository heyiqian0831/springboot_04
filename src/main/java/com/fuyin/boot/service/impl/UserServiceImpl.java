package com.fuyin.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuyin.boot.domain.LoginAdmin;
import com.fuyin.boot.domain.LoginUser;
import com.fuyin.boot.enums.DataStatus;
import com.fuyin.boot.mgb.entity.AdminDO;
import com.fuyin.boot.mgb.entity.DetailUrlDO;
import com.fuyin.boot.mgb.entity.LoginLogDO;
import com.fuyin.boot.mgb.entity.UserDO;
import com.fuyin.boot.mgb.mapper.DetailUrlMapper;
import com.fuyin.boot.mgb.mapper.ResourceMapper;
import com.fuyin.boot.mgb.mapper.UserMapper;
import com.fuyin.boot.mgb.mapper.UserRoleRelationMapper;
import com.fuyin.boot.model.domain.UserAgentInfo;
import com.fuyin.boot.model.param.InputEnterpriceParam;
import com.fuyin.boot.model.param.UserLoginParam;
import com.fuyin.boot.model.param.UserOldNewPasswordParam;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.LoginLogService;
import com.fuyin.boot.service.UserService;
import com.fuyin.boot.utils.JwtTokenUtil;
import com.fuyin.boot.utils.RedisCache;
import com.fuyin.boot.utils.UserAgentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author 何义祈安
 * @Date 2022/9/12 17:32
 * @Version 1.0
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleRelationMapper userRoleRelationMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private LoginLogService loginLogService;
    @Autowired
    private UserAgentUtil userAgentUtil;

    @Override
    public ResponseResult login(UserLoginParam userLoginParam, HttpServletRequest request) {
        String token = null;
        if(Objects.isNull(userLoginParam.getPhoneNumber())){
            throw new RuntimeException("登录错误，手机号不能为空");
        }
        //loadUserByPhoneNumber，返回UserDetails
        LoginUser loginUser = (LoginUser) loadUserByPhoneNumber(userLoginParam.getPhoneNumber());
        if (Objects.isNull(loginUser)){
            throw new RuntimeException("登录错误，查询结果为空");
        }
        //获取请求头中的信息
        LoginLogDO loginLogDO = getLoginLogDO(loginUser, request);
        //对比查到的密码和输入的密码
        //TODO ！！！判断密码是加密形式的还是铭文形式的（登录接口能正常使用应该没问题吧）
        if (!passwordEncoder.matches(userLoginParam.getPassword(), loginUser.getPassword())){
            loginLogDO.setLoginStatus("fail");
            loginLogService.insertLog(loginLogDO);
            throw new RuntimeException("登录错误，密码不正确");
        }
        //密码正确，更新最新登录时间
        UserDO userDOForLastLoginTime = loginUser.getUserDO();
        int i = setUserLastLoginTime(userDOForLastLoginTime);
        //更新完后获取最新的用户数据
        UserDO userDO = new UserDO();
        userDO = userMapper.selectById(userDOForLastLoginTime.getId());
        LoginUser loginUserNew = (LoginUser) loadUserByPhoneNumber(userDO.getPhoneNumber());
        // UserDetails封装成authentication对象
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
        //authentication对象存到SecurityContextHolder中
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //获取管理员id将用户信息存到redis中
        String userId = loginUser.getUserDO().getId().toString();
        //将管理员信息存入redis中，key: adminid:#{id}
        redisCache.setCacheObject("login:"+userId,loginUserNew);
        loginUser.getUserDO().getLastLoginTime();

        loginLogDO.setLoginStatus("success");
        //新增日志
        loginLogService.insertLog(loginLogDO);

        //将UserDetails类型的用户信息、是否为管理员、用户id、用户最后登录时间生成token
        token = jwtTokenUtil.generateToken(
                loginUser,false,userDO.getId(),userDO.getLastLoginTime());

        //将管理员id,更新时间，jwt打包成map
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        //返回responseResul对象
        return new ResponseResult(200,"登录成功",map);
    }

    private LoginLogDO getLoginLogDO(LoginUser loginUser, HttpServletRequest request) {
        //获取请求头中的信息
        UserAgentInfo agentInfo = userAgentUtil.getUserAgentInfo(request);
        LoginLogDO loginLogDO = new LoginLogDO();
        loginLogDO.setUserId(loginUser.getUserDO().getId());
        loginLogDO.setUsername(loginUser.getUsername());
        loginLogDO.setIp(agentInfo.getIp());
        loginLogDO.setBrowser(agentInfo.getBrowserType());
        loginLogDO.setOperatingSystem(agentInfo.getOs());
        return loginLogDO;
    }

    /**
     * 刷新用户最后登录时间
     **/
    @Override
    public int setUserLastLoginTime(UserDO userDO) {
        //获取管理员id
        UserDO userForLastLoginTime = new UserDO();
        userForLastLoginTime.setId(userDO.getId());
        userForLastLoginTime.setLastLoginTime(new Date());
        //设置更新条件，id
        UpdateWrapper<UserDO> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",userDO.getId());
        int update = userMapper.update(userForLastLoginTime, wrapper);
        return update;
    }

    private UserDetails loadUserByPhoneNumber(String phoneNumber) {
        //根据用户手机号查询用户信息
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserDO::getPhoneNumber,phoneNumber);
        List<UserDO> userDOS = userMapper.selectList(wrapper);
        //如果查询不到数据就通过抛出异常来给出提示
        if (CollectionUtils.isEmpty(userDOS) || userDOS.size() > 1) {
            throw new RuntimeException("数据未找到");
        }
        if(Objects.isNull(userDOS.get(0))){
            throw new RuntimeException("用户名或密码错误");
        }
        UserDO userDO = userDOS.get(0);
        //TODO 根据用户查询权限信息 添加到LoginUser中, 用ResourcesMapper
        List<String> list = resourceMapper.selectNameByUserPrimaryKey(userDO.getId());
        return new LoginUser(userDO,list);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserDO::getUsername,username);
        List<UserDO> userDOS = userMapper.selectList(wrapper);
        //如果查询不到数据就通过抛出异常来给出提示
        if (CollectionUtils.isEmpty(userDOS) || userDOS.size() > 1) {
            throw new RuntimeException("数据未找到");
        }

        if(Objects.isNull(userDOS.get(0))){
            throw new RuntimeException("用户名或密码错误");
        }
        UserDO userDO = userDOS.get(0);
        //TODO 根据用户查询权限信息 添加到LoginUser中, 用ResourcesMapper
        List<String> list = resourceMapper.selectNameByUserPrimaryKey(userDO.getId());
        return new LoginUser(userDO,list);
    }

    //注册新用户
    @Override
    public ResponseResult<Integer> register(UserDO usersDO) {
        Map<String,Object> map = new HashMap<>();

        //获取前端传入用户
        if(Objects.isNull(usersDO)){
            throw new RuntimeException("输入数据错误");
        }
        //给新增用户加上用户状态和加密密码
        usersDO.setStatus(DataStatus.ENABLE.getCode());
        usersDO.setPassword(passwordEncoder.encode(usersDO.getPassword()));
        //将数据插入到用户表中，判断如果影响数据库条数不等于1就新增失败
        int insert = userMapper.insert(usersDO);
        if(insert != 1){
            throw new RuntimeException("注册失败");
        }
        //TODO 给新增用户添加用户角色
        int i = userRoleRelationMapper.insertCommonUserRoleRelation(usersDO.getId());
        if (i != 1){
            throw new RuntimeException("添加普通用户角色失败");
        }
        //返回注册成功信息
        map.put("result",insert);
        return new ResponseResult<>(200,"注册成功",insert);
    }

    //更新昵称和手机号
    @Override
    public ResponseResult updateUsernamePhoneNumber(UserDO userDO) {
        if(Objects.isNull(userDO.getUsername()) && Objects.isNull(userDO.getPhoneNumber())){
            throw new RuntimeException("输入错误");
        }
        //拿到当前用户的id
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        if(Objects.isNull(loginUser)){
            throw new RuntimeException("SecurityContextHolder获取失败");
        }
        Long userid = loginUser.getUserDO().getId();
        UserDO userNew = new UserDO();
        userNew.setId(userid);
        //判断昵称和手机号是否都非空
        if(!Objects.isNull(userDO.getUsername()) && !Objects.isNull(userDO.getPhoneNumber())){
            //昵称手机号都不为空
            userNew.setUsername(userDO.getUsername());
            userNew.setPhoneNumber(userDO.getPhoneNumber());
            if (userMapper.updateById(userNew) !=1){
                throw new RuntimeException("更新失败");
            }
        }if(!Objects.isNull(userDO.getUsername()) && Objects.isNull(userDO.getPhoneNumber())) {
            //昵称不为空
            userNew.setUsername(userDO.getUsername());
            if (userMapper.updateById(userNew) !=1){
                throw new RuntimeException("更新失败");
            }
        }if (Objects.isNull(userDO.getUsername()) && !Objects.isNull(userDO.getPhoneNumber())){
            //手机号不为空
            userNew.setPhoneNumber(userDO.getPhoneNumber());
            if (userMapper.updateById(userNew) !=1){
                throw new RuntimeException("更新失败");
            }
        }
        return new ResponseResult(200,"修改成功");
    }

    //动态更新昵称和手机号(和上一个方法选用)
    @Override
    public ResponseResult updateUsernamePhoneNumberSet(UserDO userDO) {
        if(Objects.isNull(userDO.getUsername()) && Objects.isNull(userDO.getPhoneNumber())){
            throw new RuntimeException("输入错误");
        }
        //拿到当前用户的id
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        if(Objects.isNull(loginUser)){
            throw new RuntimeException("SecurityContextHolder获取失败");
        }
        Long userid = loginUser.getUserDO().getId();
        UserDO userNew = new UserDO();
        userNew.setId(userid);
        if(!Objects.isNull(userDO.getUsername())){
            userNew.setUsername(userDO.getUsername());
            if(userMapper.updateUsernamePhoneNumber(userNew) != 1){
                throw new RuntimeException("更新失败");
            }
        }if(!Objects.isNull(userDO.getPhoneNumber())){
            userNew.setPhoneNumber(userDO.getPhoneNumber());
            if(userMapper.updateUsernamePhoneNumber(userNew) != 1){
                throw new RuntimeException("更新失败");
            }
        }
        return new ResponseResult(200,"修改成功");
    }

    //更新用户密码，传入两个参数
    @Override
    public ResponseResult updatePassword(UserOldNewPasswordParam oldNewPassword) {
        //拿到当前用户旧密码
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //在authentication对象里的是从缓存中找到的用户数据，查出来的密码是加密过的
        //判断用户输入旧的密码是否正确
        if (!passwordEncoder.matches(oldNewPassword.getOldPassword(),loginUser.getPassword())){
            throw new RuntimeException("更新失败，旧密码错误");
        }
        //判断用户输入的新密码是否为空
        if (oldNewPassword.getNewPassword().isEmpty()){
            throw new RuntimeException("更新失败，新密码不能为空");
        }
        //修改用户密码
        UserDO userDO = new UserDO();
        userDO.setId(loginUser.getUserDO().getId());
        userDO.setPassword(passwordEncoder.encode(oldNewPassword.getNewPassword()));
        int i = userMapper.updateById(userDO);
        if (i !=1) {
            throw new RuntimeException("更新失败");
        }
            Map<String,Object> map = new HashMap<>();
        map.put("操作数",i);
        //返回修改信息
        return new ResponseResult(200,"更新成功",map);
    }





}

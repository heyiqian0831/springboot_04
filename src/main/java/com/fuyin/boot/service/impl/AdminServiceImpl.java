package com.fuyin.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fuyin.boot.domain.LoginAdmin;
import com.fuyin.boot.enums.Roles;
import com.fuyin.boot.mgb.entity.*;
import com.fuyin.boot.mgb.mapper.*;
import com.fuyin.boot.model.domain.UserAgentInfo;
import com.fuyin.boot.model.domain.UserAndRoles;
import com.fuyin.boot.model.param.AdminLoginParam;
import com.fuyin.boot.model.param.InsertUserParam;
import com.fuyin.boot.model.param.UserSelectPage;
import com.fuyin.boot.model.param.UsernameAndPhoneNumberParam;
import com.fuyin.boot.result.ResponseResult;
import com.fuyin.boot.service.AdminService;
import com.fuyin.boot.service.LoginLogService;
import com.fuyin.boot.service.SystemLogService;
import com.fuyin.boot.utils.JwtTokenUtil;
import com.fuyin.boot.utils.Log4AddUtil;
import com.fuyin.boot.utils.RedisCache;
import com.fuyin.boot.utils.UserAgentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ClassName AdminServiceImpl
 * @Description TODO
 * @Author 何义祈安
 * @Date 2022/9/12 17:37
 * @Version 1.0
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRoleRelationMapper userRoleRelationMapper;
    @Autowired
    private UserAgentUtil userAgentUtil;
    @Autowired
    private LoginLogService loginLogService;
    @Autowired
    private SystemLogService systemLogService;

    private final Log4AddUtil logUtil = new Log4AddUtil();

    /**
    *
     * 通过手机号登录
    **/
    @Override
    public ResponseResult login(AdminLoginParam adminLoginParam, HttpServletRequest request) {
        String token = null;
        if(Objects.isNull(adminLoginParam.getPhoneNumber())){
            throw new RuntimeException("登录错误，管理员手机号不能为空");
        }
        //loadUserByPhoneNumber，返回UserDetails
        LoginAdmin loginAdmin = (LoginAdmin) loadUserByPhoneNumber(adminLoginParam.getPhoneNumber());
        if (Objects.isNull(loginAdmin)){
            throw new RuntimeException("登录错误，查询结果为空");
        }
        //获取请求头中的信息
        LoginLogDO loginLogDO = getLoginLogDO(loginAdmin, request);
        //对比查到的密码和输入的密码
        //TODO ！！！判断loginAdminn中的密码是加密形式的还是铭文形式的（登录接口能正常使用应该没问题吧）
        if (!passwordEncoder.matches(adminLoginParam.getPassword(), loginAdmin.getPassword())){
            loginLogDO.setLoginStatus("fail");
            loginLogService.insertLog(loginLogDO);
            throw new RuntimeException("登录错误，密码不正确");
        }
        //密码正确，更新最新登录时间
        AdminDO adminDOForLastLoginTime = loginAdmin.getAdminDO();
        int i = setAdminLastLoginTime(adminDOForLastLoginTime);
        //更新完后获取最新的用户数据
        AdminDO adminDO = new AdminDO();
        adminDO = adminMapper.selectById(adminDOForLastLoginTime.getId());
        LoginAdmin loginAdmin1 = (LoginAdmin) loadUserByPhoneNumber(adminDO.getPhoneNumber());

        // UserDetails封装成authentication对象
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(loginAdmin,null,loginAdmin.getAuthorities());
        //authentication对象存到SecurityContextHolder中
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //获取管理员id将用户信息存到redis中
        String adminId = loginAdmin.getAdminDO().getId().toString();
        //将管理员信息存入redis中，key: adminid:#{id}
        redisCache.setCacheObject("login:"+adminId,loginAdmin1);
        //TODO 看情况要不要更新管理员最新登录时间，简单调用个adminMapper就行
        //新增日志
        loginLogDO.setLoginStatus("success");
        loginLogService.insertLog(loginLogDO);

        //将UserDetails类型的用户信息、是否为管理员、用户id、用户最后登录时间生成token
//        String jwt = JwtUtil.createJWT(adminId);
        token = jwtTokenUtil.generateToken(
                loginAdmin,true,adminDO.getId(),adminDO.getLastLoginTime());

        //将管理员id,更新时间，jwt打包成map
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);

        //返回responseResul对象
        return new ResponseResult(200,"登录成功",map);
    }

    /**
    *@Description 获取登录日志对象
    *@Param [loginAdmin, request]
    *@return com.fuyin.boot.mgb.entity.LoginLogDO
    **/
    private LoginLogDO getLoginLogDO(LoginAdmin loginAdmin, HttpServletRequest request) {
        //获取请求头中的信息
        UserAgentInfo agentInfo = userAgentUtil.getUserAgentInfo(request);
        LoginLogDO loginLogDO = new LoginLogDO();
        loginLogDO.setUserId(loginAdmin.getAdminDO().getId());
        loginLogDO.setUsername(loginAdmin.getUsername());
        loginLogDO.setIp(agentInfo.getIp());
        loginLogDO.setBrowser(agentInfo.getBrowserType());
        loginLogDO.setOperatingSystem(agentInfo.getOs());
        return loginLogDO;
    }

    /**
    *
     * 返回id，用户名，手机号，角色，注册时间，更新时间
    **/
    @Override
    public ResponseResult<List<UserAndRoles>> getAllUser(HttpServletRequest request) {
        //从用户表中查找未删除的所有用户的id，昵称，手机号，注册时间，更新时间
        QueryWrapper<UserAndRoles> wrapper = new QueryWrapper<>();
        wrapper.select("u.id uid","u.username username","u.phone_number uphonenumber", "u.gmt_create ugmtcreate",
                "u.gmt_modified ugmtmodified","r.id rid","r.`name` rname","r.description rdescription");
        List<UserAndRoles> allUserAllRole = userMapper.getAllUserAllRole(wrapper);
        SystemLogDO systemLog = logUtil.userManageLog("查询");

        if (CollectionUtils.isEmpty(allUserAllRole)) {
            systemLog.setOperationStatus("fail");
            systemLogService.insertLog(systemLog);
            throw new RuntimeException("数据未找到");
        }
        //TODO 添加查找操作日志
        //操作状态 参数：success/fail
        systemLog.setOperationStatus("success");
        systemLogService.insertLog(systemLog);

        Map<String,Object> map = new HashMap<>();
        map.put("date",allUserAllRole);
        return new ResponseResult(200,"操作成功",map);
    }
    /**
     *
     * 分页查询，入参：查询哪一页页数
     **/
    @Override
    public ResponseResult getUserPage(UserSelectPage userSelectPage) {
        IPage<UserDO> page = new Page<>();
        page.setSize(userSelectPage.getSize());
        page.setCurrent(userSelectPage.getCurrent());
        IPage<UserDO> userPage = userMapper.selectPage(page, null);
        SystemLogDO systemLog = logUtil.userManageLog("查询");
        if(Objects.isNull(userPage)){
            systemLog.setOperationStatus("fail");
            systemLogService.insertLog(systemLog);
        }else{
            systemLog.setOperationStatus("success");
            systemLogService.insertLog(systemLog);
        }
        return new ResponseResult(200,"操作成功",userPage.getRecords());
    }

    /**
    *
     * 根据用户名、手机号查询用户
    **/
    @Override
    public ResponseResult getOneByUsernameAndPhoneNumber(UsernameAndPhoneNumberParam usernameAndPhoneNumber) {
        SystemLogDO systemLog = logUtil.userManageLog("查询");
        List<UserDO> users = new ArrayList<>();
        if(Objects.isNull(usernameAndPhoneNumber.getUserName()) && Objects.isNull(usernameAndPhoneNumber.getPhoneNumber())){
            throw new RuntimeException("查询失败，传入数据为空");
        }if(!Objects.isNull(usernameAndPhoneNumber.getUserName()) && Objects.isNull(usernameAndPhoneNumber.getPhoneNumber())){
            //有昵称，无手机
            users = getUser(users,usernameAndPhoneNumber,"name");
        }if(Objects.isNull(usernameAndPhoneNumber.getUserName()) && !Objects.isNull(usernameAndPhoneNumber.getPhoneNumber())){
            //有手机无昵称
            users = getUser(users,usernameAndPhoneNumber,"phone");
        }if (!Objects.isNull(usernameAndPhoneNumber.getUserName()) && !Objects.isNull(usernameAndPhoneNumber.getPhoneNumber())) {
            //两个都有
            users = getUser(users,usernameAndPhoneNumber,"namePhone");
        }
        if (CollectionUtils.isEmpty(users) || users.size() > 1 || Objects.isNull(users)) {
            systemLog.setOperationStatus("fail");
            systemLogService.insertLog(systemLog);
            throw new RuntimeException("查询失败");
        }
        systemLog.setOperationStatus("success");
        systemLogService.insertLog(systemLog);
        Map<String,Object> map = new HashMap<>();
        map.put("user",users.get(0));
        return new ResponseResult(200,"操作成功",map);
    }

    private List<UserDO> getUser(List<UserDO> users, UsernameAndPhoneNumberParam usernameAndPhoneNumber, String type) {
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        if("name".equals(type)) {
            wrapper.eq(UserDO::getUsername, usernameAndPhoneNumber.getUserName());
            return userMapper.selectList(wrapper);
        }if("phone".equals(type)){
            wrapper.eq(UserDO::getPhoneNumber,usernameAndPhoneNumber.getPhoneNumber());
            return userMapper.selectList(wrapper);
        }if("namePhone".equals(type)){
            wrapper.eq(UserDO::getPhoneNumber, usernameAndPhoneNumber.getPhoneNumber());
            return userMapper.selectList(wrapper);
        }
        return null;
    }

    /**
    *
     * 删除用户
    **/
    @Override
    public ResponseResult deleteUser(Long userId) {
        SystemLogDO systemLog = logUtil.userManageLog("删除");
        int i = userMapper.deleteById(userId);
        if(i != 1){
            systemLog.setOperationStatus("fail");
            systemLogService.insertLog(systemLog);
            throw new RuntimeException("删除失败");
        }
        systemLog.setOperationStatus("success");
        systemLogService.insertLog(systemLog);
        return new ResponseResult(200,"删除成功",i);
    }

    /**
    *
     * 刷新用户最后登录时间
    **/
    @Override
    public int setAdminLastLoginTime(AdminDO adminDO) {
        //获取管理员id
        AdminDO adminForLastLoginTime = new AdminDO();
        adminForLastLoginTime.setId(adminDO.getId());
        adminForLastLoginTime.setLastLoginTime(new Date());
        //设置更新条件，id
        UpdateWrapper<AdminDO> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",adminDO.getId());
        int update = adminMapper.update(adminForLastLoginTime, wrapper);
        return update;
    }
    /**
     *
     * 退出登录
     **/
    @Override
    public ResponseResult logout() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginAdmin loginAdmin = (LoginAdmin) authenticationToken.getPrincipal();
        if (Objects.isNull(loginAdmin)){
            throw new RuntimeException("找不到用户");
        }
        Long adminId = loginAdmin.getAdminDO().getId();
        redisCache.deleteObject("login:"+adminId);
        //查找redis中的用户查看是否存在
        if (!Objects.isNull(redisCache.getCacheObject("login:" + adminId))){
            throw new RuntimeException("删除失败");
        }
        Map<String,Object> map = new HashMap<>();
        map.put("退出用户id",loginAdmin.getAdminDO().getId());
        map.put("退出用户名称",loginAdmin.getUsername());
        return new ResponseResult(200,"退出成功",map);
    }

    @Override
    public ResponseResult insertOne(InsertUserParam insertUserParam) {
        SystemLogDO systemLog = logUtil.userManageLog("添加");
        //在用户表中插入新用户
        UserDO user = new UserDO();
        user.setUsername(insertUserParam.getUsername());
        user.setPhoneNumber(insertUserParam.getPhoneNumber());
        user.setPassword("00000000");
        try {
            if(userMapper.insert(user) != 1){
                throw new RuntimeException("插入数据失败");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("插入数据失败"+e);
        }
        //user_role_relation插入数据，用phone不用id
        String role = insertUserParam.getRole();
        //通过输入的角色查询id
        LambdaQueryWrapper<RoleDO> wrapperRole = new LambdaQueryWrapper<>();
        wrapperRole.eq(RoleDO::getName,Roles.fromString(role).getName());
        RoleDO roleDO = roleMapper.selectOne(wrapperRole);

        LambdaQueryWrapper<UserDO> wrapperUser = new LambdaQueryWrapper<>();
        wrapperUser.eq(UserDO::getUsername,insertUserParam.getUsername());
        UserDO userDO = userMapper.selectOne(wrapperUser);

        UserRoleRelationDO userRoleRelation = new UserRoleRelationDO();
        userRoleRelation.setRoleId(roleDO.getId());
        userRoleRelation.setUserId(userDO.getId());
        if(userRoleRelationMapper.insert(userRoleRelation) != 1){
            systemLog.setOperationStatus("fail");
            systemLogService.insertLog(systemLog);
            throw new RuntimeException("插入数据失败");
        }
        systemLog.setOperationStatus("success");
        systemLogService.insertLog(systemLog);
        return null;
    }

    /**
    *@Description 更新用户名称、手机号、角色信息，传入参数：id,新username,新phoneNumber,角色信息可能会改
     * 操作表：user + user_role_resource
     * TODO 数据操作要加上事务
    **/
    @Override
    public ResponseResult update(InsertUserParam insertUserParam) {
        SystemLogDO systemLog = logUtil.userManageLog("修改");
        UserDO userDO = new UserDO();
        //获取用户唯一标识
        Long id = insertUserParam.getId();
        userDO.setUsername(insertUserParam.getUsername());
        userDO.setPhoneNumber(insertUserParam.getPhoneNumber());
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserDO::getId,id);
        int update = 0;
        try {
            update = userMapper.update(userDO, wrapper);
        } catch (Exception e) {
            systemLog.setOperationStatus("fail");
            systemLogService.insertLog(systemLog);
            e.printStackTrace();
            throw new RuntimeException("更新失败：1.用户名重复 2.手机号重复 3.该用户被删除");
        }
        //通过传进来的role查询该角色id
        LambdaQueryWrapper<RoleDO> wrapperRole = new LambdaQueryWrapper<>();
        wrapperRole.eq(RoleDO::getDescription,insertUserParam.getRole());
        RoleDO roleDO = roleMapper.selectOne(wrapperRole);
        //删除原有的user_role中的数据
        if(Objects.isNull(roleDO)){
            systemLog.setOperationStatus("fail");
            systemLogService.insertLog(systemLog);
            throw new RuntimeException("查不到角色，说明数据库没有该角色，那么旧的不用删，新的不用加");
        }
        LambdaQueryWrapper<UserRoleRelationDO> wrapperDelete = new LambdaQueryWrapper<>();
        wrapperDelete.eq(UserRoleRelationDO::getUserId,id);
        userRoleRelationMapper.delete(wrapperDelete);
        //插入新的user_role
        UserRoleRelationDO userRole = new UserRoleRelationDO();
        userRole.setUserId(id);
        userRole.setRoleId(roleDO.getId());
        userRoleRelationMapper.insert(userRole);
        systemLog.setOperationStatus("success");
        systemLogService.insertLog(systemLog);
        return new ResponseResult(200,"操作成功");
    }

    public UserDetails loadUserByPhoneNumber(String phoneNumber) throws UsernameNotFoundException {
        //根据用户手机号查询用户信息
        LambdaQueryWrapper<AdminDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminDO::getPhoneNumber,phoneNumber);
        List<AdminDO> adminDOS = adminMapper.selectList(wrapper);
        //如果查询不到数据就通过抛出异常来给出提示
        if (CollectionUtils.isEmpty(adminDOS) || adminDOS.size() > 1) {
            throw new RuntimeException("数据未找到");
        }

        if(Objects.isNull(adminDOS.get(0))){
            throw new RuntimeException("用户名或密码错误");
        }
        AdminDO adminDO = adminDOS.get(0);
        //TODO 根据用户查询权限信息 添加到LoginUser中, 用ResourcesMapper
        List<String> list = resourceMapper.selectNameByAdminPrimaryKey(adminDO.getId());
        return new LoginAdmin(adminDO,list);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<AdminDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminDO::getUsername,username);
        List<AdminDO> adminDOS = adminMapper.selectList(wrapper);
        //如果查询不到数据就通过抛出异常来给出提示
        if (CollectionUtils.isEmpty(adminDOS) || adminDOS.size() > 1) {
            throw new RuntimeException("数据未找到");
        }

        if(Objects.isNull(adminDOS.get(0))){
            throw new RuntimeException("用户名或密码错误");
        }
        AdminDO adminDO = adminDOS.get(0);
        //TODO 根据用户查询权限信息 添加到LoginUser中, 用ResourcesMapper
        List<String> list = resourceMapper.selectNameByAdminPrimaryKey(adminDO.getId());
        return new LoginAdmin(adminDO,list);
    }
}

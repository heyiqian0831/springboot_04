package com.fuyin.boot.filter;


import com.fuyin.boot.domain.LoginAdmin;
import com.fuyin.boot.domain.LoginUser;
import com.fuyin.boot.service.AdminService;
import com.fuyin.boot.service.LoginService;
import com.fuyin.boot.service.impl.UserDetailsServiceImpl;
import com.fuyin.boot.utils.JwtTokenUtil;
import com.fuyin.boot.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Objects;

/**
 * @ClassName JwtAuthenticationTokenFilter
 * @Description
 * @Author 何义祈安
 * @Date 2022/8/16 20:41
 * @Version 1.0
 */
//OncePerRequestFilter类每个请求只经过过滤器一次
@Component //类定义完要注入到spring容器中
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取Token
        String token = request.getHeader("token");
        //不一定所有请求都会有Token，所以要进行判断
        if(!StringUtils.hasText(token)){
            //放行
            filterChain.doFilter(request,response);
            //一定要return是因为doFilter放行后会去访问后面的filter,拦截器,目标资源再回到这里，如果不return从过滤链回来后会继续后面的token相关操作代码
            return;
        }
        //2. TODO 获取token负载中的userid  getUserIDFromToken,没有就非法
                //String userName = jwtTokenUtil.get
        Long userId = jwtTokenUtil.getUserIdFromToken(token);
        LOGGER.info("checking : id={}", userId);
        if(Objects.isNull(userId)){
            throw new RuntimeException("token非法");
        }
        //3. TODO 获取登录的用户是否为管理员
        Boolean isAdmin = jwtTokenUtil.getIsAdminFromToken(token);
        UserDetails userDetails = null;

        //4. TODO 通过id找数据库中的管理员信息
        String redisKey = "login:" + userId;
        userDetails = redisCache.getCacheObject(redisKey);
        if(Objects.isNull(userDetails)){
            throw new RuntimeException("用户未登录");
        }
        LOGGER.info("checking : username={}, isAdmin={}", userDetails.getUsername(), isAdmin);
        //5. TODO 判断token是否合法
        if(jwtTokenUtil.validateToken(token,userDetails)){
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, request, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }else {
            LOGGER.info("jwt validate : token={}", token);
        }
        //解析Token,拿到Token中的userId，拿不到token里的对象表示token非法
//        String userid;
//        try {
//            Claims claims = JwtUtil.parseJWT(token);
//            userid = claims.getSubject();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("token非法");
//        }
        //用userId从redis中获取缓存中的用户信息
//        String redisKey = "login:" + userid;
//        //如果是管理员？就返回给LoginAdmin?
//        LoginUser loginUser = redisCache.getCacheObject(redisKey);
//        LoginAdmin loginAdmin = null;
//        if(Objects.isNull(loginUser)){
//            throw new RuntimeException("用户未登录");
//        }
        //存入SecurityContextHolder，因为后面的filter都会从SecurityContextHolder中获取用户信息和认证信息
        //TODO 获取权限信息并封装到authorities中
        //用三个参数的方法，方法里会setAuthenticated传入true，代表已经认证
        //已经认证的状态下redis中查到的用户信息可以放到主体参数（principal）上，还没认证(principal)放用户名，(redentials)放用户密码
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());//authorities:权限信息集合
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request,response);
    }
}

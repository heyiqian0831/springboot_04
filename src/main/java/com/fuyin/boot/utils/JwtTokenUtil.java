package com.fuyin.boot.utils;

//import com.auth0.jwt.JWT;
//import com.fuyin.wealthms.model.dto.AdminUserDetails;
//import com.fuyin.wealthms.model.dto.CommonUserDetails;
import com.fuyin.boot.domain.LoginAdmin;
import com.fuyin.boot.domain.LoginUser;
import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtToken生成的工具类
 *
 * @author 何义祈安
 * @date 2021/10/21 16:52
 */
@Component
public class JwtTokenUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    //设置公用静态对象
    /**
    *@Description 是否为管理员
    **/
    private static final String CLAIM_KEY_IS_ADMIN = "isAdmin";
    /**
     *@Description 用户名
     **/
    private static final String CLAIM_KEY_USERNAME = "sub";
    /**
     *@Description userId
     **/
    private static final String CLAIM_KEY_USERID = "userId";
    /**
     *@Description 创建时间
     **/
    private static final String CLAIM_KEY_CREATED = "created";

    /**
    * 加密密钥
    **/
    @Value("${jwt.secret}")
    private String secret;
    /**
     * 过期时间
     **/
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 负责生成JWT的token
     * 传进参数：Map<String, Object> claims，map集合的payload
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                //设置Payload
                .setClaims(claims)
                //设置过期时间
                .setExpiration(generateExpirationDate())
                //设置签名，签名算法，密钥
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 验证成功后根据用户信息与用户身份生成token
     *
     * @param userDetails 用户信息
     * @param isAdmin     是否为管理员
     * @param userId 用户id
     * @param created 用户最新登录时间
     * @return token
     */
    public String generateToken(UserDetails userDetails, Boolean isAdmin, Long userId, Date created) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_IS_ADMIN, isAdmin);
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_USERID, userId);
        claims.put(CLAIM_KEY_CREATED, created);
        return generateToken(claims);
    }

    /**
     * 从token中获取JWT中的负载payload(存入的用户信息)
     *
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            //parser()：解析token
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            LOGGER.info("JWT格式验证失败:{}", token);
        }
        return claims;
    }

    /**
     * 生成token的过期时间
     *
     * @return
     */
    private Date generateExpirationDate() {
        //currentTimeMillis()：以毫秒为单位返回当前时间
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从token中获取登录用户名
     *
     * @param token
     * @return     ???
     */
    public String getUserNameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
//            username = claims.get(CLAIM_KEY_USERNAME,String.class);
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 从token中获取用户是否为管理员
     *
     * @param token
     * @return
     */
    public Boolean getIsAdminFromToken(String token) {
        Boolean isAdmin;
        try {
            Claims claims = getClaimsFromToken(token);
            isAdmin = claims.get(CLAIM_KEY_IS_ADMIN, Boolean.class);
        } catch (Exception e) {
            isAdmin = null;
        }
        return isAdmin;
    }

    /**
     * 从token中获取userId
     *
     * @param token
     * @return
     */
    public Long getUserIdFromToken(String token) {
        Long userId;
        try {
            Claims claims = getClaimsFromToken(token);
            userId = claims.get(CLAIM_KEY_USERID, Long.class);
        } catch (Exception e) {
            userId = null;
        }
        return userId;
    }

    /**
     * 从token中获取create
     *
     * @param token
     * @return
     */
    public Date getCreatedFromToken(String token) {
        Date created;
        try {
            Claims claims = getClaimsFromToken(token);
            created = claims.get(CLAIM_KEY_CREATED, Date.class);
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    /**
     * 验证token是否还有效
     *
     * @param token       客户端传入的token
     * @param userDetails 从数据库中查询出来的用户信息
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUserNameFromToken(token);
        Date lastLoginTime = null;
        //判断输入的userDetails是否等于右边的类
        if (userDetails instanceof LoginAdmin) {
            lastLoginTime = ((LoginAdmin) userDetails).getAdminDO().getGmtModified();
        } else if (userDetails instanceof LoginUser) {
            lastLoginTime = ((LoginUser) userDetails).getUserDO().getGmtModified();
        }
        //判断token里的用户名是否等于传入对象里的用户名，
        //条件：token没有失效，并且输入的用户对象的最后更新时间等于token中的最后更新时间
        String redisUsername = userDetails.getUsername();
        boolean a = username.equals(userDetails.getUsername());
        boolean b = !isTokenExpired(token);
        boolean c = getCreatedFromToken(token).equals(lastLoginTime);
        Date createdFromToken1 = getCreatedFromToken(token);
        boolean tokenExpired = isTokenExpired(token);
        Date createdFromToken = getCreatedFromToken(token);
        return username.equals(userDetails.getUsername())
                               && !isTokenExpired(token)
                               && getCreatedFromToken(token).equals(lastLoginTime);
    }

    /**
     * 判断token是否已经失效
     *
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        //从token中获取过期时间
        Date expiredDate = getExpiredDateFromToken(token);
        boolean before = expiredDate.before(new Date());
        //测试此日期是否早于当下日期
        return expiredDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     *
     * @param token
     * @return
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }


    /**
     * 根据用户信息与用户身份生成token
     *
     * @param userDetails 用户信息
     * @param isAdmin     是否为管理员
     * @return token
     */
    public String generateToken2(UserDetails userDetails, Boolean isAdmin, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_IS_ADMIN, isAdmin);
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_USERID, userId);
//        claims.put(CLAIM_KEY_CREATED, created);
        return generateToken(claims);
    }


    /**
     * 判断token是否可以被刷新
     *
     * @param token
     * @return
     */
    public boolean canRefresh(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 刷新token
     *
     * @param token
     * @return
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }
}

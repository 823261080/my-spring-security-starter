package com.jwzhang.starter.utils;

import com.alibaba.fastjson.JSON;
import com.jwzhang.starter.enums.UserTypeEnum;
import com.jwzhang.starter.config.JwtConfig;
import com.jwzhang.starter.constant.Constants;
import com.jwzhang.starter.security.service.User2DetailsService;
import com.jwzhang.starter.security.service.User1DetailsService;
import com.jwzhang.starter.security.userdetails.CustomUserDetails;
import com.jwzhang.starter.utils.ip.IpUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * JWT生成Token工具类
 *
 * @author zjw
 * @since 2022/8/17
 */
@Slf4j
@Component
public class JwtTokenUtils {

    /**
     * 时间格式化
     */
    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private User1DetailsService user1DetailsService;

    @Autowired
    private User2DetailsService user2DetailsService;

    private static JwtTokenUtils jwtTokenUtils;

    @PostConstruct
    public void init() {
        jwtTokenUtils = this;
        jwtTokenUtils.user1DetailsService = this.user1DetailsService;
    }

    /**
     * 创建Token
     *
     * @param userDetails 用户信息
     * @return token
     */
    public static String createAccessToken(CustomUserDetails userDetails) {
        // 设置JWT
        String token = Jwts.builder()
                // 用户Id
                .setId(userDetails.getUserId().toString())
                // 主题
                .setSubject(userDetails.getUsername())
                // 签发时间
                .setIssuedAt(new Date())
                // 签发者
                .setIssuer("jwzhang")
                // 过期时间
                .setExpiration(new Date(System.currentTimeMillis() + JwtConfig.expiration))
                // 签名算法、密钥
                .signWith(SignatureAlgorithm.HS512, JwtConfig.secret)
                // 自定义其他属性，如用户组织机构ID，用户所拥有的角色，用户权限信息等
                .claim("authorities", JSON.toJSONString(userDetails.getAuthorities()))
                .claim("userType", userDetails.getUserType().getCode())
                .claim("ip", IpUtils.getIpAddr(ServletUtils.getRequest()))
                .compact();
        return JwtConfig.tokenPrefix + token;
    }

    /**
     * 刷新Token
     *
     * @param oldToken 过期但未超过刷新时间的Token
     * @return token
     */
    public static String refreshAccessToken(String oldToken) {
        String username = JwtTokenUtils.getUserNameByToken(oldToken);
        Integer userType = JwtTokenUtils.getUserTypeByToken(oldToken);
        UserTypeEnum userTypeEnum = UserTypeEnum.getByCode(userType);
        if(userTypeEnum == null){
            return null;
        }
        CustomUserDetails userDetails = null;
        switch (userTypeEnum){
            case USER1:{
                userDetails = jwtTokenUtils.user1DetailsService
                        .loadUserByUsername(username);
                break;
            }
            case USER2:{
                userDetails = jwtTokenUtils.user2DetailsService
                        .loadUserByUsername(username);
                break;
            }
            default:
                return null;
        }
        userDetails.setIp(JwtTokenUtils.getIpByToken(oldToken));
        return createAccessToken(userDetails);
    }

    /**
     * 解析Token
     *
     * @param token Token信息
     * @return {@link CustomUserDetails}
     */
    public static CustomUserDetails parseAccessToken(String token) {
        CustomUserDetails userDetails = null;
        if (StringUtils.isNotEmpty(token)) {
            try {
                // 去除JWT前缀
                token = token.substring(JwtConfig.tokenPrefix.length());

                // 解析Token
                Claims claims = Jwts.parser().setSigningKey(JwtConfig.secret).parseClaimsJws(token).getBody();
                Integer userType = Integer.parseInt(claims.get("userType").toString());
                String username = claims.getSubject();
                UserTypeEnum userTypeEnum = UserTypeEnum.getByCode(userType);
                if(userTypeEnum == null){
                    return null;
                }
                switch (userTypeEnum){
                    case USER1:{
                        userDetails = jwtTokenUtils.user1DetailsService
                                .loadUserByUsername(username);
                        break;
                    }
                    case USER2:{
                        userDetails = jwtTokenUtils.user2DetailsService
                                .loadUserByUsername(username);
                        break;
                    }
                    default:
                        return null;
                }

                // 获取IP
                String ip = claims.get("ip").toString();
                userDetails.setIp(ip);
            } catch (Exception e) {
                log.error("解析Token异常：" + e);
            }
        }
        return userDetails;
    }

    /**
     * 保存Token信息到Redis中
     *
     * @param token    Token信息
     * @param username 用户名
     * @param ip       IP
     */
    public static void setTokenInfo(String token, String username, String ip) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = Constants.REDIS_TOKEN_PREFIX + token.substring(JwtConfig.tokenPrefix.length());

            Integer refreshTime = JwtConfig.refreshTime;
            LocalDateTime localDateTime = LocalDateTime.now();

            RedisCache.hset(token, "username", username, refreshTime);
            RedisCache.hset(token, "ip", ip, refreshTime);
            RedisCache.hset(token, "refreshTime",
                    df.format(localDateTime.plus(JwtConfig.refreshTime, ChronoUnit.MILLIS)), refreshTime);
            RedisCache.hset(token, "expiration", df.format(localDateTime.plus(JwtConfig.expiration, ChronoUnit.MILLIS)),
                    refreshTime);
        }
    }

    /**
     * 将Token放到黑名单中
     *
     * @param token Token信息
     */
    public static void addBlackList(String token) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = Constants.REDIS_BLACKLIST_PREFIX + token.substring(JwtConfig.tokenPrefix.length());
            RedisCache.set(token, df.format(LocalDateTime.now()), Constants.TOKEN_BLACKLIST_TIME);
        }
    }

    /**
     * Redis中删除Token
     *
     * @param token Token信息
     */
    public static void deleteRedisToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = Constants.REDIS_TOKEN_PREFIX + token.substring(JwtConfig.tokenPrefix.length());
            RedisCache.deleteKey(token);
        }
    }

    /**
     * 判断当前Token是否在黑名单中
     *
     * @param token Token信息
     */
    public static boolean isBlackList(String token) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = Constants.REDIS_BLACKLIST_PREFIX + token.substring(JwtConfig.tokenPrefix.length());
            return RedisCache.hasKey(token);
        }
        return false;
    }

    /**
     * 是否过期
     *
     * @param expiration 过期时间，字符串
     * @return 过期返回True，未过期返回false
     */
    public static boolean isExpiration(String expiration) {
        LocalDateTime expirationTime = LocalDateTime.parse(expiration, df);
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.compareTo(expirationTime) > 0;
    }

    /**
     * 是否有效
     *
     * @param refreshTime 刷新时间，字符串
     * @return 有效返回True，无效返回false
     */
    public static boolean isValid(String refreshTime) {
        LocalDateTime validTime = LocalDateTime.parse(refreshTime, df);
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.compareTo(validTime) <= 0;
    }

    /**
     * 检查Redis中是否存在Token
     *
     * @param token Token信息
     * @return bool
     */
    public static boolean hasToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = Constants.REDIS_TOKEN_PREFIX + token.substring(JwtConfig.tokenPrefix.length());
            return RedisCache.hasKey(token);
        }
        return false;
    }

    /**
     * 从Redis中获取过期时间
     *
     * @param token Token信息
     * @return 过期时间，字符串
     */
    public static String getExpirationByToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = Constants.REDIS_TOKEN_PREFIX + token.substring(JwtConfig.tokenPrefix.length());
            return RedisCache.hget(token, "expiration").toString();
        }
        return null;
    }

    /**
     * 从Redis中获取刷新时间
     *
     * @param token Token信息
     * @return 刷新时间，字符串
     */
    public static String getRefreshTimeByToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = Constants.REDIS_TOKEN_PREFIX + token.substring(JwtConfig.tokenPrefix.length());
            return RedisCache.hget(token, "refreshTime").toString();
        }
        return null;
    }

    /**
     * 从Redis中获取用户名
     *
     * @param token Token信息
     * @return username
     */
    public static String getUserNameByToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = Constants.REDIS_TOKEN_PREFIX + token.substring(JwtConfig.tokenPrefix.length());
            Object username = RedisCache.hget(token, "username");
            if(username != null){
                return username.toString();
            }
        }
        return null;
    }

    /**
     * 从Redis中获取用户名
     *
     * @param token Token信息
     * @return username
     */
    public static Integer getUserTypeByToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = Constants.REDIS_TOKEN_PREFIX + token.substring(JwtConfig.tokenPrefix.length());
            Object userType = RedisCache.hget(token, "userType");
            if(userType != null){
                return Integer.parseInt(userType.toString());
            }
        }
        return null;
    }

    /**
     * 从Redis中获取IP
     *
     * @param token Token信息
     * @return
     */
    public static String getIpByToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            // 去除JWT前缀
            token = Constants.REDIS_TOKEN_PREFIX + token.substring(JwtConfig.tokenPrefix.length());
            return RedisCache.hget(token, "ip").toString();
        }
        return null;
    }

}

package com.jwzhang.starter.utils;

import com.jwzhang.starter.enums.UserTypeEnum;
import com.jwzhang.starter.constant.HttpStatus;
import com.jwzhang.starter.exception.CustomException;
import com.jwzhang.starter.security.userdetails.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 安全服务工具类
 *
 * @author zjw
 * @since 2022/8/18
 */
public class SecurityUtils {

    /**
     * 获取用户账户
     **/
    public static String getUsername() {
        try {
            return getUserDetails().getUsername();
        } catch (Exception e) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "获取用户账户异常");
        }
    }

    /**
     * 获取用户账户
     **/
    public static Long getUserId() {
        try {
            return getUserDetails().getUserId();
        } catch (Exception e) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "获取用户账户id异常");
        }
    }

    public static UserTypeEnum getUserType(){
        try {
            return getUserDetails().getUserType();
        } catch (Exception e) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "获取用户类型异常");
        }
    }

    /**
     * 获取用户
     **/
    public static CustomUserDetails getUserDetails() {
        try {

            return (CustomUserDetails) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "获取用户信息异常");
        }
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 生成BCryptPasswordEncoder密码
     *
     * @param password 密码
     * @return 加密字符串
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 判断密码是否相同
     *
     * @param rawPassword     真实密码
     * @param encodedPassword 加密后字符
     * @return 结果
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }
}

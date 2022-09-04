package com.jwzhang.starter.security.userdetails;

import com.jwzhang.starter.enums.UserTypeEnum;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 自定义UserDetail接口
 */
public interface CustomUserDetails extends UserDetails {
    /**
     * 获取用户状态
     * @return 用户状态
     */
    Integer getStatus();

    /**
     * 获取用户id
     * @return 用户id
     */
    Long getUserId();

    /**
     * 获取用户类型
     * @return {@link UserTypeEnum}
     */
    UserTypeEnum getUserType();

    /**
     * 获取账号
     * @return 结果
     */
    String getUsername();

    /**
     * 设置ip
     * @param ip ip
     */
    void setIp(String ip);

    /**
     * 设置用户id
     * @param userId 用户id
     */
    void setUserId(Long userId);

    /**
     * 设置用户账号
     * @param username 用户账号
     */
    void setUsername(String username);

    /**
     * 获取ip地址
     * @return 结果
     */
    String getIp();
}

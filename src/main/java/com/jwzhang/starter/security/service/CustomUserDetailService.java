package com.jwzhang.starter.security.service;

import com.jwzhang.starter.enums.UserTypeEnum;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 自定义UserDetailService接口
 */
public interface CustomUserDetailService extends UserDetailsService {

    /**
     * 获取用户类型
     * @return {@link UserTypeEnum}
     */
    UserTypeEnum getUserType();
}

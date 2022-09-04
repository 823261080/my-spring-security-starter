package com.jwzhang.starter.security.handler;

import com.jwzhang.starter.enums.StatusEnum;
import com.jwzhang.starter.enums.UserTypeEnum;
import com.jwzhang.starter.exception.CustomException;
import com.jwzhang.starter.security.service.CustomUserDetailService;
import com.jwzhang.starter.security.userdetails.CustomUserDetails;
import com.jwzhang.starter.utils.SecurityUtils;
import com.jwzhang.starter.utils.ServletUtils;
import com.jwzhang.starter.constant.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 用户登录验证处理类
 *
 * @author zjw
 * @since 2022/8/18
 */
@Slf4j
@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    private List<CustomUserDetailService> userDetailServices;

    /**
     * 身份认证
     *
     * @param authentication {@link Authentication}
     * @return {@link Authentication}
     * @throws AuthenticationException 身份认证异常
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String requestPath = ServletUtils.getRequestPath();
        UserTypeEnum userTypeEnum = UserTypeEnum.getByPath(requestPath);
        if(userTypeEnum == null){
            log.info("身份认证时校验请求路径错误:requestPath={}",requestPath);
            throw new CustomException(HttpStatus.NOT_FOUND, "请求路径不正确");
        }
        // 获取用户名
        String username = (String) authentication.getPrincipal();
        // 获取密码
        String password = (String) authentication.getCredentials();
        CustomUserDetails userDetails = null;
        for (CustomUserDetailService userDetailService : userDetailServices) {
            if(Objects.equals(userDetailService.getUserType(),userTypeEnum)){
                userDetails = (CustomUserDetails)userDetailService.loadUserByUsername(username);
                break;
            }
        }

        if (userDetails == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        String s = SecurityUtils.encryptPassword(password);
        log.info("加密密码：{}，加密后：{}",password,s);
        if (!SecurityUtils.matchesPassword(password, userDetails.getPassword())) {
            throw new BadCredentialsException("用户名或密码错误");
        }
        if (Objects.equals(userDetails.getStatus(), StatusEnum.STOP.getCode())) {
            throw new LockedException("用户已停用");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    /**
     * 支持指定的身份验证
     */
    @Override
    public boolean supports(Class<?> authentication) {
        log.info("authentication:{}",authentication);
        return true;
    }

    public void setUserDetailServices(List<CustomUserDetailService> userDetailServices) {
        this.userDetailServices = userDetailServices;
    }
}

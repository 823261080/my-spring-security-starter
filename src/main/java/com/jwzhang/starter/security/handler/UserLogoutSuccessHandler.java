package com.jwzhang.starter.security.handler;

import com.jwzhang.starter.config.JwtConfig;
import com.jwzhang.starter.utils.JwtTokenUtils;
import com.jwzhang.starter.vo.Ajax;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登出成功处理类
 *
 * @author zjw
 * @since 2022/8/17
 */
@Component
public class UserLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) {
        // 添加到黑名单
        String token = request.getHeader(JwtConfig.tokenHeader);
        JwtTokenUtils.addBlackList(token);

        SecurityContextHolder.clearContext();
        Ajax.responseJson(response, Ajax.success("登出成功"));
    }
}

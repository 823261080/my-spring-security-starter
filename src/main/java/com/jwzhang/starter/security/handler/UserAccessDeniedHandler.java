package com.jwzhang.starter.security.handler;

import com.jwzhang.starter.vo.Ajax;
import com.jwzhang.starter.constant.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 无权限处理类
 *
 * @author zjw
 * @since 2022/8/17
 */
@Component
public class UserAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        Ajax.responseJson(response, Ajax.error(HttpStatus.FORBIDDEN, "拒绝访问", accessDeniedException.getMessage()));
    }

}
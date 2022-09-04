package com.jwzhang.starter.security.filter;

import com.jwzhang.starter.config.JwtConfig;
import com.jwzhang.starter.security.userdetails.CustomUserDetails;
import com.jwzhang.starter.utils.JwtTokenUtils;
import com.jwzhang.starter.utils.ip.IpUtils;
import com.jwzhang.starter.vo.Ajax;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT权限过滤器，用于验证Token是否合法
 *
 * @author zjw
 * @since 2022/8/18
 */
@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        // 取出Token
        String token = request.getHeader(JwtConfig.tokenHeader);
        if (token != null && token.startsWith(JwtConfig.tokenPrefix)) {
            // 是否在黑名单中
            if (JwtTokenUtils.isBlackList(token)) {
                Ajax.responseJson(response, Ajax.error(505, "Token已失效", "Token已进入黑名单"));
                return;
            }

            // 是否存在于Redis中
            if (JwtTokenUtils.hasToken(token)) {
                String ip = IpUtils.getIpAddr(request);
                String expiration = JwtTokenUtils.getExpirationByToken(token);
                String username = JwtTokenUtils.getUserNameByToken(token);

                // 判断是否过期
                if (JwtTokenUtils.isExpiration(expiration)) {
                    // 加入黑名单
                    JwtTokenUtils.addBlackList(token);
                    // 是否在刷新期内
                    String validTime = JwtTokenUtils.getRefreshTimeByToken(token);
                    if (JwtTokenUtils.isValid(validTime)) {
                        // 刷新Token，重新存入请求头
                        String newToke = JwtTokenUtils.refreshAccessToken(token);

                        // 删除旧的Token，并保存新的Token
                        JwtTokenUtils.deleteRedisToken(token);
                        JwtTokenUtils.setTokenInfo(newToke, username, ip);
                        response.setHeader(JwtConfig.tokenHeader, newToke);

                        log.info("用户{}的Token已过期，但为超过刷新时间，已刷新", username);

                        token = newToke;
                    } else {
                        log.info("用户{}的Token已过期且超过刷新时间，不予刷新", username);

                        // 加入黑名单
                        JwtTokenUtils.addBlackList(token);
                        Ajax.responseJson(response, Ajax.error(505, "Token已过期", "已超过刷新有效期"));
                        return;
                    }
                }

                CustomUserDetails userDetails = JwtTokenUtils.parseAccessToken(token);
                if (userDetails != null) {
                    // 校验IP
                    if (!StringUtils.equals(ip, userDetails.getIp())) {
                        log.info("用户{}请求IP与Token中IP信息不一致", username);

                        // 加入黑名单
                        JwtTokenUtils.addBlackList(token);
                        Ajax.responseJson(response, Ajax.error(505, "Token已过期", "可能存在IP伪造风险"));
                        return;
                    }

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, userDetails.getUserId(), userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}

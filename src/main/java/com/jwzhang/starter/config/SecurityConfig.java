package com.jwzhang.starter.config;

import com.jwzhang.starter.security.filter.JwtAuthenticationFilter;
import com.jwzhang.starter.security.handler.*;
import com.jwzhang.starter.security.service.User1DetailsService;
import com.jwzhang.starter.security.service.CustomUserDetailService;
import com.jwzhang.starter.security.service.User2DetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统安全核心配置
 *
 * @author zjw
 * @since 2022/8/18
 */
@Configuration
@EnableWebSecurity
// 开启方法权限注解
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 无权限处理类
     */
    @Autowired
    private UserAccessDeniedHandler userAccessDeniedHandler;

    /**
     * 用户未登录处理类
     */
    @Autowired
    private UserNotLoginHandler userNotLoginHandler;

    /**
     * 用户登出成功处理类
     */
    @Autowired
    private UserLogoutSuccessHandler userLogoutSuccessHandler;

    /**
     * 用户登录验证
     */
    @Autowired
    private UserAuthenticationProvider userAuthenticationProvider;

    /**
     * 用户权限注解
     */
    @Autowired
    private UserPermissionEvaluator userPermissionEvaluator;

    /**
     * 用户1详情服务类
     */
    @Autowired
    private User1DetailsService user1DetailsService;

    /**
     * 用户2详情服务类
     */
    @Autowired
    private User2DetailsService user2DetailsService;

    /**
     * 解决 无法直接注入 AuthenticationManager
     *
     * @return {@link AuthenticationManager}
     * @throws Exception Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 加密方式
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 注入自定义PermissionEvaluator
     */
    @Bean
    public DefaultWebSecurityExpressionHandler userSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(userPermissionEvaluator);
        return handler;
    }

    /**
     * 多用户登录验证
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        List<CustomUserDetailService> userDetailServices = new ArrayList<>();
        userDetailServices.add(user1DetailsService);
        userDetailServices.add(user2DetailsService);
        userAuthenticationProvider.setUserDetailServices(userDetailServices);
        auth.authenticationProvider(userAuthenticationProvider);
    }

    /**
     * 安全权限配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // CSRF禁用，因为不使用session
                .csrf().disable()
                // 认证失败处理类
                .exceptionHandling()
                .authenticationEntryPoint(userNotLoginHandler)
                .and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 过滤请求
                .authorizeRequests()
                .antMatchers(JwtConfig.antMatchers).permitAll()
                // 其他的需要登陆后才能访问
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable()
                // 开启跨域
                .and().cors()
                // 禁用跨站请求伪造防护
                .and().csrf().disable();
        http.logout().logoutUrl("/logout/submit")
                .logoutSuccessHandler(userLogoutSuccessHandler)
                // 配置没有权限处理类
                .and().exceptionHandling().accessDeniedHandler(userAccessDeniedHandler);
        // 添加JWT过滤器
        http.addFilter(new JwtAuthenticationFilter(authenticationManager()));
        // 禁用缓存
        http.headers().cacheControl();

    }
}

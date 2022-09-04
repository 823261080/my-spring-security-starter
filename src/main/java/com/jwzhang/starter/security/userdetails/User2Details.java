package com.jwzhang.starter.security.userdetails;

import com.jwzhang.starter.entity.SysUser2;
import com.jwzhang.starter.enums.UserTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

/**
 * EducationUserDetails
 *
 * @author zjw
 * @since 2022/8/17
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class User2Details extends SysUser2 implements CustomUserDetails, Serializable {



    private static final long serialVersionUID = 1L;

    /**
     * 用户角色
     */
    private Collection<GrantedAuthority> authorities;

    /**
     * 账号是否过期
     */
    private boolean isAccountNonExpired = false;

    /**
     * 账号是否锁定
     */
    private boolean isAccountNonLocked = false;

    /**
     * 证书是否过期
     */
    private boolean isCredentialsNonExpired = false;

    /**
     * 账号是否有效
     */
    private boolean isEnabled = true;

    /**
     * 客户请求Ip
     */
    private String ip;

    /**
     * 获得用户权限
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * 判断账号是否过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    /**
     * 判断账号是否锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    /**
     * 判断证书是否过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    /**
     * 判断账号是否有效
     */
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * 获取用户类型
     *
     * @return {@link UserTypeEnum}
     */
    @Override
    public UserTypeEnum getUserType() {
        return UserTypeEnum.USER2;
    }

}

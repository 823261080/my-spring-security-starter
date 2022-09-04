package com.jwzhang.starter.security.service;

import com.jwzhang.starter.entity.SysRole;
import com.jwzhang.starter.entity.SysUser2;
import com.jwzhang.starter.enums.UserTypeEnum;
import com.jwzhang.starter.security.userdetails.User2Details;
import com.jwzhang.starter.service.ISysRoleService;
import com.jwzhang.starter.service.ISysUser2Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户2登录Service
 *
 * @author zjw
 * @since 2022/8/18
 */
@Service
public class User2DetailsService implements CustomUserDetailService {

    @Autowired
    private ISysUser2Service user2Service;

    @Autowired
    private ISysRoleService roleService;

    /**
     * 根据用户名查用户信息
     * @param username 用户名
     * @return {@link UserDetails}
     * @throws UsernameNotFoundException 没有查询到用户信息异常
     */
    @Override
    public User2Details loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser2 userEducator = user2Service.findUserByUserName(username);
        if(userEducator != null){
            User2Details userDetails = new User2Details();
            BeanUtils.copyProperties(userEducator, userDetails);
            // 角色集合
            Set<GrantedAuthority> authorities = new HashSet<>();
            List<SysRole> roleList = roleService.findRoleByUserIdAndType(userDetails.getUserId(), this.getUserType());
            if(!CollectionUtils.isEmpty(roleList)){
                roleList.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleKey())));
            }
            userDetails.setAuthorities(authorities);
            return userDetails;
        }
        return null;
    }

    @Override
    public UserTypeEnum getUserType() {
        return UserTypeEnum.USER2;
    }
}

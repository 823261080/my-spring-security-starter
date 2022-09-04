package com.jwzhang.starter.security.handler;

import com.jwzhang.starter.entity.SysMenu;
import com.jwzhang.starter.enums.UserTypeEnum;
import com.jwzhang.starter.security.userdetails.User1Details;
import com.jwzhang.starter.utils.SecurityUtils;
import com.jwzhang.starter.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户权限注解处理类
 *
 * @author zjw
 * @since 2022/8/18
 */
@Component
public class UserPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private ISysMenuService menuCameraService;

    /**
     * 判断是否拥有权限
     *
     * @param authentication 用户身份
     * @param targetUrl      目标路径
     * @param permission     路径权限
     *
     * @return 是否拥有权限
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetUrl, Object permission) {
        // 获取用户名
        User1Details userDetails = (User1Details) authentication.getPrincipal();
        // 用户权限
        Set<String> permissions = new HashSet<>();
        UserTypeEnum userType = SecurityUtils.getUserType();
        List<SysMenu> menuList = menuCameraService.findMenuByUserIdAndType(userDetails.getUserId(),userType);
        menuList.forEach(menu -> permissions.add(menu.getPerms()));
        // 判断是否拥有权限
        return permissions.contains(permission.toString());
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}

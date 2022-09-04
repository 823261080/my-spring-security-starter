package com.jwzhang.starter.service;

import com.jwzhang.starter.entity.SysMenu;
import com.jwzhang.starter.enums.UserTypeEnum;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author zjw
 * @since 2022-08-17
 */
public interface ISysMenuService extends IService<SysMenu> {
    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return {@link List< SysMenu >}
     */
    List<SysMenu> findMenuByUserIdAndType(Long userId, UserTypeEnum userType);

    /**
     * 获取菜单树
     * @param userId 用户id
     * @param userTypeEnum 用户类型枚举
     * @return 结果
     */
    List<SysMenu> getMenuTree(Long userId, UserTypeEnum userTypeEnum);

    /**
     * 添加菜单
     * @param menuCamera 菜单信息
     * @return 菜单id
     */
    Long addMenu(SysMenu menuCamera);
}

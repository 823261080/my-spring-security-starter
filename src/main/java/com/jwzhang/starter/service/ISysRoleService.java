package com.jwzhang.starter.service;

import com.jwzhang.starter.entity.SysRole;
import com.jwzhang.starter.enums.UserTypeEnum;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author zjw
 * @since 2022-08-17
 */
public interface ISysRoleService extends IService<SysRole> {
    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return {@link List< SysRole >}
     */
    List<SysRole> findRoleByUserIdAndType(Long userId, UserTypeEnum userTypeEnum);
}

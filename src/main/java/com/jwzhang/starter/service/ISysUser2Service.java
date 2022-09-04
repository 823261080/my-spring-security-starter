package com.jwzhang.starter.service;

import com.jwzhang.starter.entity.SysUser2;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户2信息表 服务类
 * </p>
 *
 * @author zjw
 * @since 2022-08-30
 */
public interface ISysUser2Service extends IService<SysUser2> {

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 结果
     */
    SysUser2 findUserByUserName(String username);

    /**
     * 根据用户手机号查询用户信息
     * @param phone 手机号
     * @return 结果
     */
    SysUser2 findUserByPhone(String phone);
}

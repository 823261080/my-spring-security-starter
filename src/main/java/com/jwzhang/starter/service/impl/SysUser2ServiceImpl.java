package com.jwzhang.starter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jwzhang.starter.entity.SysUser2;
import com.jwzhang.starter.enums.StatusEnum;
import com.jwzhang.starter.mapper.SysUser2Mapper;
import com.jwzhang.starter.service.ISysUser2Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户2信息表 服务实现类
 * </p>
 *
 * @author zjw
 * @since 2022-08-30
 */
@Service
public class SysUser2ServiceImpl extends ServiceImpl<SysUser2Mapper, SysUser2> implements ISysUser2Service {

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 结果
     */
    @Override
    public SysUser2 findUserByUserName(String username) {
        return this.baseMapper
                .selectOne(
                        new LambdaQueryWrapper<SysUser2>()
                                .eq(SysUser2::getUsername, username)
                                .eq(SysUser2::getStatus, StatusEnum.NORMAL.getCode())
                );
    }

    /**
     * 根据用户手机号查询用户信息
     *
     * @param phone 手机号
     * @return 结果
     */
    @Override
    public SysUser2 findUserByPhone(String phone) {
        return this.baseMapper
                .selectOne(
                        new LambdaQueryWrapper<SysUser2>()
                                .eq(SysUser2::getPhone, phone)
                                .eq(SysUser2::getStatus, StatusEnum.NORMAL.getCode())
                );
    }
}

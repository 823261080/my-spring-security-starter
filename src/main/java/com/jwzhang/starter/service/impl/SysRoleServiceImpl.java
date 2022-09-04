package com.jwzhang.starter.service.impl;

import com.jwzhang.starter.entity.SysRole;
import com.jwzhang.starter.enums.UserTypeEnum;
import com.jwzhang.starter.mapper.SysRoleMapper;
import com.jwzhang.starter.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author zjw
 * @since 2022-08-17
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return {@link List <SchRole>}
     */
    @Override
    public List<SysRole> findRoleByUserIdAndType(Long userId, UserTypeEnum userTypeEnum) {
        return this.baseMapper.selectRoleByUserId(userId,userTypeEnum.getCode());
    }
}

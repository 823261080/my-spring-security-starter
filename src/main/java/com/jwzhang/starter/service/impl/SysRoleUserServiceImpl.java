package com.jwzhang.starter.service.impl;

import com.jwzhang.starter.entity.SysRoleUser;
import com.jwzhang.starter.mapper.SysRoleUserMapper;
import com.jwzhang.starter.service.ISysRoleUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色用户关联表 服务实现类
 * </p>
 *
 * @author zjw
 * @since 2022-09-02
 */
@Service
public class SysRoleUserServiceImpl extends ServiceImpl<SysRoleUserMapper, SysRoleUser> implements ISysRoleUserService {

}

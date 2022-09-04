package com.jwzhang.starter.mapper;

import com.jwzhang.starter.entity.SysRoleUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色用户关联表 Mapper 接口
 * </p>
 *
 * @author zjw
 * @since 2022-09-02
 */
@Mapper
public interface SysRoleUserMapper extends BaseMapper<SysRoleUser> {

}

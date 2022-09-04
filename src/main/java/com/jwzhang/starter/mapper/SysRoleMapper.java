package com.jwzhang.starter.mapper;

import com.jwzhang.starter.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author zjw
 * @since 2022-08-17
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return {@link List <SchRoleCamera>}
     */
    List<SysRole> selectRoleByUserId(@Param("userId") Long userId, @Param("userType") Integer userType);
}

package com.jwzhang.starter.mapper;

import com.jwzhang.starter.entity.SysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author zjw
 * @since 2022-08-17
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return {@link List <SchMenuCamera>}
     */
    List<SysMenu> selectMenuByUserIdAndType(@Param("userId") Long userId, @Param("userType") Integer userType);
}

package com.jwzhang.starter.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jwzhang.starter.entity.SysUser1;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户1信息 Mapper 接口
 * </p>
 *
 * @author zjw
 * @since 2022-08-17
 */
@Mapper
public interface SysUser1Mapper extends BaseMapper<SysUser1> {

    /**
     * 分页查询机构用户
     * @param page page
     * @param user1 参数
     * @return 结果
     */
    Page<SysUser1> selectListByPage(Page<SysUser1> page, SysUser1 user1);
}

package com.jwzhang.starter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jwzhang.starter.entity.SysUser1;
import com.jwzhang.starter.vo.ChangePwdBody;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *用户信息 服务类
 * </p>
 *
 * @author zjw
 * @since 2022-08-17
 */
public interface ISysUser1Service extends IService<SysUser1> {

    /**
     * 根据用户名称查询用户信息
     *
     * @param username 用户名称
     * @return {@link SysUser1}
     */
    SysUser1 findUserByUsername(String username);

    /**
     * 根据手机号查询用户信息
     * @param phone 手机号
     * @return 结果
     */
    SysUser1 findUserByPhone(String phone);

    /**
     * 分页查询账号列表
     * @param user1 查询参数
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 结果
     */
    Page<SysUser1> listByPage(SysUser1 user1, Integer pageNum, Integer pageSize);

    /**
     * 添加账号
     * @param sysUser1 参数
     * @return 结果
     */
    Boolean addUser1(SysUser1 sysUser1);

    /**
     * 修改账号信息
     * @param sysUser1 参数
     * @return 结果
     */
    Boolean editUser1(SysUser1 sysUser1);

    /**
     * 修改密码
     * @param changePwdBody 修改密码参数
     * @return 结果
     */
    Boolean editPassword(ChangePwdBody changePwdBody);

    /**
     * 删除用户信息
     * @param userIds
     * @return 结果
     */
    Boolean deleteUsers(List<Long> userIds);
}

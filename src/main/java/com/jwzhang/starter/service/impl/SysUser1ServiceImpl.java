package com.jwzhang.starter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jwzhang.starter.entity.SysRoleUser;
import com.jwzhang.starter.entity.SysUser1;
import com.jwzhang.starter.enums.StatusEnum;
import com.jwzhang.starter.enums.UserTypeEnum;
import com.jwzhang.starter.exception.CustomException;
import com.jwzhang.starter.mapper.SysRoleUserMapper;
import com.jwzhang.starter.mapper.SysUser1Mapper;
import com.jwzhang.starter.utils.SecurityUtils;
import com.jwzhang.starter.vo.ChangePwdBody;
import com.jwzhang.starter.service.ISysUser1Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户1信息 服务实现类
 * </p>
 *
 * @author zjw
 * @since 2022-08-17
 */
@Service
public class SysUser1ServiceImpl extends ServiceImpl<SysUser1Mapper, SysUser1> implements ISysUser1Service {

    @Autowired
    private SysRoleUserMapper roleUserMapper;

    /**
     * 根据用户名称查询用户信息
     *
     * @param username 用户名称
     * @return {@link SysUser1}
     */
    @Override
    public SysUser1 findUserByUsername(String username) {
        return this.baseMapper
                .selectOne(
                        new LambdaQueryWrapper<SysUser1>()
                                .eq(SysUser1::getUsername, username)
                                .eq(SysUser1::getStatus, StatusEnum.NORMAL.getCode())
                );
    }

    /**
     * 根据手机号查询用户信息
     *
     * @param phone 手机号
     * @return 结果
     */
    @Override
    public SysUser1 findUserByPhone(String phone) {
        return this.baseMapper.selectOne(
                new LambdaQueryWrapper<SysUser1>()
                        .eq(SysUser1::getPhone,phone)
                        .eq(SysUser1::getStatus,StatusEnum.NORMAL.getCode())
        );
    }

    /**
     * 分页查询用户1账号列表
     *
     * @param user1 查询参数
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @return 结果
     */
    @Override
    public Page<SysUser1> listByPage(SysUser1 user1, Integer pageNum, Integer pageSize) {
        Page<SysUser1> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        return this.baseMapper.selectListByPage(page,user1);
    }

    /**
     * 添加账号
     *
     * @param sysUser1 参数
     * @return 结果
     */
    @Override
    public Boolean addUser1(SysUser1 sysUser1) {
        String password = sysUser1.getPassword();
        if(StringUtils.isBlank(password)){
            throw new CustomException("密码不能为空");
        }
        SysUser1 add = new SysUser1();
        add.setUsername(sysUser1.getUsername());
        add.setNickName(sysUser1.getNickName());
        add.setPhone(sysUser1.getPhone());
        add.setPassword(SecurityUtils.encryptPassword(sysUser1.getPassword()));
        add.setCreateBy(SecurityUtils.getUsername());
        add.setCreateTime(LocalDateTime.now());
        return add.insert();
    }

    /**
     * 修改账号信息
     *
     * @param sysUser1 参数
     * @return 结果
     */
    @Override
    public Boolean editUser1(SysUser1 sysUser1) {
        Long userId = sysUser1.getUserId();
        if(userId == null){
            throw new CustomException("用户id不能为空");
        }
        SysUser1 update = new SysUser1();
        update.setUsername(sysUser1.getUsername());
        update.setNickName(sysUser1.getNickName());
        update.setPhone(sysUser1.getPhone());
        update.setUpdateBy(SecurityUtils.getUsername());
        update.setUpdateTime(LocalDateTime.now());
        return update.updateById();
    }

    /**
     * 修改密码
     *
     * @param changePwdBody 修改密码参数
     * @return 结果
     */
    @Override
    public Boolean editPassword(ChangePwdBody changePwdBody) {
        Long userId = changePwdBody.getUserId();
        String oldPassword = changePwdBody.getOldPassword();
        String password = changePwdBody.getPassword();
        String confirmPassowd = changePwdBody.getConfirmPassowd();
        if(userId == null){
            throw new CustomException("用户id不能为空");
        }
        if(StringUtils.isBlank(oldPassword)){
            throw new CustomException("原密码不能为空");
        }
        if(StringUtils.isBlank(password)){
            throw new CustomException("新密码不能为空");
        }
        if(StringUtils.isBlank(confirmPassowd)){
            throw new CustomException("确认密码不能为空");
        }
        if(!StringUtils.equals(password,confirmPassowd)){
            throw new CustomException("两次输入密码不一致");
        }
        //校验原密码是否正确
        SysUser1 userCamera = this.baseMapper.selectById(userId);
        if(userCamera == null){
            throw new CustomException("用户不存在");
        }
        if(!SecurityUtils.matchesPassword(oldPassword,userCamera.getPassword())){
            throw new CustomException("密码不正确");
        }

        SysUser1 update = new SysUser1();
        update.setUserId(userId);
        update.setPassword(SecurityUtils.encryptPassword(password));
        update.setUpdateBy(SecurityUtils.getUsername());
        update.setUpdateTime(LocalDateTime.now());
        return update.updateById();
    }

    /**
     * 删除用户信息
     *
     * @param userIds
     * @return 结果
     */
    @Override
    @Transactional
    public Boolean deleteUsers(List<Long> userIds) {
        boolean result = false;
        for (Long userId : userIds) {
            roleUserMapper.delete(
                    new LambdaQueryWrapper<SysRoleUser>()
                            .eq(SysRoleUser::getUserId,userId)
                            .eq(SysRoleUser::getUserType, UserTypeEnum.USER1.getCode())
            );
            int i = this.baseMapper.deleteById(userId);
            if(!result && i > 0){
                result = true;
            }
        }
        return result;
    }
}

package com.jwzhang.starter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author zjw
 * @since 2022-08-30
 */
@Getter
@Setter
@TableName("sys_user_type2")
@ApiModel(value = "SysUserType2对象", description = "用户2表")
public class SysUser2 extends Model<SysUser2> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户ID")
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @ApiModelProperty("用户账号")
    @TableField("user_name")
    private String username;

    @ApiModelProperty("用户密码")
    @TableField("password")
    private String password;

    @ApiModelProperty("用户姓名")
    @TableField("nick_name")
    private String nickName;

    @ApiModelProperty("身份证号")
    @TableField("pin_code")
    private String pinCode;

    @ApiModelProperty("邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty("电话")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("性别（0男 1女 2未知）")
    @TableField("sex")
    private String sex;

    @ApiModelProperty("头像地址")
    @TableField("avatar")
    private String avatar;

    @ApiModelProperty("帐号状态（0正常 1停用）")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("删除标志（0代表存在 2代表删除）")
    @TableField("del_flag")
    private String delFlag;

    @ApiModelProperty("最后登录IP")
    @TableField("login_ip")
    private String loginIp;

    @ApiModelProperty("最后登录时间")
    @TableField("login_date")
    private LocalDateTime loginDate;

    @ApiModelProperty("创建者")
    @TableField("create_by")
    private String createBy;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty("更新者")
    @TableField("update_by")
    private String updateBy;

    @ApiModelProperty("更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @Override
    public Serializable pkVal() {
        return this.userId;
    }
}

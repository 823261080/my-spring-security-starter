package com.jwzhang.starter.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.jwzhang.starter.enums.converter.SexEnumConverter;
import com.jwzhang.starter.enums.converter.StatusEnumConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author zjw
 * @since 2022-08-17
 */
@Getter
@Setter
@TableName("sys_user")
@ApiModel(value = "SysUser对象", description = "用户信息")
public class SysUser1 extends Model<SysUser1> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户ID")
    @ExcelIgnore
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;


    @ApiModelProperty("用户账号")
    @ExcelProperty("用户账号")
    @TableField("username")
    private String username;

    @ApiModelProperty("用户密码")
    @ExcelIgnore
    @TableField("password")
    private String password;

    @ApiModelProperty("用户姓名")
    @ExcelProperty("用户姓名")
    @TableField("nick_name")
    private String nickName;

    @ApiModelProperty("性别（0男 1女 2未知）")
    @ExcelProperty(value = "性别", converter = SexEnumConverter.class)
    @TableField("sex")
    private Integer sex;

    @ApiModelProperty("电话")
    @ExcelProperty("电话")
    @TableField("phone")
    private String phone;

    @ApiModelProperty("头像地址")
    @ExcelProperty("头像地址")
    @TableField("avatar")
    private String avatar;

    @ApiModelProperty("帐号状态（0正常 1停用）")
    @ExcelProperty(value = "帐号状态",converter = StatusEnumConverter.class)
    @TableField("status")
    private Integer status;

    @ApiModelProperty("创建者")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @TableField("create_by")
    private String createBy;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    @ExcelProperty("创建时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
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

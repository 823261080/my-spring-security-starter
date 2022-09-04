package com.jwzhang.starter.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 角色用户关联表
 * </p>
 *
 * @author zjw
 * @since 2022-09-02
 */
@Getter
@Setter
@TableName("sys_role_user")
@ApiModel(value = "SysRoleUser对象", description = "角色用户关联表")
public class SysRoleUser extends Model<SysRoleUser> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色和用户关联表")
    @TableId("role_user_id")
    private Long roleUserId;

    @ApiModelProperty("角色id")
    @TableField("role_id")
    private Long roleId;

    @ApiModelProperty("用户id")
    @TableField("user_id")
    private Long userId;

    @ApiModelProperty("用户类型 0-用户类型1 1-用户类型2")
    @TableField("user_type")
    private Integer userType;

    @Override
    public Serializable pkVal() {
        return this.roleUserId;
    }
}

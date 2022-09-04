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
 * 角色菜单关联表
 * </p>
 *
 * @author zjw
 * @since 2022-09-02
 */
@Getter
@Setter
@TableName("sys_role_menu")
@ApiModel(value = "SysRoleMenu对象", description = "角色菜单关联表")
public class SysRoleMenu extends Model<SysRoleMenu> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色菜单关联表")
    @TableId("role_menu_id")
    private Long roleMenuId;

    @ApiModelProperty("角色id")
    @TableField("role_id")
    private Long roleId;

    @ApiModelProperty("菜单id")
    @TableField("menu_id")
    private Long menuId;

    @Override
    public Serializable pkVal() {
        return this.roleMenuId;
    }
}

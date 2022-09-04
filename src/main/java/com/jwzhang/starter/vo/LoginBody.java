package com.jwzhang.starter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录信息
 */
@ApiModel("用户登录参数")
@Data
public class LoginBody {
    /**
     * 用户名
     */
    @ApiModelProperty("账号")
    private String username;

    /**
     * 用户密码
     */
    @
    ApiModelProperty("密码")
    private String password;

    /**
     * 验证码
     */
    @ApiModelProperty("图片验证码")
    private String code;

    /**
     * 唯一标识
     */
    @ApiModelProperty("图片验证码uuid")
    private String uuid;

    /**
     * 用户类型 0-用户1 1-用户2
     */
    @ApiModelProperty(value = "用户类型 0-用户1 1-用户2")
    private Integer userType;
}

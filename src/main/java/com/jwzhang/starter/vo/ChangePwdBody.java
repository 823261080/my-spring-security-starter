package com.jwzhang.starter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改密码参数
 */
@ApiModel("修改密码参数")
@Data
public class ChangePwdBody {
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 短信验证码
     */
    @ApiModelProperty("短信验证码")
    private String smsCode;

    /**
     * 短信验证码uuid
     */
    @ApiModelProperty("短信验证码uuid")
    private String smsUuid;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 用户类型  0机构 1教育局 2学校
     */
    @ApiModelProperty("用户类型  0机构 1教育局 2学校")
    private Integer userType;

    /**
     * 原密码
     */
    @ApiModelProperty("原密码")
    private String oldPassword;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 确认密码
     */
    @ApiModelProperty("确认密码")
    private String confirmPassowd;

}

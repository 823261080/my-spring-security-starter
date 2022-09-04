package com.jwzhang.starter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 发送短信验证码参数
 */
@ApiModel("发送短信验证码参数")
@Data
public class SendSmsBody {
    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 图片验证码uuid
     */
    @ApiModelProperty("图片验证码uuid")
    private String uuid;

    /**
     * 用户类型 0-用户1 1-用户2
     */
    @ApiModelProperty("用户类型 0-用户1 1-用户2")
    private Integer userType;

    /**
     * 图片验证码结果
     */
    @ApiModelProperty("图片验证码")
    private String imageCode;
}

package com.jwzhang.starter.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 验证码信息
 */
@ApiModel("验证码信息")
@Data
public class CaptchaVo {
    /**
     * 验证码uuid
     */
    @ApiModelProperty("验证码uuid")
    private String uuid;

    /**
     * 验证码图片base64
     * <p>
     *     返回信息前缀添加“data:image/jpeg;base64,”
     * </p>
     */
    @ApiModelProperty(value = "验证码图片base64",notes = "返回信息前缀添加\"data:image/jpeg;base64,\"")
    private String img;

}

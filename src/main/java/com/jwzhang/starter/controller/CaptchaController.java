package com.jwzhang.starter.controller;

import com.google.code.kaptcha.Producer;
import com.jwzhang.starter.utils.IdUtils;
import com.jwzhang.starter.utils.RedisCache;
import com.jwzhang.starter.vo.Ajax;
import com.jwzhang.starter.vo.CaptchaVo;
import com.jwzhang.starter.constant.Constants;
import com.jwzhang.starter.exception.CustomException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;

/**
 * 验证码操作处理
 */
@Api(tags = "验证码操作接口")
@Slf4j
@RestController
public class CaptchaController {

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    // 验证码类型
    @Value("${whcamera.captchaType}")
    private String captchaType;

    /**
     * 生成验证码
     */
    @ApiOperation("生成验证码")
    @GetMapping("/captchaImage")
    public Ajax<CaptchaVo> getCode() throws IOException {
        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;

        String capStr = null;
        String code = null;
        BufferedImage image = null;

        // 生成验证码
        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        } else{
            log.error("！！配置信息中未配置starter.captchaType参数！！");
            throw new CustomException("服务器错误，请联系管理员");
        }

        RedisCache.set(verifyKey, code, Constants.CAPTCHA_EXPIRATION);

        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", os);
        } catch (IOException e) {
            return Ajax.error(e.getMessage());
        }

        CaptchaVo captchaVo = new CaptchaVo();
        captchaVo.setUuid(uuid);
        captchaVo.setImg(new String(Base64.getEncoder().encode(os.toByteArray())));
        return Ajax.success(capStr,captchaVo);
    }
}

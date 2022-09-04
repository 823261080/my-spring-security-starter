package com.jwzhang.starter.constant;

/**
 * 常量
 *
 * @author zjw
 * @since 2022/8/23
 */
public interface Constants {
    /**
     * token保存到redis的前缀
     */
    String REDIS_TOKEN_PREFIX = "token:";

    /**
     * token黑名单redis前缀
     */
    String REDIS_BLACKLIST_PREFIX = "blacklist:";

    /**
     * token加入黑名单后，在黑名单存在时长，单位s
     * （30天）
     */
    Integer TOKEN_BLACKLIST_TIME = 30 * 24 * 60 * 60;

    /**
     * 验证码缓存key
     */
    String CAPTCHA_CODE_KEY = "captcha:codes:";

    /**
     * 验证码有效期（秒）
     */
    Integer CAPTCHA_EXPIRATION = 120;

    /**
     * 短信验证码key
     */
    String SMS_CODE_KEY = "sms:codes:";

    /**
     * 短信验证码过期时长，秒
     */
    Integer SMS_CODE_EXPIRE = 120;
}

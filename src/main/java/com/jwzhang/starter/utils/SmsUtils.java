package com.jwzhang.starter.utils;

import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.jwzhang.starter.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * 短信工具类
 *
 * @author zjw
 * @since 2022/5/10
 */
@Slf4j
public class SmsUtils {
    /** 短信访问的域名 */
    private final static String SMS_END_POINT = "dysmsapi.aliyuncs.com";

    /** 短信 AccessKey ID */
    private final static String ACCESS_KEY_ID = "xxxxxxxxxxxx-xxxxxxxxxxxxx-xxxxx";

    /** 短信 AccessKey Secret */
    private final static String ACCESS_KEY_SECRET = "xxxxx-xxxxxxx-xxxxxxxxxx";

    /** 短信签名 */
    private final static String SIGN_NAME = "短信签名";

    /**
     * 创建客户端
     *
     * @param accessKeyId     accessKeyId
     * @param accessKeySecret accessKeySecret
     * @return {@link Client}
     * @throws Exception 异常
     */
    private static Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = SMS_END_POINT;
        return new Client(config);
    }

    /**
     * 发送短信<br>
     *
     * @param templateParamJson 短信模板参数
     * @param phoneNum          手机号
     * @param templateCode      模板code
     * @param signName          模板签名
     * @param accessKeyId       accessKeyId
     * @param accessKeySecret   accessKeySecret
     */
    public static void sendMessage(String templateParamJson, String phoneNum, String templateCode, String signName,
                                   String accessKeyId, String accessKeySecret) {
        try {
            Client client = createClient(accessKeyId, accessKeySecret);
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setPhoneNumbers(phoneNum)
                    .setSignName(signName)
                    .setTemplateCode(templateCode)
                    .setTemplateParam(templateParamJson);
            log.info("【发送短信】请求内容：{}", JSON.toJSONString(sendSmsRequest));
            SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);
            if (sendSmsResponse.getBody() != null && sendSmsResponse.getBody().getCode() != null) {
                //请求成功
                log.info("【发送短信】返回结果：{}", JSON.toJSONString(sendSmsResponse.getBody()));
            }
        } catch (Exception e) {
            log.error("【发送短信】失败，e={}", e.getMessage());
            e.printStackTrace();
            throw new CustomException("发送短信失败");
        }
    }

    /**
     * 发送短信<br>
     * 默认 accessKeyId <br>
     * 默认 accessKeySecret
     *
     * @param templateParamMap 短信模板参数
     * @param phoneNum         手机号
     * @param templateCode     模板code
     * @param signName         模板签名
     */
    public static void sendMessage(Map<String, String> templateParamMap, String phoneNum, String templateCode, String signName) {
        String templateParamJson = null;
        if (!CollectionUtils.isEmpty(templateParamMap)) {
            templateParamJson = JSON.toJSONString(templateParamMap);
        }
        sendMessage(templateParamJson, phoneNum, templateCode, signName, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }

    /**
     * 发送短信 <br>
     * 默认 accessKeyId <br>
     * 默认 accessKeySecret <br>
     * 默认签名 “伟航信息智慧校园”
     *
     * @param templateParamMap 模板参数
     * @param phoneNum         手机号
     * @param templateCode     模板code
     */
    public static void sendMessage(Map<String, String> templateParamMap, String phoneNum, String templateCode) {
        sendMessage(templateParamMap, phoneNum, templateCode, SIGN_NAME);
    }

    /**
     * 发送短信 <br>
     * 默认 短信模板无参
     * 默认 accessKeyId <br>
     * 默认 accessKeySecret <br>
     * 默认签名 “伟航信息智慧校园”
     *
     * @param phoneNum     手机号
     * @param templateCode 模板code
     */
    public static void sendMessage(String phoneNum, String templateCode) {
        sendMessage(null, phoneNum, templateCode);
    }

    /**
     * 生成随机6位短信验证码
     */
    public static String genSmsCode(){
        StringBuilder code = new StringBuilder();
        code.append(RandomUtils.nextInt(1, 10));
        for (int i = 0; i < 5; i++) {
            code.append(RandomUtils.nextInt(0, 10));
        }
        return code.toString();
    }
}

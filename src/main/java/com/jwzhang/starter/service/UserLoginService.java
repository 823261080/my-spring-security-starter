package com.jwzhang.starter.service;

import com.jwzhang.starter.vo.ChangePwdBody;
import com.jwzhang.starter.vo.LoginBody;
import com.jwzhang.starter.vo.SendSmsBody;

/**
 * 用户登录服务接口
 */
public interface UserLoginService {

    /**
     * 登录
     * @return token
     */
    String login(LoginBody loginBody);

    /**
     * 发送短信
     * @param sendSmsBody 发送短信验证码参数
     * @return 发送结果
     */
    String sendSmsCode(SendSmsBody sendSmsBody);

    /**
     * 修改密码
     * @param changePwdBody 修改密码参数
     * @return 结果
     */
    String changePassword(ChangePwdBody changePwdBody);
}

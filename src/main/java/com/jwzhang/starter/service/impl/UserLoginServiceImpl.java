package com.jwzhang.starter.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jwzhang.starter.constant.Constants;
import com.jwzhang.starter.entity.SysUser1;
import com.jwzhang.starter.entity.SysUser2;
import com.jwzhang.starter.enums.UserTypeEnum;
import com.jwzhang.starter.exception.CustomException;
import com.jwzhang.starter.security.userdetails.CustomUserDetails;
import com.jwzhang.starter.service.ISysUser2Service;
import com.jwzhang.starter.utils.*;
import com.jwzhang.starter.utils.ip.IpUtils;
import com.jwzhang.starter.vo.ChangePwdBody;
import com.jwzhang.starter.vo.LoginBody;
import com.jwzhang.starter.vo.SendSmsBody;
import com.jwzhang.starter.service.UserLoginService;
import com.jwzhang.starter.service.ISysUser1Service;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户登录服务实现类
 */
@Slf4j
@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ISysUser1Service user1Service;

    @Autowired
    private ISysUser2Service user2Service;

    /**
     * 登录
     *
     * @param loginBody 登录信息
     * @return token
     */
    @Override
    public String login(LoginBody loginBody) {
        checkImageCode(loginBody.getUuid(), loginBody.getCode());

        // 用户验证
        Authentication authentication = null;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginBody.getUsername(), loginBody.getPassword()));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                throw new CustomException("用户不存在/密码错误");
            } else {
                log.error("系统异常:", e);
                throw new CustomException("登录失败:" + e.getMessage());
            }
        }
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        // 生成token
        String accessToken = JwtTokenUtils.createAccessToken(customUserDetails);
        // 保存Token信息到Redis中
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        JwtTokenUtils.setTokenInfo(accessToken, customUserDetails.getUsername(), ip);

        log.info("用户{}登录成功，Token信息已保存到Redis", customUserDetails.getUsername());
        return accessToken;
    }

    /**
     * 校验图片验证码
     *
     * @param imageUuid 图片验证码uuid
     * @param imageCode 图片验证码
     */
    private void checkImageCode(String imageUuid, String imageCode) {

        String verifyKey = Constants.CAPTCHA_CODE_KEY + imageUuid;
        String captcha = RedisCache.get(verifyKey);
        RedisCache.deleteKeyForValue(verifyKey);
        if (captcha == null) {
            throw new CustomException("验证码已失效");
        }
        if (!StringUtils.equalsIgnoreCase(imageCode, captcha)) {
            throw new CustomException("验证码错误");
        }
    }

    /**
     * 发送短信
     * @param sendSmsBody 发送短信验证码参数
     * @return 发送结果
     */
    @Override
    public String sendSmsCode(SendSmsBody sendSmsBody) {
        Integer userType = sendSmsBody.getUserType();
        String imageCode = sendSmsBody.getImageCode();
        String phone = sendSmsBody.getPhone();
        String uuid = sendSmsBody.getUuid();

        checkImageCode(uuid, imageCode);

        UserTypeEnum userTypeEnum = UserTypeEnum.getByCode(userType);
        if(userTypeEnum == null){
            throw new CustomException("用户类型不正确");
        }

        //校验用户手机号
        switch (userTypeEnum) {
            //用户1
            case USER1: {
                SysUser1 user1 = user1Service.findUserByPhone(phone);
                if (user1 == null) {
                    log.error("未查询到用户信息,phone={}",phone);
                    throw new CustomException("未查询到用户信息");
                }
                break;
            }
            //用户2
            case USER2: {
                SysUser2 user2 = user2Service.findUserByPhone(phone);
                if (user2 == null) {
                    log.error("未查询到用户信息,phone={}",phone);
                    throw new CustomException("未查询到用户信息");
                }
                break;
            }
            default:
                throw new CustomException("用户类型不正确");
        }
        //发送重置密码验证码短信
        String smsCode = SmsUtils.genSmsCode();
        //发送短信
        Map<String, String> params = new HashMap<>();
        params.put("code", smsCode);
        SmsUtils.sendMessage(params, phone, "SMS_180740064");

        // 保存验证码信息
        String smsCodeUuid = IdUtils.simpleUUID();
        RedisCache.set(Constants.SMS_CODE_KEY + smsCodeUuid, smsCode, Constants.SMS_CODE_EXPIRE);

        //返回短信uuid
        return smsCodeUuid;
    }

    /**
     * 修改密码
     *
     * @param changePwdBody 修改密码参数
     * @return 结果
     */
    @Override
    public String changePassword(ChangePwdBody changePwdBody) {
        String smsCode = changePwdBody.getSmsCode();
        String smsUuid = changePwdBody.getSmsUuid();

        //校验短信验证码
        String smsCodeInRedis = RedisCache.get(Constants.SMS_CODE_KEY + ":" + smsUuid);
        if(!StringUtils.equalsIgnoreCase(smsCodeInRedis,smsCode)){
            throw new CustomException("短信验证码错误");
        }

        //校验密码是否一致
        String password = changePwdBody.getPassword();
        String confirmPassowd = changePwdBody.getConfirmPassowd();
        if(!StringUtils.equals(password,confirmPassowd)){
            throw new CustomException("两次输入密码不一致");
        }

        //校验用户类型
        Integer userType = changePwdBody.getUserType();
        UserTypeEnum userTypeEnum = UserTypeEnum.getByCode(userType);
        if(userTypeEnum == null){
            throw new CustomException("用户类型不正确");
        }

        //修改密码
        String phone = changePwdBody.getPhone();
        //校验用户手机号
        switch (userTypeEnum) {
            //用户1
            case USER1: {
                SysUser1 userCamera = user1Service.findUserByPhone(phone);
                if (userCamera == null) {
                    log.error("未查询到用户信息,phone={}",phone);
                    throw new CustomException("未查询到用户信息");
                }
                //修改密码
                user1Service.update(new LambdaUpdateWrapper<SysUser1>()
                        .set(SysUser1::getUserId,userCamera.getUserId())
                        .set(SysUser1::getPassword,password)
                        .set(SysUser1::getUpdateTime, LocalDateTime.now())
                );
                break;
            }
            //用户2
            case USER2: {
                SysUser2 user2 = user2Service.findUserByPhone(phone);
                if (user2 == null) {
                    log.error("未查询到用户信息,phone={}",phone);
                    throw new CustomException("未查询到用户信息");
                }

                //修改密码
                user2Service.update(new LambdaUpdateWrapper<SysUser2>()
                        .set(SysUser2::getUserId,user2.getUserId())
                        .set(SysUser2::getPassword,password)
                        .set(SysUser2::getUpdateTime, LocalDateTime.now())
                );

                break;
            }
            default:
                throw new CustomException("用户类型不正确");
        }

        return "修改成功";
    }
}

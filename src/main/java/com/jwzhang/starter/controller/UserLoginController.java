package com.jwzhang.starter.controller;

import com.alibaba.fastjson.JSON;
import com.jwzhang.starter.enums.UserTypeEnum;
import com.jwzhang.starter.utils.SecurityUtils;
import com.jwzhang.starter.vo.Ajax;
import com.jwzhang.starter.vo.ChangePwdBody;
import com.jwzhang.starter.vo.LoginBody;
import com.jwzhang.starter.vo.SendSmsBody;
import com.jwzhang.starter.security.userdetails.CustomUserDetails;
import com.jwzhang.starter.service.UserLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 登录控制器
 */
@Api(tags = "登录相关接口")
@Slf4j
@RestController
@RequestMapping("/login")
public class UserLoginController {

    @Autowired
    private UserLoginService userLoginService;

    /**
     * User1登录
     * @return  用户信息
     */
    @ApiOperation("用户1登录")
    @PostMapping("/user1Login")
    public Ajax<String> userType1Login(@RequestBody LoginBody loginBody){
        return Ajax.success(userLoginService.login(loginBody));
    }

    /**
     * User2登录
     * @return  用户信息
     */
    @ApiOperation("User2登录")
    @PostMapping("/user2Login")
    public Ajax<String> userType2Login(@RequestBody LoginBody loginBody){

        return Ajax.success(userLoginService.login(loginBody));
    }

    /**
     * 发送短信
     * @param sendSmsBody 发送短信验证码参数
     * @return 发送结果
     */
    @ApiOperation("发送短信")
    @PostMapping("/sendSmsCode")
    public Ajax<String> sendSmsCode(@RequestBody SendSmsBody sendSmsBody){
        //校验参数
        if(StringUtils.isBlank(sendSmsBody.getPhone())){
            return Ajax.error("手机号不能为空");
        }
        if(StringUtils.isBlank(sendSmsBody.getUuid())){
            return Ajax.error("图片验证码uuid不能为空");
        }
        if(sendSmsBody.getUserType() == null){
            return Ajax.error("用户类型不能为空");
        }
        if(UserTypeEnum.getByCode(sendSmsBody.getUserType()) == null){
            return Ajax.error("用户类型不正确");
        }
        return Ajax.success(userLoginService.sendSmsCode(sendSmsBody));
    }

    /**
     * 修改密码
     * @return 结果
     */
    @ApiOperation("忘记密码-修改密码")
    @PutMapping("/changePassword")
    public Ajax<String> changePassword(@RequestBody ChangePwdBody changePwdBody){
        return Ajax.success(userLoginService.changePassword(changePwdBody));
    }

    /**
     * 获取登录用户信息
     * @return 用户信息
     */
    @ApiOperation("获取登录用户信息")
    @GetMapping("/userInfo")
    public Ajax<CustomUserDetails> userInfo(){
        CustomUserDetails userDetails = SecurityUtils.getUserDetails();
        log.info("userDetails:{}", JSON.toJSONString(userDetails));
        return Ajax.success(userDetails);
    }

}

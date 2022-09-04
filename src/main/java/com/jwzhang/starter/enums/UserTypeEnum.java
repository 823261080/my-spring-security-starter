package com.jwzhang.starter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 用户类型枚举
 */
@Getter
@AllArgsConstructor
public enum UserTypeEnum {
    /**
     * 机构
     */
    USER1(0,"/login/sysUser1Login"),
    /**
     * 教育局
     */
    USER2(1,"/login/sysUser2Login"),
    ;
    private Integer code;
    private String path;

    /**
     * 根据path获取枚举
     * @param path 枚举path
     * @return 枚举
     */
    public static UserTypeEnum getByPath(String path){
        UserTypeEnum[] enums = UserTypeEnum.values();
        for (UserTypeEnum status : enums) {
            if(StringUtils.equals(path,status.getPath())){
                return status;
            }
        }
        return null;
    }

    /**
     * 根据value获取枚举
     * @param enumCode 枚举code
     * @return 枚举
     */
    public static UserTypeEnum getByCode(Integer enumCode){
        UserTypeEnum[] enums = UserTypeEnum.values();
        for (UserTypeEnum status : enums) {
            if(Objects.equals(enumCode,status.getCode())){
                return status;
            }
        }
        //默认返回normal
        return null;
    }
}

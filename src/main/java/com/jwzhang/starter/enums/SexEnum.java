package com.jwzhang.starter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * SexEnum-性别枚举
 *
 * @author zjw
 * @since 2022/8/17
 */
@Getter
@AllArgsConstructor
public enum SexEnum {
    NORMAL(0,"男"),
    STOP(1,"女"),
    ;

    private Integer code;

    private String value;

    /**
     * 根据value获取枚举
     * @param enumValue 枚举value
     * @return 枚举
     */
    public static SexEnum getByValue(String enumValue){
        SexEnum[] enums = SexEnum.values();
        for (SexEnum status : enums) {
            if(StringUtils.equals(enumValue,status.getValue())){
                return status;
            }
        }
        //默认返回normal
        return NORMAL;
    }

    /**
     * 根据value获取枚举
     * @param enumCode 枚举code
     * @return 枚举
     */
    public static SexEnum getByCode(Integer enumCode){
        SexEnum[] enums = SexEnum.values();
        for (SexEnum status : enums) {
            if(Objects.equals(enumCode,status.getCode())){
                return status;
            }
        }
        //默认返回normal
        return NORMAL;
    }


}

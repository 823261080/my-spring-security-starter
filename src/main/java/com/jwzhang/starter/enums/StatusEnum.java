package com.jwzhang.starter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * StatusEnum-状态枚举
 *
 * @author zjw
 * @since 2022/8/17
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {
    NORMAL(0,"正常"),
    STOP(1,"停用"),
    ;

    private Integer code;

    private String value;

    /**
     * 根据value获取枚举
     * @param enumValue 枚举value
     * @return 枚举
     */
    public static StatusEnum getByValue(String enumValue){
        StatusEnum[] enums = StatusEnum.values();
        for (StatusEnum status : enums) {
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
    public static StatusEnum getByCode(Integer enumCode){
        StatusEnum[] enums = StatusEnum.values();
        for (StatusEnum status : enums) {
            if(Objects.equals(enumCode,status.getCode())){
                return status;
            }
        }
        //默认返回normal
        return NORMAL;
    }


}

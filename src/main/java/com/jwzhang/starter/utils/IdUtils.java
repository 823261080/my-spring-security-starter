package com.jwzhang.starter.utils;

import java.util.UUID;

/**
 * id生成工具类
 */
public class IdUtils {

    /**
     * 简化的UUID，去掉了横线
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String simpleUUID()
    {
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}

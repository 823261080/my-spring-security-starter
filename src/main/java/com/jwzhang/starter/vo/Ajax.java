package com.jwzhang.starter.vo;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletResponse;
import java.io.PrintWriter;

/**
 * Ajax
 *
 * @author zjw
 * @since 2022/8/17
 */
@ApiModel("返回结果")
@Slf4j
@Data
@AllArgsConstructor
public class Ajax<T> {
    /**
     * 返回编码
     */
    @ApiModelProperty("返回码")
    private Integer code;

    /**
     * 返回消息
     */
    @ApiModelProperty("返回消息")
    private String msg;

    /**
     * 返回数据
     */
    @ApiModelProperty("返回数据")
    private T data;

    public static <T> Ajax<T> success(){
        return new Ajax<>(200,"操作成功",null);
    }

    public static <T> Ajax<T> success(T data){
        return new Ajax<>(200,"操作成功",data);
    }

    public static <T> Ajax<T> success(String msg, T data){
        return new Ajax<>(200,msg,data);
    }

    public static <T> Ajax<T> error(){
        return new Ajax<>(500,"操作失败",null);
    }

    public static <T> Ajax<T> error(Integer code){
        return new Ajax<>(code,"操作失败",null);
    }

    public static <T> Ajax<T> error(String msg){
        return new Ajax<>(500,msg,null);
    }

    public static <T> Ajax<T> error(String msg, T data){
        return new Ajax<>(500,msg,data);
    }

    public static <T> Ajax<T> error(Integer code, String msg, T data){
        return new Ajax<>(code,msg,data);
    }

    public static <T> Ajax<T> error(Integer code, String msg){
        return new Ajax<>(code,msg,null);
    }

    public static Ajax<?> toAjax(Integer result){
        if(result == null || result <= 0){
            return Ajax.error("修改失败");
        }
        return Ajax.success("修改成功");
    }

    public static Ajax<?> toAjax(Boolean result){
        if(result == null || !result){
            return Ajax.error("修改失败");
        }
        return Ajax.success("修改成功");
    }

    /**
     * Response输出Json格式
     *
     * @param response
     * @param data     返回数据
     */
    public static void responseJson(ServletResponse response, Object data) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            out = response.getWriter();
            out.println(JSON.toJSONString(data));
            out.flush();
        } catch (Exception e) {
            log.error("Response输出Json异常：" + e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}

package com.jwzhang.starter.security.exception;

import com.jwzhang.starter.exception.CustomException;
import com.jwzhang.starter.utils.IdUtils;
import com.jwzhang.starter.vo.Ajax;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

/**
 * GlobalExceptionHandler
 *
 * @author zjw
 * @since 2022/8/23
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常
     */
    @ExceptionHandler(CustomException.class)
    public Ajax<?> customException(CustomException e)
    {
        if (Objects.isNull(e.getCode()))
        {
            return Ajax.error(e.getMessage());
        }
        return Ajax.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Ajax<?> handleException(Exception e)
    {
        String errorId = IdUtils.simpleUUID();
        log.error("系统异常-uuid={},异常信息为:",errorId,e);
        return Ajax.error("系统异常，请联系管理员，messageId="+errorId);
    }
}

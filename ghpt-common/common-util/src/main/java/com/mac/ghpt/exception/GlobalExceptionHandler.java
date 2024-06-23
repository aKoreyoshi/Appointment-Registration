package com.mac.ghpt.exception;

import com.mac.ghpt.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: 马聪
 * @function:  全局异常处理类
 * @version: 1.0
 * @date: 2024年03月05日, 17:48:57
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.fail();
    }

    @ExceptionHandler(GhptException.class)
    @ResponseBody
    public Result error(GhptException e) {
        return Result.build(e.getCode(),e.getMessage());
    }
}

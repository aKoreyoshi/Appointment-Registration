package com.mac.ghpt.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: 马聪
 * @function: 统一返回结果集
 * @version: 1.0
 * @date: 2024年03月04日, 21:54:21
 */
@Data
@Schema(description = "统一返回结果集")
public class Result<T> {

    @Schema(description = "业务状态码")
    private Integer code;

    @Schema(description = "响应信息")
    private String message;

    @Schema(description = "业务数据")
    private T data;

    public Result() {
    }

    public static<T> Result<T> build(T data) {
        Result<T> result = new Result<T>();
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

    public static<T> Result<T> build(T data, ResultCodeEnum resultCodeEnum) {
        Result<T> result = build(data);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    public static<T> Result<T> build(Integer code, String message) {
        Result<T> result = build(null);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static<T> Result<T> ok() {
        return Result.ok(null);
    }

    public static<T> Result<T> ok(T data) {
//        Result<T> result = build(data);
        return build(data,ResultCodeEnum.SUCCESS);
    }

    public static<T> Result<T> fail(){
        return Result.fail(null);
    }

    public static<T> Result<T> fail(T data) {
        Result<T> result = build(data);
        return build(data,ResultCodeEnum.FAIL);
    }

    public Result<T> message(String message) {
        this.setMessage(message);
        return this;
    }

    public Result<T> code(Integer code){
        this.setCode(code);
        return this;
    }


}

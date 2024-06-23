package com.mac.ghpt.exception;

import com.mac.ghpt.result.ResultCodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: 马聪
 * @function: 自定义异常处理类
 * @version: 1.0
 * @date: 2024年03月05日, 17:50:44
 */
@Data
@Schema(description = "自定义异常处理类")
public class GhptException extends RuntimeException {

    @Schema(description = "异常状态码")
    private Integer code;

    public GhptException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public GhptException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

}

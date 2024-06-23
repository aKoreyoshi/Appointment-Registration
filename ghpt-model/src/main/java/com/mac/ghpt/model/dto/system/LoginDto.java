package com.mac.ghpt.model.dto.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月01日, 15:53:52
 */
@Data
@Schema(description = "用户提交登录参数")
public class LoginDto {

    private String userName;
    private String password;
}

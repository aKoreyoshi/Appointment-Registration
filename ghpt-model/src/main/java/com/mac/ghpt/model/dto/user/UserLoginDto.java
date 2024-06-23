package com.mac.ghpt.model.dto.user;

import lombok.Data;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月05日, 14:41:34
 */
@Data
public class UserLoginDto {
    private String phone;
    private String code;
}
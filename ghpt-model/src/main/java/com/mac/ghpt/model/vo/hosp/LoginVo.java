package com.mac.ghpt.model.vo.hosp;

import lombok.Data;

/**
 * @author: 马聪
 * @function: 封装用户登录信息
 * @version: 1.0
 * @date: 2024年03月05日, 18:08:20
 */
@Data
public class LoginVo {

    private String token;
    private String refresh_token;  // 该字段不会存储对应的值
}

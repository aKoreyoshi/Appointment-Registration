package com.mac.ghpt.model.vo.doctor;

import lombok.Data;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月27日, 20:34:42
 */
@Data
public class DoctorLoginVo {
    private String token;
    private String refresh_token;  // 该字段不会存储对应的值
}
package com.mac.ghpt.model.dto.user;

import lombok.Data;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月05日, 17:49:28
 */
@Data
public class UserAuthDto {

    private String name;
    private String certificateType;
    private String certificateNo;
    private String certificateUrl;

}
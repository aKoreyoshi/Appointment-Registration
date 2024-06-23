package com.mac.ghpt.model.dto.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月11日, 15:17:01
 */
@Data
@Schema(description = "医院状态参数")
public class HospitalStatusDto {

    private String hospitalCode;
    private Integer status;
}

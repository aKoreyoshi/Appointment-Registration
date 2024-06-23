package com.mac.ghpt.model.dto.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: 马聪
 * @function: 科室参数实体
 * @version: 1.0
 * @date: 2024年03月13日, 15:24:37
 */
@Data
public class DepartmentDto {

    @Schema(description = "科室名称")
    private String departmentName;

    @Schema(description = "大科室名称")
    private String bigDepartmentName;
}

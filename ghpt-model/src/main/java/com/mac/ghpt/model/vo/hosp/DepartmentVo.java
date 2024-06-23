package com.mac.ghpt.model.vo.hosp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月14日, 13:31:15
 */
@Data
@Schema(description = "封装科室数据")
public class DepartmentVo {

    private String departmentCode;
    private String departmentName;
    @Schema(description = "子节点")
    private List<DepartmentVo> children;
}
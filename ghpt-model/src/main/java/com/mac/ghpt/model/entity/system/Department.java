package com.mac.ghpt.model.entity.system;

import com.mac.ghpt.model.entity.base.BaseMongoEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月13日, 11:25:53
 */
@Data
@Document("Department")
public class Department extends BaseMongoEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "医院编号")
    private String hospitalCode;

    @Schema(description = "科室编号")
    private String departmentCode;

    @Schema(description = "科室名称")
    private String departmentName;

    @Schema(description = "大科室编号")
    private String bigDepartmentCode;

    @Schema(description = "大科室名称")
    private String bigDepartmentName;

    @Schema(description = "科室描述")
    private String intro;
}

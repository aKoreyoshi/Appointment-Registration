package com.mac.ghpt.model.entity.system;

import com.mac.ghpt.model.entity.base.BaseMongoEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月27日, 14:07:45
 */
@Data
@Document("DoctorSet")
public class DoctorSet extends BaseMongoEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "医院编号")
    private String hospitalCode;

    @Schema(description = "医生编号")
    private String doctorId;

    @Schema(description = "姓名")
    private String doctorName;

    @Schema(description = "职称")
    private String doctorTitle;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "技能")
    private String doctorSkill;

    @Schema(description = "联系电话")
    private String doctorPhone;

    @Schema(description = "科室编号")
    private String departmentCode;

    @Schema(description = "科室名称")
    private String departmentName;

}
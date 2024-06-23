package com.mac.ghpt.model.entity.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mac.ghpt.model.entity.base.BaseMongoEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.sql.Date;


/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月11日, 16:14:56
 */
@Data
@Document("Schedule")
public class Schedule extends BaseMongoEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "排班编号")
    @Indexed
    private String scheduleId;

    @Schema(description = "医院编号")
    @Indexed
    private String hospitalCode;

    @Schema(description = "科室编号")
    @Indexed
    private String departmentCode;

    @Schema(description = "医生职称")
    private String doctorTitle;

    @Schema(description = "医生名字")
    private String doctorName;

    @Schema(description = "医生技能")
    private String doctorSkill;

    @Schema(description = "排班日期")
    // @JsonFormat(pattern = "yyyy-MM-dd")
    private String workDate;

    @Schema(description = "排班时间（0：上午 1：下午）")
    private Integer workTime;

    @Schema(description = "可预约数")
    private Integer reservedNumber;

    @Schema(description = "剩余预约数")
    private Integer availableNumber;

    @Schema(description = "挂号费用")
    private BigDecimal registrationFee;

    @Schema(description = "状态（-1：停诊 0：停约 1：可约）")
    private Integer status;


}

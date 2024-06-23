package com.mac.ghpt.model.dto.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;



/**
 * @author: 马聪
 * @function: 条件查询排班参数
 * @version: 1.0
 * @date: 2024年03月11日, 20:15:10
 */
@Data
@Schema(description = "条件查询排班参数实体")
public class ScheduleDto {
    private String doctorName;  // 医生名字
    // @JsonFormat(pattern = "yyyy-MM-dd")
    private String workDate;  // 排班日期
    private Integer workTime;  // 上午或下午
}

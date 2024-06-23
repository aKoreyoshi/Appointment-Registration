package com.mac.ghpt.model.vo.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月17日, 15:54:36
 */
@Data
public class ScheduleOrderVo {

    private String hosptialName;  // 医院名称
    private String departmentCode;  // 科室编号
    private String departmentName;  // 科室名称
    private String scheduleId;  // 排班id
    private String doctorName;  // 医生姓名
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date workDate;  // 工作日期
    private Integer workTime;  // 工作时间 0上午 1下午
    private BigDecimal amount;  // 挂号费用
    private Date quitTime;  // 退号时间
}
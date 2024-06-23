package com.mac.ghpt.model.entity.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mac.ghpt.model.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月17日, 12:28:41
 */
@Data
@TableName("order_info")
public class OrderInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "订单编号")
    @TableField("order_no")
    private String orderNo;

    @Schema(description = "医院名称")
    @TableField("hospital_name")
    private String hosptialName;

    @Schema(description = "科室编号")
    @TableField("department_code")
    private String departmentCode;

    @Schema(description = "科室名称")
    @TableField("department_name")
    private String departmentName;

    @Schema(description = "排班id")
    @TableField("schedule_id")
    private String scheduleId;

    @Schema(description = "医生姓名")
    @TableField("doctor_name")
    private String doctorName;

    @Schema(description = "工作日期")
    @TableField("work_date")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date workDate;

    @Schema(description = "工作时间")
    @TableField("work_time")
    private Integer workTime;

    @Schema(description = "就诊人id")
    @TableField("patient_id")
    private String patientId;

    @Schema(description = "就诊人姓名")
    @TableField("patient_name")
    private String patientName;

    @Schema(description = "就诊人电话")
    @TableField("patient_phone")
    private String patientPhone;

    @Schema(description = "预约号序")
    @TableField("number")
    private Integer number;

    @Schema(description = "取号地点")
    @TableField("fetch_address")
    private String fetchAddress;

    @Schema(description = "挂号费用")
    @TableField("amount")
    private BigDecimal amount;

    @Schema(description = "退号时间")
    @TableField("quit_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date quitTime;

    @Schema(description = "订单状态")
    @TableField("status")
    private Integer status;

    @Schema(description = "其他参数")
    @TableField(exist = false)
    private Map<String, Object> param = new HashMap<>();
}
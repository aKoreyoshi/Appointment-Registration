package com.mac.ghpt.model.vo.order;

import com.mac.ghpt.model.vo.sms.SmsVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月17日, 20:28:27
 */
@Data
public class OrderMqVo {
    @Schema(description = "可预约数")
    private Integer reservedNumber;

    @Schema(description = "剩余预约数")
    private Integer availableNumber;

    @Schema(description = "排班id")
    private String scheduleId;

    @Schema(description = "短信实体")
    private SmsVo smsVo;
}
package com.mac.ghpt.model.vo.sms;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月17日, 18:40:01
 */
@Data
@Schema(description = "短信发送参数")
public class SmsVo {
    @Schema(description = "phone")
    private String phone;
    @Schema(description = "短信模板code")
    private String templateCode;
    @Schema(description = "短信模板参数")
    private Map<String,Object> param;
}
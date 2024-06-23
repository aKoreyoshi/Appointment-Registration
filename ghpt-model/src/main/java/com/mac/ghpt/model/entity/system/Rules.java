package com.mac.ghpt.model.entity.system;


import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月08日, 18:55:45
 */
@Data
// @TableName("booking_rule")
@Document("Rules")
public class Rules {

    // @Schema(description = "医院编号")
    // @TableField("hospital_code")
    // private String hospitalCode;

    @Schema(description = "预约周期")
    // @TableField("cycle")
    private Integer cycle;

    @Schema(description = "放号时间")
    // @TableField("release_time")
    private String releaseTime;

    @Schema(description = "停挂时间")
    // @TableField("stop_time")
    private String stopTime;

    @Schema(description = "退号日期（如：就诊前一天为-1，当天为0）")
    // @TableField("quit_day")
    private Integer quitDay;

    @Schema(description = "退号时间")
    // @TableField("quit_time")
    private String quitTime;

    @Schema(description = "取号规则")
    // @TableField("rule")
    private List<String> rule;

    public void setRule(String rule) {
        if (!StringUtils.isEmpty(rule)) {
            this.rule = JSONArray.parseArray(rule, String.class);
        }
    }

}

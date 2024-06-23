package com.mac.ghpt.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mac.ghpt.model.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月18日, 14:36:09
 */
@Data
@Schema(description = "预约规则")
@TableName("booking_rule")
public class BookingRule extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "医院编号")
    @TableField("hospital_code")
    private String hospitalCode;

    @Schema(description = "预约周期")
    @TableField("cycle")
    private Integer cycle;

    @Schema(description = "放号时间")
    @TableField("release_time")
    private String releaseTime;

    @Schema(description = "停挂时间")
    @TableField("stop_time")
    private String stopTime;

    @Schema(description = "退号日期（如：就诊前一天为-1，当天为0）")
    @TableField("quit_day")
    private Integer quitDay;

    @Schema(description = "退号时间")
    @TableField("quit_time")
    private String quitTime;

    @Schema(description = "取号规则")
    @TableField("rule")
    private String rule;
}

package com.mac.ghpt.model.entity.doctor;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mac.ghpt.model.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月27日, 18:13:11
 */
@Data
@TableName("doctor")
public class Doctor extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Schema(description = "医生编号")
    @TableField("doctor_id")
    private String doctorId;

    @Schema(description = "医生姓名")
    @TableField("doctor_name")
    private String doctorName;

    @Schema(description = "登录密码")
    @TableField("password")
    private String password;

    @Schema(description = "性别")
    @TableField("gender")
    private Integer gender;

    @Schema(description = "联系电话")
    @TableField("doctor_phone")
    private String doctorPhone;

    @Schema(description = "技能")
    @TableField("doctor_skill")
    private String doctorSkill;
}
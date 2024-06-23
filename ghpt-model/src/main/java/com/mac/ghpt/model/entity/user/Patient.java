package com.mac.ghpt.model.entity.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mac.ghpt.model.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月12日, 12:32:23
 */
@Data
@TableName("patient")
public class Patient extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "就诊人姓名")
    @TableField("name")
    private String name;

    @Schema(description = "就诊人手机号")
    @TableField("phone")
    private String phone;

    @Schema(description = "性别")
    @TableField("gender")
    private Integer gender;

    @Schema(description = "出生日期")
    @TableField("birthday")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date birthday;

    @Schema(description = "证件类型")
    @TableField("certificate_type")
    private String certificateType;

    @Schema(description = "证件号")
    @TableField("certificate_no")
    private String certificateNo;

    @Schema(description = "民族")
    @TableField("ethnic_group")
    private String ethnicGroup;

    @Schema(description = "地区")
    @TableField("district")
    private String district;

    @Schema(description = "详细地址")
    @TableField("address")
    private String address;

    @Schema(description = "联系人姓名")
    @TableField("contact_name")
    private String contactName;

    @Schema(description = "联系人电话")
    @TableField("contact_phone")
    private String contactPhone;
}
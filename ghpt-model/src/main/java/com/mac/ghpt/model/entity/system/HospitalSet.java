package com.mac.ghpt.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mac.ghpt.model.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: 马聪
 * @function: 医院相关信息
 * @version: 1.0
 * @date: 2024年03月08日, 17:29:13
 */
@Data
@TableName("hospital_set")
public class HospitalSet extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "医院编号")
    @TableField("hospital_code")
    private String hospitalCode;

    @Schema(description = "医院名称")
    @TableField("hospital_name")
    private String hospitalName;

    @Schema(description = "医院等级")
    @TableField("hospital_grade")
    private String hospitalGrade;

    @Schema(description = "医院图标")
    @TableField("logoData")
    private String logoData;

    @Schema(description = "医院状态")
    @TableField("status")
    private Integer status;

    @Schema(description = "医院地址")
    @TableField("address")
    private String address;

    @Schema(description = "医院电话")
    @TableField("phone")
    private String phone;

    @Schema(description = "医院介绍")
    @TableField("intro")
    private String intro;

    @Schema(description = "路线")
    @TableField("route")
    private String route;

}

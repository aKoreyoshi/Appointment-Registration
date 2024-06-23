package com.mac.ghpt.model.entity.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mac.ghpt.model.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月05日, 14:53:37
 */
@Data
@TableName("user_info")
public class UserInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "姓名")
    @TableField("name")
    private String name;

    @Schema(description = "手机号")
    @TableField("phone")
    private String phone;

    @Schema(description = "证件类型")
    @TableField("certificate_type")
    private String certificateType;

    @Schema(description = "证件号")
    @TableField("certificate_no")
    private String certificateNo;

    @Schema(description = "证件地址")
    @TableField("certificate_url")
    private String certificateUrl;

    @Schema(description = "账号状态")
    @TableField("status")
    private Integer status;

    @Schema(description = "认证状态")
    @TableField("auth_status")
    private Integer authStatus;

}
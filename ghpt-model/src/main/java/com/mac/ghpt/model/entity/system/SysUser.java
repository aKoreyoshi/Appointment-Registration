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
 * @date: 2024年03月01日, 15:41:30
 */
@Data
@Schema(description = "系统用户实体类")
public class SysUser extends BaseEntity {

    @Schema(description = "用户名")
    @TableField(value = "username")
    private String userName;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "账号状态,0:正常  1:停用")
    private Integer status;
}

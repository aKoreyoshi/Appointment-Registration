package com.mac.ghpt.model.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * @author: 马聪
 * @function: vo是用来封装数据  dto用来接收前端传递的数据
 * @version: 1.0
 * @date: 2024年03月01日, 15:28:22
 */
@Data
public class BaseEntity implements Serializable {


    @Schema(description = "唯一标识id")
    private Long id;

    @Schema(description = "创建时间")
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Schema(description = "修改时间")
    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @Schema(description = "是否删除 0：正常  1：删除")
    @TableField("is_deleted")

    private Integer isDeleted;


}

package com.mac.ghpt.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mac.ghpt.model.entity.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月14日, 10:58:54
 */
@Data
@TableName("dict")
public class Dict extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "父id")
    @TableField("parent_id")
    private Long parentId;

    @Schema(description = "名称")
    @TableField("name")
    private String name;

    @Schema(description = "值")
    @TableField("value")
    private Long value;

    @Schema(description = "编码")
    @TableField("dict_code")
    private String dictCode;

    // @Schema(description = "其他参数")
    // @TableField(exist = false)
    // private Map<String, Object> param = new HashMap<>();

    @Schema(description = "子节点")
    @TableField(exist = false)
    private List<Dict> children;
}

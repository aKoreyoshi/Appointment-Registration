package com.mac.ghpt.model.dto.system;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月14日, 13:26:15
 */
@Data
public class DictDto {

    @ExcelProperty(index = 0)
    @Schema(description = "id")
    private Long id;

    @ExcelProperty(index = 1)
    @Schema(description = "父id")
    private Long parentId;

    @ExcelProperty(index = 2)
    @Schema(description = "名称")
    private String name;

    @ExcelProperty(index = 3)
    @Schema(description = "值")
    private Long value;

    @ExcelProperty(index = 4)
    @Schema(description = "编码")
    private String dictCode;
}

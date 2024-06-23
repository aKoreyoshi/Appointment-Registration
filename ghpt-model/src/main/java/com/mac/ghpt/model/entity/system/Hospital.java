package com.mac.ghpt.model.entity.system;

import com.alibaba.fastjson2.JSONObject;
import com.mac.ghpt.model.entity.base.BaseMongoEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author: 马聪
 * @function: 医院各种详细信息（包含预约规则）
 * @version: 1.0
 * @date: 2024年03月09日, 15:22:46
 */
@Data
@Document("Hospital")
public class Hospital extends BaseMongoEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "医院编号")
    @Indexed(unique = true) //唯一索引
    private String hospitalCode;

    @Schema(description = "医院名称")
    private String hospitalName;

    @Schema(description = "医院等级")
    private String hospitalGrade;

    @Schema(description = "医院图标")
    private String logoData;

    @Schema(description = "医院状态")
    private Integer status;

    @Schema(description = "医院地址")
    private String address;

    @Schema(description = "医院电话")
    private String phone;

    @Schema(description = "医院介绍")
    private String intro;

    @Schema(description = "路线")
    private String route;

    @Schema(description = "预约规则")
    private Rules rules;

    public void setRules(String rules) {
        this.rules = JSONObject.parseObject(rules, Rules.class);
    }

}

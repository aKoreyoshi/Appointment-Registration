package com.mac.ghpt.model.vo.hosp;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月08日, 11:32:08
 */
@Data
public class HostAddressVo {
    @Schema(description = "ip地址")
    private String ip;

    @Schema(description = "省份")
    @JsonProperty("pro")  // json格式中数据的名称
    private  String province;

    @Schema(description = "省份code")
    @JsonProperty("proCode")
    private String provinceCode;

    @Schema(description = "城市")
    @JsonProperty("city")
    private String city;

    @Schema(description = "城市code")
    @JsonProperty("cityCode")
    private String cityCode;

    @Schema(description = "区")
    @JsonProperty("region")
    private String region;

    @Schema(description = "区code")
    @JsonProperty("regionCode")
    private String regionCode;

    @Schema(description = "详细地址")
    @JsonProperty("addr")
    private String address;

    @Schema(description = "区名称")
    @JsonProperty("regionNames")
    private String regionNames;

    @Schema(description = "错误信息")
    @JsonProperty("err")
    private String err;

}

package com.mac.ghpt.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月08日, 11:43:20
 */
@Data
public class WeatherVo {

    @Schema(description = "城市")
    @JsonProperty("city")
    private String city;

    @Schema(description = "夜间气温")
    @JsonProperty("night_air_temperature")
    private String nightTemperature;

    @Schema(description = "白天气温")
    @JsonProperty("day_air_temperature")
    private String dayTemperature;

    @Schema(description = "天气")
    @JsonProperty("weather")
    private String weather;


    @Schema(description = "风向")
    @JsonProperty("wind_direction")
    private String windDirection;
}

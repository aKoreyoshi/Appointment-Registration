package com.mac.ghpt.hosp.controller;

import com.mac.ghpt.hosp.service.HospService;
import com.mac.ghpt.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月13日, 17:18:32
 */
@Tag(name = "医院管理")
@RestController
@RequestMapping("/api/hosp")
public class HospController {

    private HospService hospService;

    @Autowired
    public HospController(HospService hospService) {
        this.hospService = hospService;
    }

    @GetMapping("/getHospInfo")
    public Result getHospInfo() {
        Map<String, Object> map = hospService.getHospInfo();
        return Result.ok(map);
    }

    @GetMapping("/getWeather")
    public Result getWeather() {
        Result weather = hospService.getWeather();
        return weather;
    }
}
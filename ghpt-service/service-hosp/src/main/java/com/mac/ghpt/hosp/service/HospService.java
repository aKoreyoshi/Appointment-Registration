package com.mac.ghpt.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mac.ghpt.model.entity.system.HospitalSet;
import com.mac.ghpt.result.Result;

import java.util.Map;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月13日, 17:28:21
 */
public interface HospService extends IService<HospitalSet> {
    Map<String, Object> getHospInfo();

    Result getWeather();
}
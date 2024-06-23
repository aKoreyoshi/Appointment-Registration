package com.mac.ghpt.hosp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mac.ghpt.hosp.mapper.HospMapper;
import com.mac.ghpt.hosp.service.HospService;
import com.mac.ghpt.manage.client.HospFeignClient;
import com.mac.ghpt.model.entity.system.HospitalSet;
import com.mac.ghpt.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月13日, 17:29:15
 */
@Service
public class HospServiceImpl extends ServiceImpl<HospMapper, HospitalSet> implements HospService {

    private HospFeignClient hospFeignClient;
    @Autowired
    public HospServiceImpl(HospFeignClient hospFeignClient) {
        this.hospFeignClient = hospFeignClient;
    }
    @Override
    public Map<String, Object> getHospInfo() {
        Map<String, Object> hospitalInfo = hospFeignClient.getHospitalInfo();
        return hospitalInfo;
    }

    @Override
    public Result getWeather() {
        Result weather = hospFeignClient.getWeather();
        return weather;
    }
}
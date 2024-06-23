package com.mac.ghpt.manage.service;


import com.mac.ghpt.model.dto.system.HospitalStatusDto;

import java.util.Map;


/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年03月08日, 17:28:18
 */
public interface HospitalService {
    // 添加医院信息
    // void saveHospital(Map<String, Object> paramMap);
    void saveHospital(String filePath);

    // 获取医院信息
    Map<String, Object> getHospitalInfo();

    // 修改医院状态
    void updateHospStatus(HospitalStatusDto hospitalStatusDto);
}

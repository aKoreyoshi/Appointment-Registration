package com.mac.ghpt.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mac.ghpt.model.dto.user.PatientDto;
import com.mac.ghpt.model.entity.user.Patient;

import java.util.List;
import java.util.Map;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年04月12日, 12:38:54
 */
public interface PatientService extends IService<Patient> {
    // 添加就诊人
    void savePatient(Long userId, PatientDto patientDto);

    // 修改
    void updatePatient(Patient patient);

    // 根据id删除
    void removePatient(Long id);

    // 查询就诊人列表
    Map<String, Object> getPatientList(Long userId);

    Map<String, Object> getDictData();

    // 根据id获取就诊人信息
    Map<String, Object> getPatientById(Long id);
    Patient getPatient(Long id);

    List<Patient> getPatientListByName(List<String> nameList);
}
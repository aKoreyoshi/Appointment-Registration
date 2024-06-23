package com.mac.ghpt.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mac.ghpt.manage.client.HospFeignClient;
import com.mac.ghpt.model.dto.user.PatientDto;
import com.mac.ghpt.model.entity.system.Dict;
import com.mac.ghpt.model.entity.user.Patient;
import com.mac.ghpt.model.enums.DictEnum;
import com.mac.ghpt.user.mapper.PatientMapper;
import com.mac.ghpt.user.service.PatientService;
import com.mac.ghpt.utils.RandomIdGeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月12日, 12:39:18
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Autowired
    private HospFeignClient hospFeignClient;
    @Autowired
    private PatientMapper patientMapper;

    /**
     * 添加就诊人
     *
     * @param userId
     * @param patientDto
     */
    @Override
    public void savePatient(Long userId, PatientDto patientDto) {
        Patient patient = new Patient();
        BeanUtil.copyProperties(patientDto, patient);
        // 这里有一个问题就是前端传递的地区是一个数组，我们要把他转为一个字符串
        String[] district = patientDto.getDistrict();
        String districtStr = Arrays.stream(district).collect(Collectors.joining(""));
        // 为patient添加数据
        patient.setId(RandomIdGeneratorUtil.generateRandomId());
        patient.setName(patientDto.getName());
        patient.setPhone(patientDto.getPhone());
        patient.setGender(patientDto.getGender());
        patient.setBirthday(patientDto.getBirthday());
        patient.setCertificateType(patientDto.getCertificateType());
        patient.setCertificateNo(patientDto.getCertificateNo());
        patient.setEthnicGroup(patientDto.getEthnicGroup());
        patient.setDistrict(districtStr);
        patient.setAddress(patientDto.getAddress());
        patient.setContactName(patientDto.getContactName());
        patient.setContactPhone(patientDto.getContactPhone());
        // 设置其他值
        patient.setUserId(userId);
        patient.setCreateTime(new Date());
        patient.setUpdateTime(new Date());
        patient.setIsDeleted(0);
        // 添加
        baseMapper.insert(patient);
    }

    /**
     * 修改就诊人信息
     * @param patient
     */
    @Override
    public void updatePatient(Patient patient) {
        // 设置其他值
        patient.setUpdateTime(new Date());
        baseMapper.updateById(patient);
    }

    /**
     * 根据id删除就诊人
     *
     * @param id
     */
    @Override
    public void removePatient(Long id) {
        baseMapper.deleteById(id);
    }

    /**
     * 查询就诊人列表
     * @param userId
     * @return
     */
    @Override
    public Map<String, Object> getPatientList(Long userId) {
        Map<String, Object> map = new HashMap<>();
        LambdaQueryWrapper<Patient> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Patient::getUserId, userId);
        List<Patient> patients = baseMapper.selectList(wrapper);
        patients = this.packageAddress(patients);
        map.put("patients", patients);
        return map;
    }

    @Override
    public Map<String, Object> getPatientById(Long id) {
        Map<String, Object> map = new HashMap<>();
        Patient patient = baseMapper.selectById(id);
        map.put("patient", patient);
        return map;
    }

    @Override
    public Patient getPatient(Long id) {
        Patient patient = baseMapper.selectById(id);
        return patient;
    }

    @Override
    public List<Patient> getPatientListByName(List<String> nameList) {
        List<Patient> patients = baseMapper.selectBatchNames(nameList);
        return patients;
    }

    // 封装一下地址信息
    private List<Patient> packageAddress(List<Patient> patients) {
        patients.stream().forEach(patient -> {
            String newAddress = patient.getDistrict() + patient.getAddress();
            patient.setAddress(newAddress);
        });
        return patients;
    }

    @Override
    public Map<String, Object> getDictData() {
        HashMap<String, Object> map = new HashMap<>();
        // 获取地区
        List<Dict> districts = hospFeignClient.findChildrenByDictCode(DictEnum.PROVINCE.getDictCode());
        map.put("districts", districts);
        // 获取民族
        List<Dict> ethnicGroups = hospFeignClient.findChildrenByDictCode(DictEnum.NATION.getDictCode());
        map.put("ethnicGroups",ethnicGroups);
        return map;
    }

}
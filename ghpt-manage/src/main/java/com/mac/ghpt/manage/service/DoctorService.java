package com.mac.ghpt.manage.service;

import com.mac.ghpt.model.dto.system.DoctorDto;
import com.mac.ghpt.model.entity.system.Department;
import com.mac.ghpt.model.entity.system.DoctorSet;
import org.springframework.data.domain.Page;


/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年04月27日, 14:17:19
 */
public interface DoctorService {
    // 上传科室数据
    void saveDoctor(String filePath);

    // 条件分页查询科室
    Page<DoctorSet> selectPage(Integer currentPage, Integer pageSize, DoctorDto doctorDto);

    // 根据id删除科室
    void deleteDoctorById(String id);

}
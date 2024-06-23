package com.mac.ghpt.manage.service;

import com.mac.ghpt.model.dto.system.DepartmentDto;
import com.mac.ghpt.model.entity.system.Department;
import com.mac.ghpt.model.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年03月13日, 11:45:25
 */
public interface DepartmentService {
    // 上传科室数据
    void saveDepartment(String filePath);

    // 条件分页查询科室
    Page<Department> selectPage(Integer currentPage, Integer pageSize, DepartmentDto departmentDto);

    // 根据id删除科室
    void deleteDepartmentById(String id);

    // 获取科室列表
    List<DepartmentVo> getDepartmentList();

    // 根据医院编号和科室编号获取科室
    Department getByHospitalCodeAndDepartmentCode(String hospitalCode, String departmentCode);

}

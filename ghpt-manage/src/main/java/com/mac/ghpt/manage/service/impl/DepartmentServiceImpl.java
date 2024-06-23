package com.mac.ghpt.manage.service.impl;

import com.alibaba.fastjson2.JSON;
import com.mac.ghpt.manage.repository.DepartmentRepository;
import com.mac.ghpt.manage.service.DepartmentService;
import com.mac.ghpt.model.dto.system.DepartmentDto;
import com.mac.ghpt.model.entity.system.Department;
import com.mac.ghpt.model.vo.hosp.DepartmentVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月13日, 11:45:42
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    DepartmentRepository departmentRepository;

    /**
     * 添加科室信息
     * @param filePath
     */
    @Override
    public void saveDepartment(String filePath) {
        Path path = Path.of(filePath);
        try {
            String jsonString = Files.readString(path);
            // 将json字符串转为对象
            List<Department> departments = JSON.parseArray(jsonString, Department.class);
            departments.stream().forEach(department -> {
                // 根据医院编号和科室编号查询是否存在
                Department departmentExist = departmentRepository.findByHospitalCodeAndDepartmentCode(
                        department.getHospitalCode(), department.getDepartmentCode());
                if (departmentExist != null) {
                    // 存在，修改相关信息
                    departmentExist.setUpdateTime(new Date());
                    departmentExist.setIsDeleted(0);
                    departmentRepository.save(departmentExist);
                } else {
                    // 不存在，进行添加
                    department.setCreateTime(new Date());
                    department.setUpdateTime(new Date());
                    department.setIsDeleted(0);
                    departmentRepository.save(department);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 条件分页查询科室
     * @param currentPage
     * @param pageSize
     * @param departmentDto
     * @return
     */
    @Override
    public Page<Department> selectPage(Integer currentPage, Integer pageSize, DepartmentDto departmentDto) {
        PageRequest pageRequest = PageRequest.of(currentPage - 1, pageSize);
        // 创建匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true)
                .withIgnoreNullValues();

        // 封装参数
        Department department = new Department();
        if (!StringUtils.isEmpty(departmentDto.getDepartmentName())) {
            department.setDepartmentName(departmentDto.getDepartmentName());
        }
        if (!StringUtils.isEmpty(departmentDto.getBigDepartmentName())) {
            department.setBigDepartmentName(departmentDto.getBigDepartmentName());
        }

        // 封装查询条件
        Example<Department> example = Example.of(department, matcher);
        Page<Department> departmentPage = departmentRepository.findAll(example, pageRequest);
        return departmentPage;
    }

    /**
     * 根据id删除科室
     * @param id
     */
    @Override
    public void deleteDepartmentById(String id) {
        departmentRepository.deleteById(id);
    }

    @Override
    public List<DepartmentVo> getDepartmentList() {
        //创建List集合，用来最终的数据封装
        ArrayList<DepartmentVo> result = new ArrayList<>();
        // 获取所有科室信息
        List<Department> departments = departmentRepository.findAll();
        // 根据大科室code进行分组，将每个大科室对应的小科室放到同一个分组中
        Map<String, List<Department>> departmentMap = departments.stream()
                .collect(Collectors.groupingBy(Department::getBigDepartmentCode));

        // 遍历大科室
        for (Map.Entry<String, List<Department>> entry : departmentMap.entrySet()) {
            // 拿到大科室编号
            String bigDepartmentCode = entry.getKey();
            // 大科室对应的子科室
            List<Department> childDepartmentList = entry.getValue();
            DepartmentVo departmentVo = new DepartmentVo();
            // 封装数据
            departmentVo.setDepartmentCode(bigDepartmentCode);
            departmentVo.setDepartmentName(childDepartmentList.get(0).getBigDepartmentName());

            // 封装子科室
            ArrayList<DepartmentVo> children = new ArrayList<>();
            for (Department childDepartment : childDepartmentList) {
                DepartmentVo child = new DepartmentVo();
                child.setDepartmentCode(childDepartment.getDepartmentCode());
                child.setDepartmentName(childDepartment.getDepartmentName());
                children.add(child);
            }
            departmentVo.setChildren(children);
            // 添加到大科室列表
            result.add(departmentVo);
        }
        return result;
    }

    @Override
    public Department getByHospitalCodeAndDepartmentCode(String hospitalCode, String departmentCode) {
        Department department = departmentRepository.
                findByHospitalCodeAndDepartmentCode(hospitalCode, departmentCode);
        return department;
    }


}

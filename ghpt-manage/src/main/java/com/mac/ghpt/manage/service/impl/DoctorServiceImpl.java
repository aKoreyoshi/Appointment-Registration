package com.mac.ghpt.manage.service.impl;

import com.alibaba.fastjson2.JSON;
import com.mac.ghpt.manage.repository.DoctorRepository;
import com.mac.ghpt.manage.service.DoctorService;
import com.mac.ghpt.model.dto.system.DoctorDto;
import com.mac.ghpt.model.entity.system.Department;
import com.mac.ghpt.model.entity.system.DoctorSet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月27日, 14:20:53
 */
@Service
public class DoctorServiceImpl implements DoctorService {

    private DoctorRepository doctorRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public void saveDoctor(String filePath) {
        Path path = Path.of(filePath);
        try {
            String jsonString = Files.readString(path);
            // 将json字符串转为对象
            List<DoctorSet> doctorDtos = JSON.parseArray(jsonString, DoctorSet.class);
            doctorDtos.stream().forEach(doctorSet -> {
                // 根据医生编号查询是否存在
                DoctorSet doctorSetExist = doctorRepository.findByDoctorId(doctorSet.getDoctorId());
                if (doctorSetExist != null) {
                    // 存在，修改相关信息
                    doctorSetExist.setUpdateTime(new Date());
                    doctorSetExist.setIsDeleted(0);
                    doctorRepository.save(doctorSetExist);
                } else {
                    // 不存在，进行添加
                    doctorSet.setCreateTime(new Date());
                    doctorSet.setUpdateTime(new Date());
                    doctorSet.setIsDeleted(0);
                    doctorRepository.save(doctorSet);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Page<DoctorSet> selectPage(Integer currentPage, Integer pageSize, DoctorDto doctorDto) {
        PageRequest pageRequest = PageRequest.of(currentPage - 1, pageSize);
        // 创建匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true)
                .withIgnoreNullValues();

        // 封装参数
        DoctorSet doctorSet = new DoctorSet();
        if (!StringUtils.isEmpty(doctorDto.getDoctorId())) {
            doctorSet.setDoctorId(doctorDto.getDoctorId());
        }
        if (!StringUtils.isEmpty(doctorDto.getDoctorName())) {
            doctorSet.setDoctorName(doctorDto.getDoctorName());
        }
        if (!StringUtils.isEmpty(doctorDto.getDepartmentName())) {
            doctorSet.setDepartmentName(doctorDto.getDepartmentName());
        }

        // 封装查询条件
        Example<DoctorSet> example = Example.of(doctorSet, matcher);
        Page<DoctorSet> doctorPage = doctorRepository.findAll(example, pageRequest);
        return doctorPage;
    }

    @Override
    public void deleteDoctorById(String id) {
        doctorRepository.deleteById(id);
    }
}
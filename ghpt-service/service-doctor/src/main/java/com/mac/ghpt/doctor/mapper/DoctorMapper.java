package com.mac.ghpt.doctor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mac.ghpt.model.entity.doctor.Doctor;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年04月27日, 18:12:30
 */
@Mapper
public interface DoctorMapper extends BaseMapper<Doctor> {
}
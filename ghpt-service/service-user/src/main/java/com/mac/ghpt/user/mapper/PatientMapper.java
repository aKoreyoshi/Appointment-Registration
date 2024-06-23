package com.mac.ghpt.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mac.ghpt.model.entity.user.Patient;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年04月12日, 12:39:35
 */
@Mapper
public interface PatientMapper extends BaseMapper<Patient> {
    List<Patient> selectBatchNames(List<String> nameList);
}
package com.mac.ghpt.hosp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mac.ghpt.model.entity.system.HospitalSet;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年04月13日, 17:29:30
 */
@Mapper
public interface HospMapper extends BaseMapper<HospitalSet> {
}
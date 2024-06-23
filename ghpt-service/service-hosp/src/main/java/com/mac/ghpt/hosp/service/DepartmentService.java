package com.mac.ghpt.hosp.service;

import com.mac.ghpt.model.vo.hosp.DepartmentVo;

import java.util.List;
import java.util.Map;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年04月13日, 20:32:40
 */
public interface DepartmentService {
    List<DepartmentVo> getDepartmentList();
}
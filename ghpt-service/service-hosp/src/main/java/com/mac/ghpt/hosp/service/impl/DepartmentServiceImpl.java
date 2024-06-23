package com.mac.ghpt.hosp.service.impl;

import com.mac.ghpt.hosp.service.DepartmentService;
import com.mac.ghpt.manage.client.HospFeignClient;
import com.mac.ghpt.model.vo.hosp.DepartmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;


/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月13日, 20:32:59
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private HospFeignClient hospFeignClient;
    @Autowired
    public DepartmentServiceImpl(HospFeignClient hospFeignClient) {
        this.hospFeignClient = hospFeignClient;
    }

    @Override
    public List<DepartmentVo> getDepartmentList() {
        List<DepartmentVo> departmentList = hospFeignClient.getDepartmentList();
        return departmentList;
    }
}
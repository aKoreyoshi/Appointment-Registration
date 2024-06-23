package com.mac.ghpt.hosp.controller;

import com.mac.ghpt.hosp.service.DepartmentService;
import com.mac.ghpt.model.vo.hosp.DepartmentVo;
import com.mac.ghpt.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月13日, 20:33:23
 */
@Tag(name = "科室管理")
@RestController
@RequestMapping("/api/hosp/department")
public class DepartmentController {

    private DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Operation(description = "获取科室列表")
    @GetMapping("/getDepartmentList")
    public Result getDepartmentList(){
        List<DepartmentVo> departmentVos = departmentService.getDepartmentList();
        return Result.ok(departmentVos);
    }
}
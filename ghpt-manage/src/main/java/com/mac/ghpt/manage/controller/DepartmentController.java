package com.mac.ghpt.manage.controller;

import com.mac.ghpt.manage.service.DepartmentService;
import com.mac.ghpt.model.dto.system.DepartmentDto;
import com.mac.ghpt.model.entity.system.Department;
import com.mac.ghpt.model.vo.hosp.DepartmentVo;
import com.mac.ghpt.result.Result;
import com.mac.ghpt.utils.FileProcessingUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月13日, 11:49:03
 */
@Tag(name = "科室管理接口")
@RestController
@RequestMapping("/admin/hosp/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Operation(description = "上传科室信息")
    @PostMapping("/saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        try {
            Part filePart = request.getPart("file");
            // 获取文件名
            String fileName = filePart.getSubmittedFileName();
            // 保存文件到指定目录   Project was migrated to the new faster Maven import
            String path = "D:\\gdesign\\example" + "\\" + fileName;
            // 判断文件是否存在
            boolean fileExists = FileProcessingUtil.isFileExists(path);
            if (fileExists) {
                // 文件已存在，删除原本文件
                FileProcessingUtil.deleteFile(path);
                // 保存
                filePart.write(path);
            } else {
                // 文件不存在，直接保存
                filePart.write(path);
            }
            // 调用上传函数
            departmentService.saveDepartment(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    @Operation(description = "展示科室信息")
    @GetMapping("/getDepartmentList/{currentPage}/{pageSize}")
    public Result getDepartmentList(@PathVariable("currentPage") Integer currentPage,
                                    @PathVariable("pageSize") Integer pageSize,
                                    DepartmentDto departmentDto) {
        Page<Department> departmentPage = departmentService.selectPage(currentPage, pageSize, departmentDto);

        return Result.ok(departmentPage);
    }

    @Operation(description = "根据id删除科室信息")
    @DeleteMapping("/deleteDepartmentById/{id}")
    public Result deleteDepartmentById(@PathVariable("id") String id) {
        departmentService.deleteDepartmentById(id);
        return Result.ok();
    }


    @Operation(description = "获取科室列表")
    @GetMapping("/getDepList")
    public List<DepartmentVo> getDepartmentList() {
        List<DepartmentVo> departmentVos = departmentService.getDepartmentList();
        return departmentVos;
    }

    @Operation(description = "根据医院编号和科室编号获取科室")
    @GetMapping("/getDepartment/{hospitalCode}/{departmentCode}")
    public Department getDepartment(@PathVariable("hospitalCode") String hospitalCode,
                                    @PathVariable("departmentCode") String departmentCode) {
        Department department =
                departmentService.getByHospitalCodeAndDepartmentCode(hospitalCode, departmentCode);
        return department;
    }
}

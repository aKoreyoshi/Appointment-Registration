package com.mac.ghpt.manage.controller;

import com.mac.ghpt.manage.service.DoctorService;
import com.mac.ghpt.model.dto.system.DepartmentDto;
import com.mac.ghpt.model.dto.system.DoctorDto;
import com.mac.ghpt.model.entity.system.Department;
import com.mac.ghpt.model.entity.system.DoctorSet;
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

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月27日, 14:30:53
 */
@Tag(name = "医生管理")
@RestController
@RequestMapping("/admin/hosp/doctor")
public class DoctorController {
    private DoctorService doctorService;
    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @Operation(description = "上传科室信息")
    @PostMapping("/saveDoctor")
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
            doctorService.saveDoctor(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    @Operation(description = "展示科室信息")
    @GetMapping("/getDoctorList/{currentPage}/{pageSize}")
    public Result getDepartmentList(@PathVariable("currentPage") Integer currentPage,
                                    @PathVariable("pageSize") Integer pageSize,
                                    DoctorDto doctorDto) {
        Page<DoctorSet> doctorSets = doctorService.selectPage(currentPage, pageSize, doctorDto);

        return Result.ok(doctorSets);
    }

    @Operation(description = "根据id删除科室信息")
    @DeleteMapping("/deleteDoctorById/{id}")
    public Result deleteDepartmentById(@PathVariable("id") String id) {
        doctorService.deleteDoctorById(id);
        return Result.ok();
    }
}
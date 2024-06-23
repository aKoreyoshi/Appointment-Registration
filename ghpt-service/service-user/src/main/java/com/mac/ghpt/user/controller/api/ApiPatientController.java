package com.mac.ghpt.user.controller.api;

import com.mac.ghpt.model.dto.user.PatientDto;
import com.mac.ghpt.model.entity.user.Patient;
import com.mac.ghpt.result.Result;
import com.mac.ghpt.user.service.PatientService;
import com.mac.ghpt.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月12日, 12:40:40
 */
@Tag(name = "患者")
@RestController
@RequestMapping("/api/user/patient")
public class ApiPatientController {
    private PatientService patientService;

    @Autowired
    public ApiPatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(description = "添加就诊人")
    @PostMapping("/auth/savePatient")
    public Result savePatient(@RequestBody PatientDto patientDto, HttpServletRequest request) {
        // 根据request获取用户id
        Long userId = UserAuthUtil.getUserId(request);
        patientService.savePatient(userId, patientDto);
        return Result.ok();
    }

    @Operation(description = "修改就诊人信息")
    @PutMapping("/auth/updatePatient")
    public Result updatePatient(@RequestBody Patient patient) {
        patientService.updatePatient(patient);
        return Result.ok();
    }

    @Operation(description = "根据id查询就诊人信息")
    @GetMapping("/auth/getPatientById/{id}")
    public Result getPatientById(@PathVariable("id") Long id) {
        Map<String,Object> map = patientService.getPatientById(id);
        return Result.ok(map);
    }

    @Operation(description = "删除就诊人信息")
    @DeleteMapping("/auth/removePatient/{id}")
    public Result removePatient(@PathVariable("id") Long id) {
        patientService.removePatient(id);
        return Result.ok();
    }

    @Operation(description = "查询就诊人列表")
    @GetMapping("/auth/getPatientList")
    public Result getPatientList(HttpServletRequest request) {
        Long userId = UserAuthUtil.getUserId(request);
        Map<String, Object> map = patientService.getPatientList(userId);
        return Result.ok(map);
    }

    @Operation(description = "获取字典数据")
    @GetMapping("/getDictData")
    public Result getDictData() {
        Map<String, Object> map = patientService.getDictData();
        return Result.ok(map);
    }

    @Operation(description = "根据id获取返回patient对象")
    @GetMapping("/auth/getPatient/{id}")
    public Patient getPatientOrder(@PathVariable("id") Long id) {
        Patient patient = patientService.getPatient(id);
        return patient;
    }

}
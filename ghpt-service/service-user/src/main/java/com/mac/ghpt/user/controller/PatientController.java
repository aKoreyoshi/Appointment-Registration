package com.mac.ghpt.user.controller;

import com.mac.ghpt.model.entity.user.Patient;
import com.mac.ghpt.user.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月29日, 12:35:49
 */
@Tag(name = "医生端调用")
@RestController
@RequestMapping("/api/patient")
public class PatientController {
    private PatientService patientService;
    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(description = "根据姓名获取就诊人列表")
    @RequestMapping("/getPatientListByName")
    public List<Patient> getPatientListByName(@RequestBody List<String> patientNames) {
//    public List<Patient> getPatientListByName(List<String> patientNames) {
        // TODO 测试用户端
        System.out.println("patientNames: " + patientNames);
        return patientService.getPatientListByName(patientNames);
    }

    @Operation(description = "根据id获取详细信息")
    @GetMapping("/getById/{id}")
    public Patient getById(@PathVariable("id") Long id) {
        return patientService.getPatient(id);
    }
}
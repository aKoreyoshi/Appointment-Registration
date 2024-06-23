package com.mac.ghpt.user.client;

import com.mac.ghpt.model.dto.user.UserStatusDto;
import com.mac.ghpt.model.entity.user.Patient;
import com.mac.ghpt.model.entity.user.UserInfo;
import com.mac.ghpt.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年04月12日, 10:19:39
 */

@FeignClient(name = "service-user", url = "http://localhost:8082")
//@Repository
public interface UserFeignClient {

    // 获取用户列表
    @GetMapping("/admin/user/getUserList")
    List<UserInfo> getUserList();

    // 完成认证
    @PutMapping("/admin/user/authdUser/{phone}")
    void authdUser(@PathVariable("phone") String phone);

    // 更新用户状态
    @PutMapping("/admin/user/updateStatus")
    void updateStatus(@RequestBody UserStatusDto userStatusDto);

    // 根据patientid获取patient对象
    @GetMapping("/api/user/patient/auth/getPatient/{id}")
    Patient getPatientOrder(@PathVariable("id") Long id);

    // 批量获取就诊人信息
    @RequestMapping("/api/patient/getPatientListByName")
//    List<Patient> getPatientListByName(List<String> patientNames);
    List<Patient> getPatientListByName(@RequestBody List<String> patientNames);

    @GetMapping("/api/patient/getById/{id}")
    Patient getById(@PathVariable("id") Long id);
}
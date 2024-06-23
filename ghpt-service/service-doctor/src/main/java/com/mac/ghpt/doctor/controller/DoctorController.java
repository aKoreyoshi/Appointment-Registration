package com.mac.ghpt.doctor.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mac.ghpt.doctor.service.DoctorService;
import com.mac.ghpt.model.dto.doctor.DoctorLoginDto;
import com.mac.ghpt.model.dto.doctor.PatientQueryVo;
import com.mac.ghpt.model.entity.doctor.Doctor;
import com.mac.ghpt.model.entity.order.OrderInfo;
import com.mac.ghpt.model.entity.user.Patient;
import com.mac.ghpt.model.enums.OrderStatusEnum;
import com.mac.ghpt.model.vo.doctor.DoctorLoginVo;
import com.mac.ghpt.model.vo.order.OrderQueryVo;
import com.mac.ghpt.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月27日, 15:50:11
 */
@Tag(name = "医生后台系统")
@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    private DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @Operation(description = "判断账号是否合法")
    @PostMapping("/checkValidity/{doctorId}")
    public Result checkValidity(@PathVariable("doctorId") String doctorId) {
        Boolean isValidity = doctorService.checkValidity(doctorId);
        if (isValidity) return Result.ok();
        else return Result.build(244, "账号不合法");
    }

    @Operation(description = "判断账号是否被注册")
    @PostMapping("/checkIsExist/{doctorId}")
    public Result checkIsExist(@PathVariable("doctorId") String doctorId) {
        Boolean isExist = doctorService.checkIsExist(doctorId);
        if (isExist) return Result.build(245, "账号已存在");
        else return Result.ok();
    }

    @Operation(description = "医生注册")
    @PostMapping("/register")
    public Result register(@RequestBody DoctorLoginDto doctorLoginDto) {
        doctorService.registerUser(doctorLoginDto);
        return Result.ok();
    }

    @Operation(description = "登录")
    @PostMapping("/login")
    public Result login(@RequestBody DoctorLoginDto doctorLoginDto) {
        DoctorLoginVo doctorLoginVo = doctorService.login(doctorLoginDto);
        return Result.ok(doctorLoginVo);
    }

    @Operation(description = "获取医生登录信息")
    @GetMapping("/getDoctorInfo")
    public Result getUserInfo(@RequestHeader(name = "token") String token) {
        Doctor doctor = doctorService.getDoctorInfo(token);
        return Result.ok(doctor);
    }

    @Operation(description = "退出")
    @GetMapping("/logout")
    public Result logout(@RequestHeader(value = "token") String token) {
        doctorService.logout(token);
        return Result.ok();
    }

    @Operation(description = "获取订单列表")
    @GetMapping("/getPageList/{currentPage}/{pageSize}")
    public Result getPageList(@PathVariable("currentPage") Integer currentPage,
                              @PathVariable("pageSize") Integer pageSize,
                              OrderQueryVo orderQueryVo) {
        Map<String, Object> pageList = doctorService.getOrderList(currentPage, pageSize, orderQueryVo);
        return Result.ok(pageList);
    }

    @Operation(description = "获取状态信息列表")
    @GetMapping("/getStatusList")
    public Result getstatusList() {
        return Result.ok(OrderStatusEnum.getStatusList());
    }

    @Operation(description = "根据订单No获取订单详情")
    @GetMapping("/getOrderDetail/{orderNo}")
    public Result getOrderDetail(@PathVariable("orderNo") String orderNo) {
        OrderInfo orderInfoVo = doctorService.getOrderDetail(orderNo);
        return Result.ok(orderInfoVo);
    }

    @Operation(description = "条件分页查询就诊人信息")
    @PostMapping("/getPatientList/{currentPage}/{pageSize}")
    public Result getPatientList(@PathVariable("currentPage") Integer currentPage,
                                 @PathVariable("pageSize") Integer pageSize,
                                 @RequestBody PatientQueryVo patientQueryVo) {
        // TODO 测试患者姓名集合
        System.out.println("患者姓名集合：" + patientQueryVo);
        Page<Patient> page = new Page<>(currentPage, pageSize);
        IPage<Patient> iPage = doctorService.getPatientList(page, patientQueryVo);
        return Result.ok(iPage);
    }

    @Operation(description = "获取就诊人具体信息")
    @GetMapping("/getPatientDetail/{id}")
    public Result getPatientDetail(@PathVariable("id") Long id) {
        Patient patient = doctorService.getPatientDetail(id);
        return Result.ok(patient);
    }
}
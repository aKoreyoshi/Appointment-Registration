package com.mac.ghpt.doctor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mac.ghpt.model.dto.doctor.DoctorLoginDto;
import com.mac.ghpt.model.dto.doctor.PatientQueryVo;
import com.mac.ghpt.model.entity.doctor.Doctor;
import com.mac.ghpt.model.entity.order.OrderInfo;
import com.mac.ghpt.model.entity.user.Patient;
import com.mac.ghpt.model.vo.doctor.DoctorLoginVo;
import com.mac.ghpt.model.vo.order.OrderQueryVo;

import java.util.List;
import java.util.Map;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年04月27日, 18:12:01
 */
public interface DoctorService extends IService<Doctor> {
    // 注册
    void registerUser(DoctorLoginDto doctorLoginDto);

    // 判断账号输入是否合法
    Boolean checkValidity(String doctorId);

    // 判断账号是否注册
    Boolean checkIsExist(String doctorId);

    // 登录
    DoctorLoginVo login(DoctorLoginDto doctorLoginDto);

    // 获取登录信息
    Doctor getDoctorInfo(String token);

    // 退出
    void logout(String token);

    // 获取订单列表
    Map<String, Object> getOrderList(Integer currentPage, Integer pageSize, OrderQueryVo orderQueryVo);

    // 获取订单详情
    OrderInfo getOrderDetail(String orderNo);

    IPage<Patient> getPatientList(Page<Patient> page, PatientQueryVo patientQueryVo);

    Patient getPatientDetail(Long id);
}
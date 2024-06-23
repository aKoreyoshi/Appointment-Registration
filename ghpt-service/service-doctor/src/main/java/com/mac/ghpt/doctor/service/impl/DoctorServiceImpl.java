package com.mac.ghpt.doctor.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mac.ghpt.constant.Constants;
import com.mac.ghpt.doctor.mapper.DoctorMapper;
import com.mac.ghpt.doctor.repository.DoctorRepository;
import com.mac.ghpt.doctor.service.DoctorService;
import com.mac.ghpt.exception.GhptException;
import com.mac.ghpt.model.dto.doctor.DoctorLoginDto;
import com.mac.ghpt.model.dto.doctor.PatientQueryVo;
import com.mac.ghpt.model.entity.doctor.Doctor;
import com.mac.ghpt.model.entity.order.OrderInfo;
import com.mac.ghpt.model.entity.system.DoctorSet;
import com.mac.ghpt.model.entity.user.Patient;
import com.mac.ghpt.model.vo.doctor.DoctorLoginVo;
import com.mac.ghpt.model.vo.hosp.LoginVo;
import com.mac.ghpt.model.vo.order.OrderQueryVo;
import com.mac.ghpt.order.client.OrderFeignClient;
import com.mac.ghpt.result.ResultCodeEnum;
import com.mac.ghpt.user.client.UserFeignClient;
import com.mac.ghpt.utils.MD5;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月27日, 18:12:16
 */
@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor> implements DoctorService {

    private DoctorRepository doctorRepository;
    private RedisTemplate<String, String> redisTemplate;
    private OrderFeignClient orderFeignClient;
    private UserFeignClient userFeignClient;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository,
                             RedisTemplate redisTemplate,
                             OrderFeignClient orderFeignClient,
                             UserFeignClient userFeignClient) {
        this.doctorRepository = doctorRepository;
        this.redisTemplate = redisTemplate;
        this.orderFeignClient = orderFeignClient;
        this.userFeignClient = userFeignClient;
    }

    /**
     * 判断账号是否合法
     * @param doctorId
     * @return
     */
    @Override
    public Boolean checkValidity(String doctorId) {
        List<DoctorSet> doctorSets = doctorRepository.findAll();
        // 如果结果是true说明账号合法，否则不合法
        for (DoctorSet doctorSet : doctorSets) {
            if (doctorSet.getDoctorId().equals(doctorId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断账号是否已经被注册
     * @param doctorId
     * @return
     */
    @Override
    public Boolean checkIsExist(String doctorId) {
        // 在判断当前账号是否被注册
        List<Doctor> doctors = baseMapper.selectList(null);
        // 如果结果为true说明账号已经被注册，否则可以注册
        for (Doctor doctor : doctors) {
            if (doctor.getDoctorId().equals(doctorId)) {
                return true;
            }
        }
        return false;
    }



    /**
     * 注册账户
     * @param doctorLoginDto
     */
    @Override
    public void registerUser(DoctorLoginDto doctorLoginDto) {
        // 拿到账号和密码  账号就是医生编号
        String doctorId = doctorLoginDto.getUserName();
        String password = doctorLoginDto.getPassword();
        // 根据doctorId到数据库中查询该医生的信息
        DoctorSet doctorSet = doctorRepository.findByDoctorId(doctorId);

        // 封装信息
        Doctor doctor = new Doctor();
        doctor.setDoctorId(doctorId);
        doctor.setDoctorPhone(doctorSet.getDoctorPhone());
        doctor.setDoctorName(doctorSet.getDoctorName());
        doctor.setDoctorSkill(doctorSet.getDoctorSkill());
        // 处理性别
        if (doctorSet.getGender().equals("男")){
            doctor.setGender(1);
        }
        if (doctorSet.getGender().equals("女")) {
            doctor.setGender(0);
        }
        // 对密码加密
        String encryptPwd = DigestUtils.md5DigestAsHex(password.getBytes());
        doctor.setPassword(encryptPwd);
        doctor.setCreateTime(new Date());
        doctor.setUpdateTime(new Date());
        doctor.setIsDeleted(0);
        baseMapper.insert(doctor);
    }


    /**
     * 登录
     * @param doctorLoginDto
     * @return
     */
    @Override
    public DoctorLoginVo login(DoctorLoginDto doctorLoginDto) {
        // 拿到账号和密码
        String doctorId = doctorLoginDto.getUserName();
        String password = doctorLoginDto.getPassword();
        // 查询数据库
        LambdaQueryWrapper<Doctor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Doctor::getDoctorId,doctorId);
        Doctor doctor = baseMapper.selectOne(wrapper);
        // 将输入的密码做加密处理
        String encryptPwd = DigestUtils.md5DigestAsHex(password.getBytes());
        // 根数据库中密码做对比
        if (!doctor.getPassword().equals(encryptPwd)) {
            throw new GhptException(ResultCodeEnum.PASSWORD_ERROR);
        }
        // 生成用户令牌，保存到redis中，时间为30分钟
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.opsForValue().set(Constants.DOCTOR_TOEKN + token,
                JSON.toJSONString(doctor), 30, TimeUnit.MINUTES);
        DoctorLoginVo doctorLoginVo = new DoctorLoginVo();
        doctorLoginVo.setToken(token);
        doctorLoginVo.setRefresh_token("");
        return doctorLoginVo;
    }

    /**
     * 获取登录信息
     * @param token
     * @return
     */
    @Override
    public Doctor getDoctorInfo(String token) {
        String jsonString = redisTemplate.opsForValue().get(Constants.DOCTOR_TOEKN + token);
        Doctor doctor = JSON.parseObject(jsonString, Doctor.class);
        return doctor;
    }

    @Override
    public void logout(String token) {
        // 从redis中移除用户的token
        redisTemplate.delete(Constants.DOCTOR_TOEKN + token);
    }

    /**
     * 根据医生姓名获取下单该医生的订单
     * @param currentPage
     * @param pageSize
     * @param orderQueryVo
     * @return
     */
    @Override
    public Map<String, Object> getOrderList(Integer currentPage, Integer pageSize, OrderQueryVo orderQueryVo) {
        Map<String, Object> page = orderFeignClient.getPage(currentPage, pageSize, orderQueryVo);
        return page;
    }

    @Override
    public OrderInfo getOrderDetail(String orderNo) {
        OrderInfo orderInfo = orderFeignClient.getById(orderNo);
        return orderInfo;
    }

    @Override
    public IPage<Patient> getPatientList(Page<Patient> page, PatientQueryVo patientQueryVo) {
        // 条件
        String name = patientQueryVo.getName();
        List<Patient> patients = userFeignClient.getPatientListByName(patientQueryVo.getPatientNames());
        IPage<Patient> ipage = page;
        if (!StringUtils.isEmpty(name)) {
            // 根据patientName对list集合过滤分组
            List<Patient> list = patients.stream().filter(patient -> patient.getName().equals(name))
                    .collect(Collectors.toList());
            ipage.setRecords(list);
            ipage.setTotal(list.size());
            ipage.setPages(list.size() / page.getSize());
        } else {
            ipage.setRecords(patients);
            ipage.setTotal(patients.size());
            ipage.setPages(patients.size() / page.getSize());
        }
        return ipage;
    }

    @Override
    public Patient getPatientDetail(Long id) {
        Patient patient = userFeignClient.getById(id);
        return patient;
    }


}
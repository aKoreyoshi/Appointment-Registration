package com.mac.ghpt.manage.service.impl;

import com.alibaba.fastjson2.JSON;
import com.mac.ghpt.exception.GhptException;
import com.mac.ghpt.manage.repository.ScheduleRepository;
import com.mac.ghpt.manage.service.DepartmentService;
import com.mac.ghpt.manage.service.HospitalService;
import com.mac.ghpt.manage.service.ScheduleService;
import com.mac.ghpt.model.dto.system.ScheduleDto;
import com.mac.ghpt.model.entity.system.BookingRule;
import com.mac.ghpt.model.entity.system.Department;
import com.mac.ghpt.model.entity.system.HospitalSet;
import com.mac.ghpt.model.entity.system.Schedule;
import com.mac.ghpt.model.vo.order.ScheduleOrderVo;
import com.mac.ghpt.result.ResultCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月11日, 18:57:49
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private DepartmentService departmentService;

    /**
     * 上传排班方法实现
     *
     * @param filePath
     */
    @Override
    public void saveSchedule(String filePath) {
        Path path = Path.of(filePath);
        try {
            String jsonString = Files.readString(path);
            // 将JSON字符串转换为Schedule对象
            List<Schedule> schedules = JSON.parseArray(jsonString, Schedule.class);
            schedules.stream().forEach(schedule -> {
                // System.out.println(schedule);
                // 根据医院编号和排班编号查询数据是否已经存在
                Schedule scheduleExist = scheduleRepository.findByHospitalCodeAndScheduleId(
                        schedule.getHospitalCode(), schedule.getScheduleId());

                if (scheduleExist != null) {
                    scheduleExist.setUpdateTime(new Date());
                    scheduleExist.setStatus(1);
                    scheduleExist.setIsDeleted(0);
                    scheduleRepository.save(scheduleExist);
                } else {
                    schedule.setCreateTime(new Date());
                    schedule.setUpdateTime(new Date());
                    schedule.setIsDeleted(0);
                    scheduleRepository.save(schedule);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 条件分页查询排班记录
     *
     * @param currentPage
     * @param pageSize
     * @param scheduleDto
     * @return
     */
    @Override
    public Page<Schedule> selectPage(Integer currentPage, Integer pageSize, ScheduleDto scheduleDto) {
        PageRequest pageRequest = PageRequest.of(currentPage - 1, pageSize);
        // 创建匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                // 改变默认字符串匹配方式，模糊查询
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                // 改变默认大小写匹配方式，忽略大小写
                .withIgnoreCase(true)
                .withIgnoreNullValues();

        // 封装查询参数
        Schedule schedule = new Schedule();
        if (!StringUtils.isEmpty(scheduleDto.getDoctorName())) {
            schedule.setDoctorName(scheduleDto.getDoctorName());
        }
        if (!StringUtils.isEmpty(scheduleDto.getWorkDate())) {
            schedule.setWorkDate(scheduleDto.getWorkDate());
        }
        if (scheduleDto.getWorkTime() != null) {
            schedule.setWorkTime(scheduleDto.getWorkTime());
        }

        // 封装查询条件
        Example<Schedule> example = Example.of(schedule, exampleMatcher);
        Page<Schedule> schedulePage = scheduleRepository.findAll(example, pageRequest);
        return schedulePage;
    }

    @Override
    public void deleteSchedule(String id) {
        scheduleRepository.deleteById(id);
    }

    @Override
    public List<Schedule> getDetailSchedule(String departmentCode, String workDate) {
        List<Schedule> scheduleList = scheduleRepository
                .findByDepartmentCodeAndWorkDate(departmentCode, workDate);
        return scheduleList;
    }

    @Override
    public Schedule getScheduleById(String scheduleId) {
//        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        Schedule schedule = scheduleRepository.findByScheduleId(scheduleId);
        return schedule;
    }

    /**
     * 根据排班id获取排班订单信息
     *
     * @param scheduleId
     * @return
     */
    @Override
    public ScheduleOrderVo getScheduleOrder(String scheduleId) {
        ScheduleOrderVo scheduleOrderVo = new ScheduleOrderVo();
        Schedule schedule = this.getScheduleById(scheduleId);
        if (schedule == null) {
            throw new GhptException(ResultCodeEnum.DATA_ERROR);
        }
        Map<String, Object> hospitalInfo = hospitalService.getHospitalInfo();
//        LinkedHashMap<String, String> hospitalSet = (LinkedHashMap<String, String>) hospitalInfo.get("hospitalSet");
        HospitalSet hospitalSet = (HospitalSet) hospitalInfo.get("hospitalSet");
        if (hospitalSet == null) {
            throw new GhptException(ResultCodeEnum.DATA_ERROR);
        }
//        LinkedHashMap<String, Object> bookingRule = (LinkedHashMap<String, Object>) hospitalInfo.get("bookingRule");
        BookingRule bookingRule = (BookingRule) hospitalInfo.get("bookingRule");
        if (bookingRule == null) {
            throw new GhptException(ResultCodeEnum.DATA_ERROR);
        }
        Department department = departmentService.getByHospitalCodeAndDepartmentCode(
//                        hospitalSet.get("hospitalCode"), schedule.getDepartmentCode());
                hospitalSet.getHospitalCode(), schedule.getDepartmentCode());
        // 封装数据
        // 获取医院名称
//        String hospitalName = hospitalSet.get("hospitalName");
        String hospitalName = hospitalSet.getHospitalName();
        scheduleOrderVo.setHosptialName(hospitalName);
        scheduleOrderVo.setDepartmentCode(schedule.getDepartmentCode());
        scheduleOrderVo.setDepartmentName(department.getDepartmentName());
        scheduleOrderVo.setScheduleId(scheduleId);
        scheduleOrderVo.setDoctorName(schedule.getDoctorName());
        scheduleOrderVo.setWorkDate(new DateTime(schedule.getWorkDate()).toDate());
        scheduleOrderVo.setWorkTime(schedule.getWorkTime());
        scheduleOrderVo.setAmount(schedule.getRegistrationFee());

        //退号截止天数（如：就诊前一天为-1，当天为0）
//        int quitDay = Integer.parseInt(bookingRule.get("quitDay").toString());
        int quitDay = bookingRule.getQuitDay();
//        DateTime quitTime = getDateTime(new DateTime(schedule.getWorkDate()).plusDays(quitDay).toDate(),
//                bookingRule.getQuitTime());
        DateTime quitTime = getDateTime(new DateTime(schedule.getWorkDate()).toDate(),
                bookingRule.getQuitTime());
        // TODO 测试退号时间
        System.out.println("预约规则的退号时间为：" + bookingRule.getQuitTime() + ",退号时间为：" + quitTime);
        scheduleOrderVo.setQuitTime(quitTime.toDate());
        return scheduleOrderVo;
    }

    @Override
    public void update(Schedule schedule) {
        scheduleRepository.save(schedule);
    }


    /**
     * 将Date日期（yyyy-MM-dd HH:mm）转换为DateTime
     */
    private DateTime getDateTime(Date date, String timeString) {
        String dateTimeString = new DateTime(date).toString("yyyy-MM-dd") + " " + timeString;
        DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(dateTimeString);
        return dateTime;
    }
}

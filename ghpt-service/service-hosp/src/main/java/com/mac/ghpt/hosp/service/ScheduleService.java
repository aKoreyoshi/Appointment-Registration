package com.mac.ghpt.hosp.service;


import com.mac.ghpt.model.entity.system.Schedule;
import com.mac.ghpt.model.vo.order.ScheduleOrderVo;

import java.util.List;
import java.util.Map;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年04月13日, 17:31:19
 */
public interface ScheduleService {

    Map<String, Object> getBookingScheduleRule(Integer currentPage, Integer pageSize,
                                               String departmentCode);

    List<Schedule> getDetailSchedule(String departmentCode, String workDate);

    // 根据id获取
    Schedule getScheduleById(String scheduleId);

//    // 获取排班订单信息
//    ScheduleOrderVo getScheduleOrder(String scheduleId);

    // 更新排班
    void updateSchedule(Schedule schedule);

}
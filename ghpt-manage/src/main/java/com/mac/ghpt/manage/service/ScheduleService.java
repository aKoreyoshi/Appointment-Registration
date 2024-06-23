package com.mac.ghpt.manage.service;


import com.mac.ghpt.model.dto.system.ScheduleDto;
import com.mac.ghpt.model.entity.system.Schedule;
import com.mac.ghpt.model.vo.order.ScheduleOrderVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年03月11日, 16:14:30
 */
public interface ScheduleService {
    // 上传排班
    void saveSchedule(String filePath);

    // 条件分页查询排班记录
    Page<Schedule> selectPage(Integer currentPage, Integer pageSize, ScheduleDto scheduleDto);

    void deleteSchedule(String id);

    List<Schedule> getDetailSchedule(String departmentCode, String workDate);

    // 根据id获取Schedule
    Schedule getScheduleById(String scheduleId);

    // 获取排班订单信息
    ScheduleOrderVo getScheduleOrder(String scheduleId);

    // 更新排班
    void update(Schedule schedule);


}

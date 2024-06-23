package com.mac.ghpt.manage.client;

import com.mac.ghpt.model.entity.system.Department;
import com.mac.ghpt.model.entity.system.Dict;
import com.mac.ghpt.model.entity.system.Schedule;
import com.mac.ghpt.model.vo.hosp.DepartmentVo;
import com.mac.ghpt.model.vo.order.ScheduleOrderVo;
import com.mac.ghpt.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年04月10日, 17:01:45
 */
@FeignClient(name = "ghpt-manage", url = "http://localhost:8090")
public interface HospFeignClient {

    // 根据dictCode获取子元素
    @GetMapping("/admin/hosp/dict/findChildrenByDictCode/{dictCode}")
    List<Dict> findChildrenByDictCode(@PathVariable("dictCode") String dictCode);

    // 获取医院详细信息
    @GetMapping("/admin/hosp/hospital/getHospInfo")
    Map<String, Object> getHospitalInfo();

    // 获取科室列表
    @GetMapping("/admin/hosp/department/getDepList")
    List<DepartmentVo> getDepartmentList();

    @GetMapping("/admin/hosp/department/getDepartment/{hospitalCode}/{departmentCode}")
    Department getDepartment(@PathVariable("hospitalCode") String hospitalCode,
                             @PathVariable("departmentCode") String departmentCode);

    @GetMapping("/admin/hosp/schedule/getDetailSchedule/{departmentCode}/{workDate}")
    List<Schedule> getDetailSchedule(@PathVariable("departmentCode") String departmentCode,
                                     @PathVariable("workDate") String workDate);

    // 根据id获取schedule
    @GetMapping("/admin/hosp/schedule/getScheduleById/{scheduleId}")
    Schedule getScheduleById(@PathVariable("scheduleId")String scheduleId);

    @PostMapping("/admin/hosp/schedule/updateSchedule")
    void updateSchedule(Schedule schedule);

    // 根据排班id获取排班可预约数
    @GetMapping("/admin/hosp/schedule/getScheduleOrder/{scheduleId}")
    ScheduleOrderVo getScheduleOrder(@PathVariable("scheduleId") String scheduleId);

    // 获取天气信息
    @GetMapping("/admin/system/index/getWeather")
    Result getWeather();
}
package com.mac.ghpt.hosp.controller;

import com.mac.ghpt.hosp.service.ScheduleService;
import com.mac.ghpt.model.entity.system.Schedule;
import com.mac.ghpt.model.vo.order.ScheduleOrderVo;
import com.mac.ghpt.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月13日, 17:34:32
 */
@Tag(name = "排班管理")
@RestController
@RequestMapping("/api/hosp/schedule")
public class ScheduleController {

    private ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Operation(description = "获取可预约排班数据")
    @GetMapping("/getBookingScheduleRule/{currentPage}/{pageSize}/{departmentCode}")
    public Result getBookingScheduleRule(@PathVariable("currentPage") Integer currentPage,
                                         @PathVariable("pageSize") Integer pageSize,
                                         @PathVariable("departmentCode")String departmentCode) {
        Map<String, Object> bookingScheduleRule =
                scheduleService.getBookingScheduleRule(currentPage, pageSize, departmentCode);
        return Result.ok(bookingScheduleRule);
    }

    @Operation(description = "获取排班数据")
    @GetMapping("/findScheduleList/{departmentCode}/{workDate}")
    public Result findScheduleList(@PathVariable("departmentCode") String departmentCode,
                                   @PathVariable("workDate") String workDate) {
        return Result.ok(scheduleService.getDetailSchedule(departmentCode, workDate));
    }

    @Operation(description = "根据id获取")
    @GetMapping("/getScheduleById/{scheduleId}")
    public Result getScheduleById(@PathVariable("scheduleId") String scheduleId) {
        Schedule schedule = scheduleService.getScheduleById(scheduleId);
        return Result.ok(schedule);
    }
    // TODO 删除
//    @Operation(description = "根据排班id获取预约下单数据")
//    @GetMapping("/auth/getScheduleOrder/{scheduleId}")
//    public ScheduleOrderVo getScheduleOrder(@PathVariable("scheduleId") String scheduleId) {
//        return scheduleService.getScheduleOrder(scheduleId);
//    }
}
package com.mac.ghpt.manage.controller;

import com.mac.ghpt.manage.service.ScheduleService;
import com.mac.ghpt.model.dto.system.ScheduleDto;
import com.mac.ghpt.model.entity.system.Schedule;
import com.mac.ghpt.model.vo.order.ScheduleOrderVo;
import com.mac.ghpt.result.Result;
import com.mac.ghpt.utils.FileProcessingUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月11日, 19:01:14
 */
@Tag(name = "排班管理接口")
@RestController
@RequestMapping("/admin/hosp/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Operation(description = "上传排班记录")
    @PostMapping("/saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        try {
            Part filePart = request.getPart("file");
            // 获取文件名
            String fileName = filePart.getSubmittedFileName();
            //设置保存路径
            String path = "D:\\gdesign\\example" + "\\" + fileName;
            // 判断文件是否存在
            boolean fileExists = FileProcessingUtil.isFileExists(path);
            if (fileExists) {
                // 文件存在，替换
                FileProcessingUtil.deleteFile(path);
                filePart.write(path);
            } else {
                // 直接保存文件
                filePart.write(path);
            }
            // 调用上传排班接口
            scheduleService.saveSchedule(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }

    @Operation(description = "分页展示排班记录")
    @GetMapping("/getScheduleList/{currentPage}/{pageSize}")
    public Result getScheduleList(@PathVariable("currentPage") Integer currentPage,
                                  @PathVariable("pageSize") Integer pageSize,
                                  ScheduleDto scheduleDto){
        Page<Schedule> shedulePage = scheduleService.selectPage(currentPage, pageSize, scheduleDto);
        return Result.ok(shedulePage);
    }

    @Operation(description = "根据id删除排班记录")
    @DeleteMapping("/deleteSchedule/{id}")
    public Result deleteSchedule(@PathVariable("id") String id){
        scheduleService.deleteSchedule(id);
        return Result.ok();
    }

    @Operation(description = "获取排班详情")
    @GetMapping("/getDetailSchedule/{departmentCode}/{workDate}")
    public List<Schedule> getDetailSchedule(@PathVariable("departmentCode") String departmentCode,
                                            @PathVariable("workDate") String workDate) {
        return scheduleService.getDetailSchedule(departmentCode, workDate);
    }

    @Operation(description = "根据id获取schedule")
    @GetMapping("/getScheduleById/{scheduleId}")
    public Schedule getScheduleById(@PathVariable("scheduleId")String scheduleId) {
        Schedule schedule = scheduleService.getScheduleById(scheduleId);
        return schedule;
    }

    @Operation(description = "根据排班id获取预约下单数据")
    @GetMapping("/getScheduleOrder/{scheduleId}")
    public ScheduleOrderVo getScheduleOrder(@PathVariable("scheduleId") String scheduleId) {
        return scheduleService.getScheduleOrder(scheduleId);
    }

    @Operation(description = "更新排班")
    @PostMapping("/updateSchedule")
    public void updateSchedule(Schedule schedule) {
        scheduleService.update(schedule);
    }

}

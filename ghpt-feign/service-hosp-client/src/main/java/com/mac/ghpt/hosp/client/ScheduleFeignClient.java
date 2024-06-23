package com.mac.ghpt.hosp.client;

import com.mac.ghpt.model.vo.order.ScheduleOrderVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年04月17日, 16:37:19
 */
@FeignClient(name = "service-hosp", url = "http://localhost:8081")
public interface ScheduleFeignClient {

    @GetMapping("/api/hosp/schedule/auth/getScheduleOrder/{scheduleId}")
    ScheduleOrderVo getScheduleOrder(@PathVariable("scheduleId") String scheduleId);
}
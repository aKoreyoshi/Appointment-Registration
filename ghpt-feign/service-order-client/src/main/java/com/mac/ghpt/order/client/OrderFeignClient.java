package com.mac.ghpt.order.client;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mac.ghpt.model.entity.order.OrderInfo;
import com.mac.ghpt.model.vo.order.OrderCountVo;
import com.mac.ghpt.model.vo.order.OrderQueryVo;
import com.mac.ghpt.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月18日, 21:03:30
 */
@FeignClient(name = "service-order", url = "http://localhost:8086")
public interface OrderFeignClient {

    @GetMapping("/admin/order/getPage/{currentPage}/{pageSize}")
    Map<String, Object> getPage(@PathVariable("currentPage") Integer currentPage,
                   @PathVariable("pageSize") Integer pageSize,
                   @RequestBody(required = false) OrderQueryVo orderQueryVo);


    @GetMapping("/admin/order/getByNo/{orderNo}")
    OrderInfo getById(@PathVariable("orderNo")String orderNo);

//    @GetMapping("/auth/getStatusList")
//    List<Map<String,Object>> getStatusList();

    @GetMapping("/admin/order/getCountOrderData")
    List<OrderCountVo> getCountOrderData();

    @PutMapping("/admin/order/updateStatus")
    void updateStatus(@RequestBody OrderInfo orderInfo);

    @GetMapping("/getOrderList/{doctorName}")
    List<OrderInfo> getOrderList(@PathVariable("doctorName") String doctorName);

}
package com.mac.ghpt.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mac.ghpt.model.entity.order.OrderInfo;
import com.mac.ghpt.model.enums.OrderStatusEnum;
import com.mac.ghpt.model.vo.order.OrderQueryVo;
import com.mac.ghpt.order.service.OrderService;
import com.mac.ghpt.result.Result;
import com.mac.ghpt.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月17日, 14:27:32
 */
@Tag(name = "订单管理")
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @Operation(description = "生成订单  Generate orders")
    @PostMapping("/auth/generateOrder/{scheduleId}/{id}")
    public Result generateOrder(@PathVariable("scheduleId") String scheduleId,
                                @PathVariable("id") Long id) {
        return Result.ok(orderService.generateOrder(scheduleId, id));
    }

    @Operation(description = "根据订单编号获取订单")
    @GetMapping("/auth/getOrderByOrderNo/{orderNo}")
    public Result getOrderByOrderNo(@PathVariable("orderNo") String orderNo) {
        return Result.ok(orderService.getOrderInfoById(orderNo));
    }

    @Operation(description = "分页条件查询订单")
    @GetMapping("/auth/getOrderList/{currentPage}/{pageSize}")
    public Result getOrderList(@PathVariable("currentPage")Integer currentPage,
                               @PathVariable("pageSize")Integer pageSize,
                               OrderQueryVo orderQueryVo, HttpServletRequest request) {
        Long userId = UserAuthUtil.getUserId(request);
        orderQueryVo.setUserId(userId);
        Page<OrderInfo> page = new Page<>(currentPage, pageSize);
        IPage<OrderInfo> iPage = orderService.selectPage(page, orderQueryVo);
        return Result.ok(iPage);
    }

    @Operation(description = "获取订单状态列表")
    @GetMapping("/auth/getStatusList")
    public Result getStatusList() {
        return Result.ok(OrderStatusEnum.getStatusList());
    }

    @Operation(description = "取消预约")
    @GetMapping("/auth/cancelOrder/{orderNo}")
    public Result cancelOrder(@PathVariable("orderNo") String orderNo) {
        Boolean isCancel = orderService.cancelOrder(orderNo);
        return Result.ok(isCancel);
    }

    @Operation(description = "支付完成，更改订单状态")
    @PutMapping("/auth/updateOrderStatus/{orderNo}")
    public Result isPay(@PathVariable("orderNo") String orderNo) {
        orderService.isPay(orderNo);
        return Result.ok();
    }



}
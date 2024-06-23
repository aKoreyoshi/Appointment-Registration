package com.mac.ghpt.manage.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mac.ghpt.exception.GhptException;
import com.mac.ghpt.helper.HttpRequestHelper;
import com.mac.ghpt.manage.service.OrderService;
import com.mac.ghpt.model.entity.order.OrderInfo;
import com.mac.ghpt.model.enums.OrderStatusEnum;
import com.mac.ghpt.model.vo.order.OrderCountVo;
import com.mac.ghpt.model.vo.order.OrderQueryVo;
import com.mac.ghpt.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月17日, 18:02:23
 */
@Tag(name = "订单管理-后台")
@RestController
@RequestMapping("/admin/hosp/order")
public class OrderController {

    private OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/submitOrder")
    public Result AgreeAccountLendProject(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
            Map<String, Object> resultMap = orderService.submitOrder(paramMap);
            return Result.ok(resultMap);
        } catch (GhptException e) {
            return Result.fail().message(e.getMessage());
        }
    }

    @Operation(description = "分页条件查询订单")
    @GetMapping("/getPageList/{currentPage}/{pageSize}")
    public Result getPageList(@PathVariable("currentPage") Integer currentPage,
                              @PathVariable("pageSize") Integer pageSize,
                              OrderQueryVo orderQueryVo) {
        Map<String, Object> pageList = orderService.getPageList(currentPage, pageSize, orderQueryVo);
        return Result.ok(pageList);
    }

    @Operation(description = "根据id获取订单信息")
    @GetMapping("/getOrderByNo/{orderNo}")
    public Result getOrderByNo(@PathVariable("orderNo") String orderNo) {
        OrderInfo orderInfo = orderService.getOrderByNo(orderNo);
        return Result.ok(orderInfo);
    }

    @Operation(description = "获取状态信息列表")
    @GetMapping("/getStatusList")
    public Result getstatusList() {
        return Result.ok(OrderStatusEnum.getStatusList());
    }

    @Operation(description = "获取每日预约数")
    @GetMapping("/getOrderCount")
    public Result getOrderCount() {
        Map<String, Object> map = orderService.getOrderCount();
        return Result.ok(map);
    }

    @Operation(description = "修改订单状态")
    @PostMapping("/updateCancelStatus")
    public Result updateOrderStatus(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
            orderService.updateCancelStatus(paramMap);
            return Result.ok();
        } catch (GhptException e) {
            return Result.fail().message(e.getMessage());
        }
    }
}
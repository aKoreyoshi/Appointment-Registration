package com.mac.ghpt.order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mac.ghpt.model.entity.order.OrderInfo;
import com.mac.ghpt.model.enums.OrderStatusEnum;
import com.mac.ghpt.model.vo.order.OrderCountVo;
import com.mac.ghpt.model.vo.order.OrderQueryVo;
import com.mac.ghpt.order.service.OrderService;
import com.mac.ghpt.result.Result;
import com.mac.ghpt.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月18日, 20:50:38
 */
@Tag(name = "后台订单管理")
@RestController
@RequestMapping("/admin/order")
public class AdminOrderController {

    private OrderService orderService;

    @Autowired
    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(description = "分页条件查询订单")
    @RequestMapping("/getPage/{currentPage}/{pageSize}")
    public Map<String, Object> getPage(@PathVariable("currentPage")Integer currentPage,
                               @PathVariable("pageSize")Integer pageSize,
                               @RequestBody(required = false) OrderQueryVo orderQueryVo) {
        Page<OrderInfo> page = new Page<>(currentPage, pageSize);
        IPage<OrderInfo> iPage = orderService.selectPage(page, orderQueryVo);
        HashMap<String, Object> map = new HashMap<>();
        map.put("page",iPage);
        return map;
    }

//    @Operation(description = "获取订单状态列表")
//    @GetMapping("/auth/getStatusList")
//    public List<Map<String,Object>> getStatusList() {
//        return OrderStatusEnum.getStatusList();
//    }

    @Operation(description = "根据id查询订单详情")
    @GetMapping("/getByNo/{orderNo}")
    public OrderInfo getById(@PathVariable("orderNo")String orderNo) {
        return orderService.getOrderInfoById(orderNo);
    }

    @Operation(description = "获取每日预约数")
    @GetMapping("/getCountOrderData")
    public List<OrderCountVo> getCountOrderData() {
        List<OrderCountVo> list = orderService.countOrderData();
        return list;
    }

    @Operation(description = "修改状态")
    @PutMapping("/updateStatus")
    public void updateStatus(@RequestBody OrderInfo orderInfo) {
        orderService.updateOrderStatus(orderInfo);
    }
}
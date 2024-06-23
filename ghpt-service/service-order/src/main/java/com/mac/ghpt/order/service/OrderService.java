package com.mac.ghpt.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mac.ghpt.model.entity.order.OrderInfo;
import com.mac.ghpt.model.vo.order.OrderCountVo;
import com.mac.ghpt.model.vo.order.OrderQueryVo;

import java.util.List;
import java.util.Map;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年04月17日, 14:23:57
 */
public interface OrderService extends IService<OrderInfo> {

    // 根据排班ID和就诊人ID生成订单
    String generateOrder(String scheduleId, Long id);

    //根据订单id查询订单详情
    OrderInfo getOrderInfoById(String orderNo);

    // 查询订单列表
    IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);

    // 统计每日预约数
    List<OrderCountVo> countOrderData();

    // 取消预约
    Boolean cancelOrder(String orderNo);

    void updateOrderStatus(OrderInfo orderInfo);

    void isPay(String orderNo);

    // 医生端口查看订单列表
//    Map<String,Object> getList(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);
}
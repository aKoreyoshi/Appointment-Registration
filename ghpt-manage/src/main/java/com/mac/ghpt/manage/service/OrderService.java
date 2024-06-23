package com.mac.ghpt.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mac.ghpt.model.entity.order.OrderInfo;
import com.mac.ghpt.model.vo.order.OrderCountVo;
import com.mac.ghpt.model.vo.order.OrderQueryVo;
import com.mac.ghpt.result.Result;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年04月17日, 18:01:04
 */
public interface OrderService {

    Map<String, Object> submitOrder(Map<String, Object> paramMap);

    // 条件分页查询
    Map<String, Object> getPageList(Integer currentPage, Integer pageSize, OrderQueryVo orderQueryVo);

    OrderInfo getOrderByNo(String orderNo);

    Map<String, Object> getOrderCount();

    // 更新取消预约状态
    void updateCancelStatus(Map<String, Object> paramMap);
}
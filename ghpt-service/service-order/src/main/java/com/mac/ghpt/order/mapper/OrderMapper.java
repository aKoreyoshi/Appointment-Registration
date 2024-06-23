package com.mac.ghpt.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mac.ghpt.model.entity.order.OrderInfo;
import com.mac.ghpt.model.vo.order.OrderCountVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年04月17日, 14:23:04
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderInfo> {
    List<OrderCountVo> countOrderData();
}
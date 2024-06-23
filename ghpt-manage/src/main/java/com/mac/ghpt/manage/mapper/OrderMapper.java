package com.mac.ghpt.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mac.ghpt.model.entity.order.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年04月17日, 18:12:43
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderInfo> {
}
package com.mac.ghpt.manage.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mac.ghpt.exception.GhptException;
import com.mac.ghpt.manage.mapper.OrderMapper;
import com.mac.ghpt.manage.mapper.ScheduleMapper;
import com.mac.ghpt.manage.repository.ScheduleRepository;
import com.mac.ghpt.manage.service.OrderService;
import com.mac.ghpt.manage.service.ScheduleService;
import com.mac.ghpt.model.entity.order.OrderInfo;
import com.mac.ghpt.model.entity.system.Schedule;
import com.mac.ghpt.model.entity.user.Patient;
import com.mac.ghpt.model.enums.OrderStatusEnum;
import com.mac.ghpt.model.vo.order.OrderCountVo;
import com.mac.ghpt.model.vo.order.OrderQueryVo;
import com.mac.ghpt.order.client.OrderFeignClient;
import com.mac.ghpt.result.Result;
import com.mac.ghpt.result.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月17日, 18:01:23
 */
@Service
public class OrderServiceImpl implements OrderService {

    private ScheduleService scheduleService;
    private MongoTemplate mongoTemplate;
    private ScheduleRepository scheduleRepository;
    private OrderFeignClient orderFeignClient;
    @Autowired
    public OrderServiceImpl(ScheduleService scheduleService,
                            MongoTemplate mongoTemplate,
                            ScheduleRepository scheduleRepository,
                            OrderFeignClient orderFeignClient){
        this.scheduleService = scheduleService;
        this.mongoTemplate = mongoTemplate;
        this.scheduleRepository = scheduleRepository;
        this.orderFeignClient = orderFeignClient;
    }
    @Override
    public Map<String, Object> submitOrder(Map<String, Object> paramMap) {
        String departmentCode = (String)paramMap.get("departmentCode");
        String scheduleId = (String)paramMap.get("scheduleId");
        String workDate = (String)paramMap.get("workDate");
        String workTime = (String)paramMap.get("workTime");
        String amount = (String)paramMap.get("amount");

        Schedule schedule = scheduleService.getScheduleById(scheduleId);
        if(null == schedule) {
            throw new GhptException(ResultCodeEnum.DATA_ERROR);
        }

        //就诊人信息
//        Patient patient = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Patient.class);
        Patient patient = JSON.parseObject(JSON.toJSONString(paramMap), Patient.class);
        // TODO 处理就诊人业务 后续可做优化
        Long patientId = this.savePatient(patient);

        Map<String, Object> resultMap = new HashMap<>();
        int availableNumber = schedule.getAvailableNumber().intValue() - 1;
        if(availableNumber > 0) {
            schedule.setAvailableNumber(availableNumber);
//            scheduleMapper.updateById(schedule);
            mongoTemplate.updateFirst(
                    new Query(Criteria.where("scheduleId").is(scheduleId)),
                    new Update().set("availableNumber", availableNumber), Schedule.class);

            //记录预约记录
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setPatientId(patientId.toString());
            orderInfo.setScheduleId(scheduleId);
            int number = schedule.getReservedNumber().intValue() - schedule.getAvailableNumber().intValue();
            orderInfo.setNumber(number);
            orderInfo.setAmount(new BigDecimal(amount));
//            String fetchTime = "0".equals(workDate) ? " 09:30前" : " 14:00前";
//            orderInfo.setFetchTime(reserveTime + fetchTime);
            orderInfo.setFetchAddress("一楼9号窗口");
            //默认 未支付
//            orderInfo.setStatus(0);
//            orderMapper.insert(orderInfo);

            resultMap.put("resultCode","0000");
            resultMap.put("resultMsg","预约成功");
            //预约号序
            resultMap.put("number", number);
            //取号时间
//            resultMap.put("fetchTime", reserveDate + "09:00前");;
            //取号地址
            resultMap.put("fetchAddress", "一层114窗口");;
            //排班可预约数
            resultMap.put("reservedNumber", schedule.getReservedNumber());
            //排班剩余预约数
            resultMap.put("availableNumber", schedule.getAvailableNumber());
        } else {
            throw new GhptException(ResultCodeEnum.DATA_ERROR);
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> getPageList(Integer currentPage, Integer pageSize, OrderQueryVo orderQueryVo) {
        Map<String, Object> page = orderFeignClient.getPage(currentPage, pageSize, orderQueryVo);
        return page;
    }

    @Override
    public OrderInfo getOrderByNo(String orderNo) {
        OrderInfo orderInfo = orderFeignClient.getById(orderNo);
        return orderInfo;
    }

    @Override
    public Map<String, Object> getOrderCount() {
        HashMap<String, Object> map = new HashMap<>();
        List<OrderCountVo> list = orderFeignClient.getCountOrderData();
        List<LocalDate> dateList = list.stream().map((item) ->
                        item.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .collect(Collectors.toList());
//        List<Date> dateList = list.stream().map(OrderCountVo::getDate).collect(Collectors.toList());
        List<Integer> countList = list.stream().map(OrderCountVo::getCount).collect(Collectors.toList());
        map.put("dateList",dateList);
        map.put("countList",countList);
        return map;
    }

    @Override
    public void updateCancelStatus(Map<String, Object> paramMap) {
        // 拿到订单编号
        String orderNo = (String) paramMap.get("orderNo");
        OrderInfo orderInfo = orderFeignClient.getById(orderNo);
        if (null == orderInfo) {
            throw new GhptException(ResultCodeEnum.DATA_ERROR);
        }
        // 更新可用预约数
        String scheduleId = orderInfo.getScheduleId();
        Schedule schedule = scheduleRepository.findByScheduleId(scheduleId);
        if (null == schedule) {
            throw new GhptException(ResultCodeEnum.DATA_ERROR);
        }
        int availableNumber = schedule.getAvailableNumber().intValue() + 1;
        mongoTemplate.updateFirst(
                new Query(Criteria.where("scheduleId").is(scheduleId)),
                new Update().set("availableNumber", availableNumber),Schedule.class);
        // 更新状态
        orderInfo.setStatus(OrderStatusEnum.CANCLE.getStatus());
        orderInfo.setQuitTime(new Date());
        orderFeignClient.updateStatus(orderInfo);
    }


    /**
     * 医院处理就诊人信息
     * @param patient
     */
    private Long savePatient(Patient patient) {
        // 业务：略
        return 1L;
    }
}
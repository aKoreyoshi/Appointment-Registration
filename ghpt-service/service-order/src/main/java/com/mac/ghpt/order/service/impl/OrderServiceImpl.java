package com.mac.ghpt.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.db.sql.Order;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mac.ghpt.exception.GhptException;
import com.mac.ghpt.helper.HttpRequestHelper;
import com.mac.ghpt.manage.client.HospFeignClient;
import com.mac.ghpt.model.entity.order.OrderInfo;
import com.mac.ghpt.model.entity.user.Patient;
import com.mac.ghpt.model.enums.OrderStatusEnum;
import com.mac.ghpt.model.vo.order.OrderCountVo;
import com.mac.ghpt.model.vo.order.OrderMqVo;
import com.mac.ghpt.model.vo.order.OrderQueryVo;
import com.mac.ghpt.model.vo.order.ScheduleOrderVo;
import com.mac.ghpt.model.vo.sms.SmsVo;
import com.mac.ghpt.order.mapper.OrderMapper;
import com.mac.ghpt.order.service.OrderService;
import com.mac.ghpt.rabbit.constant.MqConst;
import com.mac.ghpt.rabbit.service.RabbitService;
import com.mac.ghpt.result.ResultCodeEnum;
import com.mac.ghpt.user.client.UserFeignClient;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月17日, 14:25:24
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderInfo> implements OrderService {

    private UserFeignClient userFeignClient;
    private HospFeignClient hospFeignClient;
    private RabbitService rabbitService;
    private OrderMapper orderMapper;

    @Autowired
    public OrderServiceImpl(UserFeignClient userFeignClient,
                            HospFeignClient hospFeignClient,
                            OrderMapper orderMapper,
                            RabbitService rabbitService) {
        this.userFeignClient = userFeignClient;
        this.hospFeignClient = hospFeignClient;
        this.rabbitService = rabbitService;
        this.orderMapper = orderMapper;
    }

    /**
     * 生成订单
     *
     * @param scheduleId
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String generateOrder(String scheduleId, Long id) {
        // 获取就诊人信息
        Patient patient = userFeignClient.getPatientOrder(id);
        // 获取排班信息
        ScheduleOrderVo scheduleOrder = hospFeignClient.getScheduleOrder(scheduleId);
        // 封装订单信息
        OrderInfo orderInfo = new OrderInfo();
        // scheduleOrderVo 的数据复制到  orderInfo
        BeanUtil.copyProperties(scheduleOrder, orderInfo);
        // 生成随机订单号
        String orderNo = System.currentTimeMillis() + "" + new SecureRandom().nextInt(100);
        orderInfo.setOrderNo(orderNo);
        // 其他参数
        orderInfo.setUserId(patient.getUserId());
        orderInfo.setPatientId(id.toString());
        orderInfo.setPatientName(patient.getName());
        orderInfo.setPatientPhone(patient.getPhone());
        // 设置状态，预约成功，待支付
        orderInfo.setStatus(OrderStatusEnum.UNPAID.getStatus());
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());
        orderInfo.setIsDeleted(0);
        baseMapper.insert(orderInfo);

        //调用医院接口，实现预约挂号操作
        //设置调用医院接口需要的参数，参数放到map集合中
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("departmentCode", orderInfo.getDepartmentCode());
        paramMap.put("scheduleId", orderInfo.getScheduleId());
        paramMap.put("workDate", new DateTime(orderInfo.getWorkDate()).toString("yyyy-MM-dd"));
        paramMap.put("workTime", orderInfo.getWorkTime());
        paramMap.put("amount", orderInfo.getAmount());
        paramMap.put("patientName", orderInfo.getPatientName());
        paramMap.put("certificateType", patient.getCertificateType());
        paramMap.put("certificateNo", patient.getCertificateNo());
        paramMap.put("gender", patient.getGender());
        // 处理出生日期
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        paramMap.put("birthday", format.format(patient.getBirthday()));
        paramMap.put("patientPhone", patient.getPhone());
        paramMap.put("district", patient.getDistrict());
        paramMap.put("address", patient.getAddress());
        // 联系人
        paramMap.put("contactName", patient.getContactName());
        paramMap.put("contactPhone", patient.getContactPhone());
        paramMap.put("timestamp", HttpRequestHelper.getTimestamp());

        // 请求医院接口
        JSONObject result =
                HttpRequestHelper.sendRequest(paramMap, "http://localhost:8090/admin/hosp/order/submitOrder");
        //状态码
        if (result.getInteger("code") == 200) {
            JSONObject jsonObject = result.getJSONObject("data");
            //预约序号
            Integer number = jsonObject.getInteger("number");
            ;
            //取号地址
            String fetchAddress = jsonObject.getString("fetchAddress");
            ;
            //更新订单
            orderInfo.setNumber(number);
            orderInfo.setFetchAddress(fetchAddress);
            orderInfo.setUpdateTime(new Date());
            baseMapper.updateById(orderInfo);

            //排班可预约数
            Integer reservedNumber = jsonObject.getInteger("reservedNumber");
            //排班剩余预约数
            Integer availableNumber = jsonObject.getInteger("availableNumber");
            // TODO 发送mq信息更新号源和短信通知   后期来处理
            //发送mq信息更新号源和短信通知
            //发送mq信息更新号源
            OrderMqVo orderMqVo = new OrderMqVo();
            orderMqVo.setScheduleId(scheduleId);
            orderMqVo.setAvailableNumber(availableNumber);
            orderMqVo.setReservedNumber(reservedNumber);
            // 短信提示
            SmsVo smsVo = new SmsVo();
            smsVo.setPhone(orderInfo.getPatientPhone());
            smsVo.setTemplateCode("SMS_275235389");
            String reserveDate =
                    new DateTime(orderInfo.getWorkDate()).toString("yyyy-MM-dd")
                            + (orderInfo.getWorkTime() == 0 ? "上午" : "下午");
            Map<String, Object> param = new HashMap<String, Object>() {{
                put("title", orderInfo.getHosptialName() + "|" + orderInfo.getDepartmentName());
                put("amount", orderInfo.getAmount());
                put("reserveDate", reserveDate);
                put("name", orderInfo.getPatientName());
                put("quitTime", new DateTime(orderInfo.getQuitTime()).toString("yyyy-MM-dd HH:mm"));
            }};
            smsVo.setParam(param);
            orderMqVo.setSmsVo(smsVo);
            // 发送短信
//            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_ORDER, MqConst.ROUTING_ORDER, orderMqVo);
        } else {
            throw new GhptException(ResultCodeEnum.FAIL);
        }

        return orderInfo.getOrderNo();
    }

    /**
     * 根据订单编号获取订单详情
     *
     * @param orderNo
     * @return
     */
    @Override
    public OrderInfo getOrderInfoById(String orderNo) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getOrderNo, orderNo);
        OrderInfo orderInfo = baseMapper.selectOne(wrapper);
        orderInfo = this.packageOrderInfo(orderInfo);
        return orderInfo;
    }

    @Override
    public IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo) {
        // 获取查询条件
        String hospitalName = orderQueryVo.getHospitalName();
        Long userId = orderQueryVo.getUserId();
        String patientName = orderQueryVo.getPatientName();
        Integer status = orderQueryVo.getStatus();
        // 判断条件
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isEmpty(orderQueryVo.getDoctorName())) {
            wrapper.eq(OrderInfo::getDoctorName, orderQueryVo.getDoctorName());
        }
        if (!StringUtils.isEmpty(hospitalName)) {
            wrapper.eq(OrderInfo::getHosptialName, hospitalName);
        }
        if (orderQueryVo.getUserId() != null) {
            wrapper.eq(OrderInfo::getUserId, userId);
        }
        if (!StringUtils.isEmpty(patientName)) {
            wrapper.like(OrderInfo::getPatientName, patientName);
        }
        if (status != null) {
            wrapper.eq(OrderInfo::getStatus, status);
        }
        IPage<OrderInfo> orderInfos = baseMapper.selectPage(pageParam, wrapper);
        orderInfos.getRecords().stream().forEach(orderInfo -> {
            this.packageOrderInfo(orderInfo);
        });
        return orderInfos;
    }

    @Override
    public List<OrderCountVo> countOrderData() {
        List<OrderCountVo> list = orderMapper.countOrderData();
        return list;
    }

    /**
     * 根据订单号取消预约
     *
     * @param orderNo
     * @return
     */
    @Override
    public Boolean cancelOrder(String orderNo) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getOrderNo, orderNo);
        // 获取到订单信息
        OrderInfo orderInfo = baseMapper.selectOne(wrapper);
        // 判断是否能取消预约
        DateTime dateTime = new DateTime(orderInfo.getWorkDate());
        if (dateTime.isBeforeNow()) {
            // 超过退号时间，无法退号
            throw new GhptException(ResultCodeEnum.CANCEL_ORDER_NO);
        }
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("orderNo", orderNo);
        JSONObject result = HttpRequestHelper.
                sendRequest(reqMap, "http://localhost:8090/admin/hosp/order/updateCancelStatus");

        if (result.getInteger("code") != 200) {
            // 调用医院系统取消预约
            throw new GhptException(ResultCodeEnum.CANCEL_ORDER_FAIL);
        } else {
            // 更新订单状态
//            if (orderInfo.getStatus().intValue() == OrderStatusEnum.UNPAID.getStatus().intValue()) {
                orderInfo.setStatus(OrderStatusEnum.CANCLE.getStatus());
                baseMapper.updateById(orderInfo);
//            }
            // 发送mq信息更新预约数
            // 我们与下单成功更新预约数使用相同的mq信息，不设置可预约数与剩余预约数，接收端可预约数减1即可,取消+1
            OrderMqVo orderMqVo = new OrderMqVo();
            orderMqVo.setScheduleId(orderInfo.getScheduleId());
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_ORDER,
                    MqConst.ROUTING_ORDER, orderMqVo);
        }
        return true;
    }

    @Override
    public void updateOrderStatus(OrderInfo orderInfo) {
        baseMapper.updateById(orderInfo);
    }

    @Override
    public void isPay(String orderNo) {
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getOrderNo, orderNo);
        // 获取到订单信息
        OrderInfo orderInfo = baseMapper.selectOne(wrapper);
        // 更新订单状态为支付
        orderInfo.setStatus(OrderStatusEnum.PAID.getStatus());
        orderInfo.setUpdateTime(new Date());
        baseMapper.updateById(orderInfo);
    }


    private OrderInfo packageOrderInfo(OrderInfo orderInfo) {
        orderInfo.getParam().put("orderStatusString",
                OrderStatusEnum.getStatusNameByStatus(orderInfo.getStatus()));
        return orderInfo;
    }


}
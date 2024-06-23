//package com.mac.ghpt.hosp.receiver;
//
//import com.mac.ghpt.hosp.service.ScheduleService;
//import com.mac.ghpt.model.entity.system.Schedule;
//import com.mac.ghpt.model.vo.order.OrderMqVo;
//import com.mac.ghpt.model.vo.sms.SmsVo;
//import com.mac.ghpt.rabbit.constant.MqConst;
//import com.mac.ghpt.rabbit.service.RabbitService;
//import com.rabbitmq.client.Channel;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.Exchange;
//import org.springframework.amqp.rabbit.annotation.Queue;
//import org.springframework.amqp.rabbit.annotation.QueueBinding;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
///**
// * @author: 马聪
// * @function:
// * @version: 1.0
// * @date: 2024年04月17日, 20:35:08
// */
//@Component
//public class HospitalReceiver {
//
//    private ScheduleService scheduleService;
//    private RabbitService rabbitService;
//    @Autowired
//    public HospitalReceiver(ScheduleService scheduleService, RabbitService rabbitService){
//        this.scheduleService = scheduleService;
//        this.rabbitService = rabbitService;
//    }
//
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = MqConst.QUEUE_ORDER,durable = "true"),
//            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_ORDER),
//            key = {MqConst.ROUTING_ORDER}
//    ))
//    public void receiver(OrderMqVo orderMqVo, Message message, Channel channel) throws IOException {
//       if (null != orderMqVo.getAvailableNumber()) {
//           //下单成功更新预约数
//           Schedule schedule = scheduleService.getScheduleById(orderMqVo.getScheduleId());
//           schedule.setReservedNumber(orderMqVo.getReservedNumber());
//           schedule.setAvailableNumber(orderMqVo.getAvailableNumber());
//           scheduleService.updateSchedule(schedule);
//       } else {
//           //取消预约更新预约数
//           Schedule schedule = scheduleService.getScheduleById(orderMqVo.getScheduleId());
//           int availableNumber = schedule.getAvailableNumber().intValue() + 1;
//           schedule.setAvailableNumber(availableNumber);
//           scheduleService.updateSchedule(schedule);
//       }
//        //发送短信
//        SmsVo smsVo = orderMqVo.getSmsVo();
//        if(null != smsVo) {
//            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_MSM, MqConst.ROUTING_MSM_ITEM, smsVo);
//        }
//    }
//}
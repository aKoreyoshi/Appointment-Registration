//package com.mac.ghpt.sms.receiver;
//
//import com.mac.ghpt.model.vo.sms.SmsVo;
//import com.mac.ghpt.rabbit.constant.MqConst;
//import com.mac.ghpt.sms.service.SmsService;
//import com.rabbitmq.client.Channel;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.Exchange;
//import org.springframework.amqp.rabbit.annotation.Queue;
//import org.springframework.amqp.rabbit.annotation.QueueBinding;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * @author: 马聪
// * @function:
// * @version: 1.0
// * @date: 2024年04月17日, 20:23:39
// */
//@Component
//public class SmsReceiver {
//
//    private SmsService smsService;
//    @Autowired
//    public SmsReceiver(SmsService smsService){
//        this.smsService = smsService;
//    }
//
//    //监听队列里面是否有内容，有就进行方法调用，短信的发送，订阅到这个内容，往队列中放入发送的请求  最终完成发送
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = MqConst.QUEUE_MSM_ITEM, durable = "true"),
//            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_MSM),
//            key = {MqConst.ROUTING_MSM_ITEM}
//    ))
//
//    public void send(SmsVo smsVo, Message message, Channel channel) {
//        smsService.sendSms(smsVo);
//    }
//
//}
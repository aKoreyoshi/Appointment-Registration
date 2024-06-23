package com.mac.ghpt.rabbit.constant;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月17日, 18:39:06
 */
public class MqConst {
    /**
     * 预约下单
     */
    public static final String EXCHANGE_DIRECT_ORDER
            = "exchange.direct.order";
    public static final String ROUTING_ORDER = "order";
    //队列
    public static final String QUEUE_ORDER  = "queue.order";
    /**
     * 短信
     */
    public static final String EXCHANGE_DIRECT_MSM = "exchange.direct.sms";
    public static final String ROUTING_MSM_ITEM = "sms.item";
    //队列
    public static final String QUEUE_MSM_ITEM  = "queue.sms.item";
}
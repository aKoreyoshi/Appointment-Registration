package com.mac.ghpt.sms.service;

import com.mac.ghpt.model.vo.sms.SmsVo;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年04月03日, 16:23:23
 */
public interface SmsService {
    // 发送短信验证码
    boolean sendCode(String phone, String code);

    // 使用mq发送短信
    boolean sendSms(SmsVo smsVo);
}
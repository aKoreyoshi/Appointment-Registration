package com.mac.ghpt.sms.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月03日, 17:48:33
 */
@Getter
@Component
//@ConfigurationProperties(prefix = "aliyun.sms")
public class AliyunSmsProperties {

    @Value("${aliyun.sms.regionId}")
    private String regionId;

    @Value("${aliyun.sms.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.sms.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.sms.signName}")
    private String signName;

    @Value("${aliyun.sms.templateCode}")
    private String templateCode;

}
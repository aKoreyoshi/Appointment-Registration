package com.mac.ghpt.sms.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.mac.ghpt.model.vo.sms.SmsVo;
import com.mac.ghpt.sms.service.SmsService;
import com.mac.ghpt.sms.utils.AliyunSmsProperties;
import darabonba.core.client.ClientOverrideConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月03日, 16:23:56
 */
@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private AliyunSmsProperties aliyunSmsProperties;

    /**
     * 发送短信验证
     * @param phone
     * @param code
     * @return
     */
    @Override
    public boolean sendCode(String phone, String code) {
        // 判断手机号是否为空
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        // 提供阿里云身份验证
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(aliyunSmsProperties.getAccessKeyId())
                .accessKeySecret(aliyunSmsProperties.getAccessKeySecret())
                .build());

        // 配置client
        AsyncClient client = AsyncClient.builder()
                .region(aliyunSmsProperties.getRegionId())
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                )
                .build();

        // API请求的参数设置
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .phoneNumbers(phone)
                .signName(aliyunSmsProperties.getSignName())
                .templateCode(aliyunSmsProperties.getTemplateCode())
                .templateParam(JSONUtil.toJsonStr(MapUtil.builder("code", code).build()))
                .build();
        // 发送短信
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        boolean isSuccess;
        try {
            isSuccess = response.get().getBody().getCode().equals("OK");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        return isSuccess;
    }


    /**
     * 使用MQ发送短信
     * @param smsVo
     * @return
     */
    @Override
    public boolean sendSms(SmsVo smsVo) {
        //手机号不等于空才发送  等于空不发送
        if (!StringUtils.isEmpty(smsVo.getPhone())) {
            String code = smsVo.getParam().get("code").toString();
            boolean isSend = this.sendCode(smsVo.getPhone(), code);
            return isSend;
        }
        return false;
    }
}
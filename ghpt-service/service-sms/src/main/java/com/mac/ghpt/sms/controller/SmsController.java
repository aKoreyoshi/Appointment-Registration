package com.mac.ghpt.sms.controller;

import com.mac.ghpt.result.Result;
import com.mac.ghpt.result.ResultCodeEnum;
import com.mac.ghpt.sms.service.SmsService;
import com.mac.ghpt.sms.utils.AliyunSmsProperties;
import com.mac.ghpt.sms.utils.RandomCodeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月02日, 21:31:02
 */
@Tag(name = "短信接口管理")
@RestController
@RequestMapping("/api/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Operation(summary = "发送短信验证码")
    @GetMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable("phone") String phone) {
        System.out.println("phone" + phone);
        // 先从redis缓存中取验证码 key是手机号 value是验证码
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)) {
            // 直接返回
            return Result.ok();
        }
        // redis缓存中没有验证码
        // 首先生成验证码
        code = RandomCodeUtil.generateSixDigitCode();
        // 调用方法发送验证码
        boolean isSend = smsService.sendCode(phone, code);
        if (isSend) {
            // 将验证码放入到redis缓存中
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
            return Result.ok();
        }
        return Result.fail(ResultCodeEnum.CODE_SEND_FAIL);
    }
}
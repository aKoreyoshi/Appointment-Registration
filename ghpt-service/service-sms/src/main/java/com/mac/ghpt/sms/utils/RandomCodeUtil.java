package com.mac.ghpt.sms.utils;

import java.util.Random;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月03日, 17:09:54
 */
public class RandomCodeUtil {
    // 生成四位数字验证码
    public static String generateFourDigitCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }

    // 生成六位数字验证码
    public static String generateSixDigitCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
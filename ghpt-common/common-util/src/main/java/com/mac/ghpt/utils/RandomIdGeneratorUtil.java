package com.mac.ghpt.utils;

import java.util.Random;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月13日, 13:48:42
 */
public class RandomIdGeneratorUtil {
    public static Long generateRandomId() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(random.nextInt(10)); // 生成一个随机的数字（0-9）
        }
        return Long.parseLong(sb.toString());
    }

//    public static void main(String[] args) {
//        Long randomId = generateRandomId();
//        System.out.println("Random ID: " + randomId);
//    }
}
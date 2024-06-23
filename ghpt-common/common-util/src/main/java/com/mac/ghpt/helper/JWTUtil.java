package com.mac.ghpt.helper;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月11日, 13:53:25
 */
public class JWTUtil {
    //token过期时间的设置 30天
    private static long tokenExpiration = 24 * 60 * 60 * 30;

    // 设置密匙
    private static Algorithm SERCET_KEY = Algorithm.HMAC256("ghpt-parent");

    // 根据参数生成token
    public static String createToken(Long id, String phone) {
        String token = JWT.create()
                .withClaim("id", id)
                .withClaim("phone", phone)
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpiration * 1000))
                .sign(SERCET_KEY);
        return token;
    }

    // 根据token获取id
    public static Long getId(String token){
        if (StringUtils.isEmpty(token)) return null;
        // 解析token
        Long id = JWT.require(SERCET_KEY)
                .build()
                .verify(token)
                .getClaim("id")
                .as(Long.class);
        return id;
    }

    // 根据token获取phone
    public static String getPhone(String token){
        if (StringUtils.isEmpty(token)) return "";
        String phone = JWT.require(SERCET_KEY)
                .build()
                .verify(token)
                .getClaim("phone")
                .as(String.class);
        return phone;
    }

    public static void main(String[] args) {
        String token = JWTUtil.createToken(1L, "13812345678");
        String phone = JWTUtil.getPhone(token);
        System.out.println(phone);
    }
}
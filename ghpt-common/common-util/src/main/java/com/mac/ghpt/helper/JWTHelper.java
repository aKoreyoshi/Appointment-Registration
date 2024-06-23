package com.mac.ghpt.helper;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月05日, 16:00:09
 */
public class JWTHelper {
    //token过期时间的设置 30天
    private static long tokenExpiration = 24 * 60 * 60 * 30;
    //token签名的密钥
    private static SecretKey  tokenSignKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    //根据参数串生成token
    public static String createToken(Long id, String phone) {
        Date now = new Date();
        String token = Jwts.builder()
                .setSubject("ghpt-user")
                .claim("userId", id)
                .claim("phone", phone)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenExpiration))
                .signWith(tokenSignKey)
                .compact();
        return token;
    }

    //根据token字符串得到用户id
    public static Long getUserId(String token) {
        if (StringUtils.isEmpty(token)) return null;
        Claims claims = Jwts.parser()
                .verifyWith(tokenSignKey)
                .build()
                .parseClaimsJws(token)
                .getPayload();
//        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
//        Claims claims = claimsJws.getBody();
//        Integer userId = (Integer) claims.get("userId");
        return claims.get("userId", Long.class);
    }

    //根据token字符串得到用户phone
    public static String getPhone(String token) {
        if (StringUtils.isEmpty(token)) return "";
//        Jws<Claims> claimsJws
//                = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
//        Claims claims = claimsJws.getBody();
        Claims claims = Jwts.parser()
                .verifyWith(tokenSignKey)
                .build()
                .parseClaimsJws(token)
                .getPayload();

        return claims.get("phone", String.class);
    }

//    public static void main(String[] args) {
//        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJnaHB0LXVzZXIiLCJ1c2VySWQiOjE3Nzc2MzgxMzMzNDEzODA2MTAsInBob25lIjoiMTg3MDM2MjUyMTciLCJpYXQiOjE3MTI3NDg4OTksImV4cCI6MTcxMjc1MTQ5MX0.NUCL_pHj3AboUIn8aVD8ncph-3zDDwPqeZCZzcjrr5bkKiV4jrqRiT3Uo4YWJaH2ujCdLlN04ukLCuPdSMoC9w";
//        String toekn2 = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJnaHB0LXVzZXIiLCJ1c2VySWQiOjE3Nzc2MzgxMzMzNDEzODA2MTAsInBob25lIjoiMTg3MDM2MjUyMTciLCJpYXQiOjE3MTI3NDg1MDgsImV4cCI6MTcxMjc1MTEwMH0.kIm-fZE7G4S4vI1J9H4tkoaHN0UWLPS6ZbWIWWQFqFAYmgQ6fapx66f2QyudyHRVvKskc3nR8ltOSEaVqoUqIg";
//        System.out.println(JWTHelper.getPhone(token));
//    }


}
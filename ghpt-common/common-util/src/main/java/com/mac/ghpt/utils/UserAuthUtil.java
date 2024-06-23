package com.mac.ghpt.utils;

import com.mac.ghpt.helper.JWTHelper;
import com.mac.ghpt.helper.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author: 马聪
 * @function: 获取当前用户信息工具类
 * @version: 1.0
 * @date: 2024年04月05日, 16:33:37
 */
public class UserAuthUtil {
    // 获取当前用户的id
    public static Long getUserId(HttpServletRequest request){
        // 从header获取token
        String token = request.getHeader("token");
        // jwt从token获取userid
        Long id = JWTUtil.getId(token);
        return id;
    }

    // 获取当前用户名称
    public static String getPhone(HttpServletRequest request){
        // 从header获取token
        String token = request.getHeader("token");
        // jwt从token获取userName
        String phone = JWTUtil.getPhone(token);
        return phone;

    }
}
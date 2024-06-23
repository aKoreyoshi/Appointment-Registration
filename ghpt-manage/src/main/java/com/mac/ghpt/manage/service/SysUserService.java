package com.mac.ghpt.manage.service;

import com.alibaba.fastjson2.JSONObject;
import com.mac.ghpt.model.dto.system.LoginDto;
import com.mac.ghpt.model.entity.system.SysUser;
import com.mac.ghpt.model.vo.hosp.LoginVo;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年03月01日, 15:46:19
 */
public interface SysUserService {

    // 用户登录
    LoginVo login(LoginDto loginDto);

    // 获取用户信息
    SysUser getUserInfo(String token);

    // 退出登录
    void logout(String token);

    // 获取当地实时天气
    JSONObject getWeather();
}

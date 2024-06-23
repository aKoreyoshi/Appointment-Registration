package com.mac.ghpt.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mac.ghpt.model.dto.user.UserStatusDto;
import com.mac.ghpt.model.entity.user.UserInfo;
import com.mac.ghpt.model.dto.user.UserAuthDto;
import com.mac.ghpt.model.dto.user.UserLoginDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月05日, 13:55:25
 */
public interface UserService extends IService<UserInfo> {
    // 用户手机号登录
    Map<String, Object> login(UserLoginDto userLoginDto);

    // 用户认证
    void authUser(String phone, UserAuthDto userAuthDto);

    // 获取证件类型
    Map<String, Object> getCertificateType();

    // 根据token获取用户信息
    UserInfo getUserInfo(HttpServletRequest request);

    // 获取用户列表
    List<UserInfo> getUserList();

    // 后台完成认证
    void authdUser(String phone);

    // 更新用户状态
    void updateStatus(UserStatusDto userStatusDto);
}
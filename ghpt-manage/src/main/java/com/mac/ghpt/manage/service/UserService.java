package com.mac.ghpt.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mac.ghpt.model.dto.user.UserStatusDto;
import com.mac.ghpt.model.entity.user.UserInfo;

import java.util.List;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年04月12日, 10:29:12
 */
public interface UserService  {
    List<UserInfo> getUserList();

    void authUser(String phone);

    void updateStatus(UserStatusDto userStatusDto);
}
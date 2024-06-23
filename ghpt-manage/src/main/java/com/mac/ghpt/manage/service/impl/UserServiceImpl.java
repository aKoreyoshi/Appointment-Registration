package com.mac.ghpt.manage.service.impl;

import com.mac.ghpt.manage.service.UserService;
import com.mac.ghpt.model.dto.user.UserStatusDto;
import com.mac.ghpt.model.entity.user.UserInfo;
import com.mac.ghpt.user.client.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月12日, 10:30:44
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public List<UserInfo> getUserList() {
        List<UserInfo> userList = userFeignClient.getUserList();
        return userList;
    }

    @Override
    public void authUser(String phone) {
        userFeignClient.authdUser(phone);
    }

    @Override
    public void updateStatus(UserStatusDto userStatusDto) {
        userFeignClient.updateStatus(userStatusDto);
    }

}
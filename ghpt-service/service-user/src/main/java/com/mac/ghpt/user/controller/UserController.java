package com.mac.ghpt.user.controller;

import com.mac.ghpt.model.dto.user.UserStatusDto;
import com.mac.ghpt.model.entity.user.UserInfo;
import com.mac.ghpt.result.Result;
import com.mac.ghpt.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月05日, 13:56:13
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(description = "获取用户列表")
    @GetMapping("/getUserList")
    public List<UserInfo> getUserList() {
        List<UserInfo> userList =  userService.getUserList();
        return userList;
    }

    @Operation(description = "完成认证")
    @PutMapping("/authdUser/{phone}")
    public void authdUser(@PathVariable("phone") String phone) {
        userService.authdUser(phone);
    }

    @Operation(description = "更改用户状态")
    @PutMapping("/updateStatus")
    public void updateStatus(@RequestBody UserStatusDto userStatusDto) {
        userService.updateStatus(userStatusDto);
    }
}
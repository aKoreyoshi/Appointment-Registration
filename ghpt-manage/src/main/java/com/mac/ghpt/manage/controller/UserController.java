package com.mac.ghpt.manage.controller;

import com.mac.ghpt.manage.service.UserService;
import com.mac.ghpt.model.dto.user.UserStatusDto;
import com.mac.ghpt.model.entity.user.UserInfo;
import com.mac.ghpt.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月12日, 10:27:10
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/admin/hosp/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(description = "获取用户列表")
    @GetMapping("/getUserList")
    public Result getUserList(){
        List<UserInfo> userList = userService.getUserList();
        return Result.ok(userList);
    }

    @Operation(description = "认证")
    @PutMapping("/authUser/{phone}")
    public Result authUser(@PathVariable("phone") String phone) {
        userService.authUser(phone);
        return Result.ok();
    }

    @Operation(description = "更新用户状态")
    @PutMapping("/updateStatus")
    public Result updateStatus(@RequestBody UserStatusDto userStatusDto) {
        userService.updateStatus(userStatusDto);
        return Result.ok();
    }
}
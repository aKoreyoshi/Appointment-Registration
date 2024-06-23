package com.mac.ghpt.user.controller.api;

import com.mac.ghpt.model.entity.user.UserInfo;
import com.mac.ghpt.model.dto.user.UserAuthDto;
import com.mac.ghpt.model.dto.user.UserLoginDto;
import com.mac.ghpt.result.Result;
import com.mac.ghpt.user.service.UserService;
import com.mac.ghpt.utils.UserAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月05日, 15:06:10
 */
@Tag(name = "用户登录管理")
@RestController
@RequestMapping("/api/user")
public class ApiUserController {

    @Autowired
    private UserService userService;

    @Operation(description = "用户登录")
    @PostMapping("/loginUser")
    public Result loginUser(@RequestBody UserLoginDto userLoginDto) {
        Map<String, Object> loginUser = userService.login(userLoginDto);
        return Result.ok(loginUser);
    }

    @Operation(description = "获取用户信息")
    @GetMapping("/getUserInfo")
    public Result getUserInfo(HttpServletRequest request) {
        UserInfo userInfo = userService.getUserInfo(request);
        return Result.ok(userInfo);
    }

    @Operation(description = "用户认证")
    @PostMapping("/auth/authUser")
    public Result authUser(@RequestBody UserAuthDto userAuthDto, HttpServletRequest request) {
        // 两个参数，第一个为用户的phone唯一标识，另一个是认证的参数对象
        // 获取phone
        String phone = UserAuthUtil.getPhone(request);
        userService.authUser(phone, userAuthDto);
        return Result.ok();
    }

    @Operation(description = "获取证件类型")
    @GetMapping("/getCredentialsType")
    public Result getCredentialsType() {
        Map<String, Object> certificateType = userService.getCertificateType();
        return Result.ok(certificateType);
    }



}
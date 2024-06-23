package com.mac.ghpt.manage.controller;

import com.alibaba.fastjson2.JSONObject;
import com.mac.ghpt.manage.service.SysUserService;
import com.mac.ghpt.model.dto.system.LoginDto;
import com.mac.ghpt.model.entity.system.SysUser;
import com.mac.ghpt.model.vo.hosp.LoginVo;
import com.mac.ghpt.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月01日, 15:50:16
 */
@Tag(name = "用户登录接口")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    @Operation(description = "用户登录")
    @PostMapping("/login")
    public Result<LoginVo> login(@RequestBody LoginDto loginDto) {
        LoginVo loginVo = sysUserService.login(loginDto);
        return Result.ok(loginVo);
    }

    @Operation(description = "获取用户信息")
    @GetMapping("/getUserInfo")
    public Result getUserInfo(@RequestHeader(name = "token") String token) {
        SysUser sysUser = sysUserService.getUserInfo(token);
        return Result.ok(sysUser);
    }


    @Operation(description = "退出")
    @GetMapping("/logout")
    public Result logout(@RequestHeader(value = "token") String token) {
        sysUserService.logout(token);
        return Result.ok();
    }


    @GetMapping("/getWeather")
    public Result getWeather() {
        JSONObject weather = sysUserService.getWeather();
        return Result.ok(weather);
    }


}





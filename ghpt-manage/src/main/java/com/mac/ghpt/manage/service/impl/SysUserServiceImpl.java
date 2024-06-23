package com.mac.ghpt.manage.service.impl;

;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mac.ghpt.constant.Constants;
import com.mac.ghpt.exception.GhptException;
import com.mac.ghpt.manage.service.SysUserService;
import com.mac.ghpt.model.dto.system.LoginDto;
import com.mac.ghpt.model.entity.system.SysUser;
import com.mac.ghpt.manage.mapper.SysUserMapper;
import com.mac.ghpt.model.vo.hosp.HostAddressVo;
import com.mac.ghpt.model.vo.hosp.LoginVo;
import com.mac.ghpt.result.ResultCodeEnum;

import com.mac.ghpt.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月01日, 15:49:50
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 用户登录
     *
     * @param loginDto
     */
    @Override
    public LoginVo login(LoginDto loginDto) {
        // 获取到用户名
        String username = loginDto.getUserName();
        // 根据用户名查询数据库
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUserName, username);
        SysUser sysUser = sysUserMapper.selectOne(wrapper);
        if (sysUser == null) {
            throw new GhptException(ResultCodeEnum.USERNAME_NOT_EXIST);
        }
        // 验证密码是否正确
        String inputPassword = loginDto.getPassword();
        // MD5加密
        String MD5_inputPassword = DigestUtils.md5DigestAsHex(inputPassword.getBytes());
        if (!sysUser.getPassword().equals(MD5_inputPassword)) {
            throw new GhptException(ResultCodeEnum.PASSWORD_ERROR);
        }
        // 验证当前账号状态
        if (sysUser.getStatus() != 0) {
            throw new GhptException(ResultCodeEnum.USER_STATUS_ERROR);
        }
        // 生成用户令牌，保存到redis中，时间为30分钟
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.opsForValue().set(Constants.USER_TOKEN + token,
                JSON.toJSONString(sysUser), 30, TimeUnit.MINUTES);
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        loginVo.setRefresh_token("");
        return loginVo;
    }


    /**
     * 获取用户登录信息
     * @param token
     * @return
     */
    @Override
    public SysUser getUserInfo(String token) {
        String JSON_User = redisTemplate.opsForValue().get(Constants.USER_TOKEN + token);
        SysUser sysUser = JSON.parseObject(JSON_User, SysUser.class);
        return sysUser;
    }

    @Override
    public void logout(String token) {
        // 从redis中移除用户的token
        redisTemplate.delete(Constants.USER_TOKEN + token);
    }

    @Override
    public JSONObject getWeather() {
        // 获取本机ip，省市区等信息
        String host = "https://whois.pconline.com.cn/ipJson.jsp?json=true";
        RestTemplate restTemplate = new RestTemplate();
        String object = restTemplate.getForObject(host, String.class);
        HostAddressVo hostAddressVo = JSONObject.parseObject(object, HostAddressVo.class);
        JSONObject weather = this.weather(hostAddressVo);
        return weather;
    }

    // 获取具体天气
    private JSONObject weather(HostAddressVo hostAddressVo) {
        String host = "https://iweather.market.alicloudapi.com";
        String path = "/address";
        String method = "GET";
        String appcode = "17e365140dab4f94b0c4f02c474a8777";
        Map<String, String> headers = new HashMap<String, String>();
        // 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("needday", "1");
        querys.put("prov", hostAddressVo.getProvince());
        querys.put("city", hostAddressVo.getCity());
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            JSONObject weatherJson = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
            return weatherJson;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}

package com.mac.ghpt.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mac.ghpt.exception.GhptException;
import com.mac.ghpt.helper.JWTUtil;
import com.mac.ghpt.manage.client.HospFeignClient;
import com.mac.ghpt.model.dto.user.UserStatusDto;
import com.mac.ghpt.model.entity.system.Dict;
import com.mac.ghpt.model.entity.user.UserInfo;
import com.mac.ghpt.model.enums.AuthStatusEnum;
import com.mac.ghpt.model.enums.DictEnum;
import com.mac.ghpt.model.dto.user.UserAuthDto;
import com.mac.ghpt.model.dto.user.UserLoginDto;
import com.mac.ghpt.result.ResultCodeEnum;
import com.mac.ghpt.user.mapper.UserInfoMapper;
import com.mac.ghpt.user.service.UserService;
import com.mac.ghpt.utils.RandomIdGeneratorUtil;
import com.mac.ghpt.utils.UserAuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月05日, 13:55:41
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private HospFeignClient hospFeignClient;

    /**
     * 登录
     * @param userLoginDto
     * @return
     */
    @Override
    public Map<String, Object> login(UserLoginDto userLoginDto) {
        // 拿到手机号和验证码
        String phone = userLoginDto.getPhone();
        String code = userLoginDto.getCode();
        // 判断是否为空值
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            // 抛出参数错误的异常
            throw new GhptException(ResultCodeEnum.PARAM_ERROR);
        }
        // 判断验证码是否正确
        String redisCode = redisTemplate.opsForValue().get(phone);
        if (!code.equals(redisCode)) {
            // 抛出验证码错误的异常
            throw new GhptException(ResultCodeEnum.CODE_ERROR);
        }

        // 判断手机号是否为新用户，如果是新用户则自动完成注册
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getPhone, phone);
        UserInfo userInfo = baseMapper.selectOne(wrapper);
        if (userInfo == null) {
            // 自动注册
            userInfo = new UserInfo();
            userInfo.setId(RandomIdGeneratorUtil.generateRandomId());
            userInfo.setName("");
            userInfo.setPhone(phone);
            userInfo.setStatus(0);
            userInfo.setAuthStatus(0); // 此时未认证状态
            // 将用户信息添加到数据库
            baseMapper.insert(userInfo);
        }
        // 判断用户账号状态
        if (userInfo.getStatus() == 1) {
            // 用户账号被封禁
            throw new GhptException(ResultCodeEnum.USER_STATUS_ERROR);
        }

        // 生成JWT令牌
        Map<String, Object> map = new HashMap<>();
        map.put("phone", userInfo.getPhone());
//        String token = JWTHelper.createToken(userInfo.getId(), phone);
        String token = JWTUtil.createToken(userInfo.getId(), phone);
        // 同时将token放入redis
        redisTemplate.opsForValue().set("token",token,30, TimeUnit.DAYS);
        map.put("token", token);
        return map;
    }

    /**
     * 用户信息认证
     * @param phone
     * @param userAuthDto
     */
    @Override
    public void authUser(String phone, UserAuthDto userAuthDto) {
        // 根据phone查询到用户信息
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getPhone, phone);
        UserInfo userInfo = baseMapper.selectOne(wrapper);
        // 开始认证
        userInfo.setName(userAuthDto.getName());
        userInfo.setCertificateType(userAuthDto.getCertificateType());
        userInfo.setCertificateNo(userAuthDto.getCertificateNo());
        userInfo.setCertificateUrl(userAuthDto.getCertificateUrl());
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_ING.getStatus()); // 将认证状态改为 ‘认证中’
        // 更新用户信息
        baseMapper.updateById(userInfo);
        //TODO 后期完善可以添加此功能：发送短信通知用户，认证信息正在审核中
    }

    /**
     * 获取证件类型
     * @return
     */
    @Override
    public Map<String, Object> getCertificateType() {
        HashMap<String, Object> map = new HashMap<>();
        // 调用字典服务获取证件类型
        List<Dict> certificateTypeList = hospFeignClient.findChildrenByDictCode(DictEnum.CERTIFICATES_TYPE.getDictCode());
        map.put("certificateTypeList", certificateTypeList);
        return map;
    }

    /**
     * 根据token获取用户信息
     * @param request
     * @return
     */
    @Override
    public UserInfo getUserInfo(HttpServletRequest request) {
        // 解析token，拿到登录者的phone
        String phone = UserAuthUtil.getPhone(request);
        // 根据phone查询用户信息
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getPhone, phone);
        UserInfo userInfo = baseMapper.selectOne(wrapper);
        return userInfo;
    }

    @Override
    public List<UserInfo> getUserList() {
        List<UserInfo> userList = baseMapper.selectList(null);
        return userList;
    }


    @Override
    public void authdUser(String phone) {
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getPhone, phone);
        UserInfo userInfo = baseMapper.selectOne(wrapper);
        // 将认证状态改为 ‘认证完成’
        userInfo.setAuthStatus(2);
        baseMapper.updateById(userInfo);
    }

    @Override
    public void updateStatus(UserStatusDto userStatusDto) {
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getPhone, userStatusDto.getPhone());
        UserInfo userInfo = baseMapper.selectOne(wrapper);
        userInfo.setStatus(userStatusDto.getStatus());
        baseMapper.updateById(userInfo);
    }
}
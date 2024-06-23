package com.mac.ghpt.result;

import lombok.Getter;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月04日, 22:06:43
 */
@Getter
public enum ResultCodeEnum {

    // 常用枚举
    SUCCESS(200, "成功"),
    FAIL(201,"失败"),
    PARAM_ERROR(203,"参数错误"),
    SERVICE_ERROR(204,"服务异常"),
    DATA_ERROR(205,"数据异常"),

    // 用户相关枚举
    LOGIN_AUTH(208,"用户未登录"),
    USERNAME_NOT_EXIST(209,"用户名不存在"),
    USERNAME_ERROR(210,"用户名错误"),
    PASSWORD_ERROR(211,"密码错误"),
    CODE_ERROR(212,"验证码错误"),
    USER_STATUS_ERROR(213,"用户状态异常"),
    PHONE_IS_EXIST(214,"当前手机号已被使用"),
    CODE_SEND_FAIL(215,"验证码发送失败"),
    URL_ENCODE_ERROR( 216, "URL编码失败"),
    ILLEGAL_CALLBACK_REQUEST_ERROR( 217, "非法回调请求"),
    FETCH_ACCESS_TOKEN_FAILD( 218, "获取accessToken失败"),
    FETCH_USERINFO_ERROR( 219, "获取用户信息失败"),

    // 订单相关枚举
    PAY_RUNNING(220,"支付中"),
    CANCEL_ORDER_FAIL(225,"取消订单失败"),
    CANCEL_ORDER_NO(225,"不能取消订单"),

    // 医院相关
    NUMBER_NO(230,"可预约号不足"),
    TIME_NO(232,"当前不在预约时间内"),
    HOSPITAL_LOCK(235,"医院被锁定，暂时不能访问"),

    // 其他
    INNER_PATH_ACCESS_ERROR(240,"内部路径，不允许访问"),
    FILE_UPLOAD_FAILER(241,"文件上传失败"),
    ;

    private Integer code;
    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

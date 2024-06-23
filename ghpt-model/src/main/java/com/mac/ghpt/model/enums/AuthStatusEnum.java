package com.mac.ghpt.model.enums;

import lombok.Data;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月06日, 11:14:12
 */

public enum AuthStatusEnum {

    AUTH_NO(0, "未认证"),

    AUTH_ING(1, "认证中"),

    AUTH_SUCCESS(2, "认证通过"),

    AUTH_FAIL(-1, "认证失败");

    private Integer status;
    private String name;

    AuthStatusEnum(Integer status, String name) {
        this.status = status;
        this.name = name;
    }
    public static String getStatusNameByStatus(Integer status) {
        AuthStatusEnum arrObj[] = AuthStatusEnum.values();
        for (AuthStatusEnum obj : arrObj) {
            if (status.intValue() == obj.getStatus().intValue()) {
                return obj.getName();
            }
        }
        return "";
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
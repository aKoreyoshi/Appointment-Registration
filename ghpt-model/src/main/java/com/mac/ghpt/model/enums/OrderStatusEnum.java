package com.mac.ghpt.model.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月17日, 17:11:41
 */
public enum OrderStatusEnum {

    UNPAID(0,"预约成功，待支付"),
    PAID(1,"已支付" ),
//    GET_NUMBER(2,"已取号" ),
    CANCLE(-1,"取消预约"),
            ;

    private Integer status;
    private String comment ;

    public static String getStatusNameByStatus(Integer status) {
        OrderStatusEnum arrObj[] = OrderStatusEnum.values();
        for (OrderStatusEnum obj : arrObj) {
            if (status.intValue() == obj.getStatus().intValue()) {
                return obj.getComment();
            }
        }
        return "";
    }

    public static List<Map<String,Object>> getStatusList() {
        List<Map<String,Object>> list = new ArrayList<>();
        OrderStatusEnum arrObj[] = OrderStatusEnum.values();
        for (OrderStatusEnum obj : arrObj) {
            Map<String,Object> map = new HashMap<>();
            map.put("status", obj.getStatus());
            map.put("comment", obj.getComment());
            list.add(map);
        }
        return list;
    }

    OrderStatusEnum(Integer status, String comment ){
        this.comment=comment;
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
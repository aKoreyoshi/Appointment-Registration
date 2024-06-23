package com.mac.ghpt.model.vo.order;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月18日, 18:15:52
 */
@Data
public class OrderQueryVo implements Serializable {
    private static final Long serialVersionUID = 1L;
    private String doctorName;  // 医生姓名
    private String hospitalName;  // 医院名称
    private Long userId;  // 用户id
    private String patientName;  // 就诊人姓名
    private Integer status;  // 订单状态
}
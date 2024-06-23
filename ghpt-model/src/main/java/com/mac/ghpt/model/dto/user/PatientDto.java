package com.mac.ghpt.model.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月12日, 12:46:54
 */
@Data
public class PatientDto {
    private String name;
    private String phone;
    private Integer gender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date birthday;
    private String certificateType;
    private String certificateNo;
    private String ethnicGroup;
    private String[] district;
    private String address;
    private String contactName;
    private String contactPhone;
}
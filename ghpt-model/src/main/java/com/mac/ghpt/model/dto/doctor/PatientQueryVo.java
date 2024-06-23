package com.mac.ghpt.model.dto.doctor;

import lombok.Data;

import java.util.List;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月29日, 12:33:47
 */
@Data
public class PatientQueryVo {
    private List<String> patientNames;
    private String name;

}
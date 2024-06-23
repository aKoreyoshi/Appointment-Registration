package com.mac.ghpt.manage.controller;

import com.mac.ghpt.manage.service.HospitalService;
import com.mac.ghpt.model.dto.system.HospitalStatusDto;
import com.mac.ghpt.result.Result;
import com.mac.ghpt.utils.FileProcessingUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月08日, 21:23:00
 */
@Tag(name = "医院信息管理接口")
@RestController
@RequestMapping("/admin/hosp/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @Operation(description = "保存医院配置")
    @PostMapping("/saveHosp")
    public Result saveHospital(HttpServletRequest request) {
        try {
            Part filePart = request.getPart("file");
            // 获取文件名
            String fileName = filePart.getSubmittedFileName();
            // 保存文件到指定目录
            String path = "D:\\gdesign\\example" + "\\" + fileName;
            // 判断文件是否存在
            boolean fileExists = FileProcessingUtil.isFileExists(path);
            if (fileExists) {
                // 文件已存在，删除原本文件
                FileProcessingUtil.deleteFile(path);
                // 保存
                filePart.write(path);
            } else {
                // 文件不存在，直接保存
                filePart.write(path);
            }
            // 调用添加医院接口
            hospitalService.saveHospital(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return Result.ok();
    }


    @Operation(description = "展示医院信息")
    @GetMapping("/getHospInfo")
    public Map<String, Object> getHospitalInfo() {
        Map<String, Object> hospitalInfo = hospitalService.getHospitalInfo();
        return hospitalInfo;
    }

    @Operation(description = "后台获取医院信息")
    @GetMapping("/getHospital")
    public Result getHospital() {
        Map<String, Object> hospitalInfo = hospitalService.getHospitalInfo();
        return Result.ok(hospitalInfo);
    }

    @Operation(description = "修改医院状态")
    @PostMapping("/updateHospStatus")
    public Result updateHospStatus(@RequestBody HospitalStatusDto hospitalStatusDto) {
        hospitalService.updateHospStatus(hospitalStatusDto);
        return Result.ok();
    }


}

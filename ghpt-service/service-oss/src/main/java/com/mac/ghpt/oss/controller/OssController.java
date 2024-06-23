package com.mac.ghpt.oss.controller;

import com.mac.ghpt.oss.service.OssService;
import com.mac.ghpt.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年05月29日, 16:35:53
 */
@Tag(name = "OSS对象存储")
@RestController
@RequestMapping("/api/oss")
public class OssController {

    private OssService ossService;

    @Autowired
    public OssController(OssService ossService) {
        this.ossService = ossService;
    }

    @Operation(description = "上传证件")
    @PostMapping("/fileUpLoad")
    public Result upload(@RequestParam(value = "file") MultipartFile file) {
        String url = ossService.upload(file);
        return Result.ok(url);
    }
}
package com.mac.ghpt.manage.controller;

import com.alibaba.excel.EasyExcel;
import com.mac.ghpt.manage.listener.DictListener;
import com.mac.ghpt.manage.service.DictService;
import com.mac.ghpt.model.dto.system.DictDto;
import com.mac.ghpt.model.entity.system.Dict;
import com.mac.ghpt.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月14日, 12:48:43
 */
@Tag(name = "数据字典管理")
@RestController
@RequestMapping("/admin/hosp/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    @Operation(description = "导入数据字典")
    @PostMapping("/importData")
    public Result importData(MultipartFile file) {
        dictService.importData(file);
        return Result.ok();
    }

    @Operation(description = "导出数据字典")
    @GetMapping("/exportData")
    public void exportData(HttpServletResponse response) {
        dictService.exportData(response);
    }

    @Operation(description = "获取数据构建树形结构")
    @GetMapping("/buildTrees")
    public Result buildTrees() {
        List<Dict> dictList = dictService.buildTrees();
        return Result.ok(dictList);
    }

    @Operation(description = "判断是否为空")
    @GetMapping("/isEmpty")
    public Result isEmpty() {
        Boolean isEmpty = dictService.isEmpty();
        return Result.ok(isEmpty);
    }

    @Operation(description = "根据dictCode获取子元素")
    @GetMapping("/findChildrenByDictCode/{dictCode}")
    public List<Dict> findChildrenByDictCode(@PathVariable("dictCode") String dictCode) {
        List<Dict> dictList = dictService.findChildrenByDictCode(dictCode);
        return dictList;
    }

}

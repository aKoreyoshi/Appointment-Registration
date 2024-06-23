package com.mac.ghpt.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mac.ghpt.model.entity.system.Dict;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年03月14日, 12:48:56
 */
public interface DictService extends IService<Dict> {
    // 导入数据字典
    void importData(MultipartFile file);

    // 构建树形结构
    List<Dict> buildTrees();

    // 判断数据字典表是否为空
    Boolean isEmpty();

    // 导出数据字典
    void exportData(HttpServletResponse response);

    // 根据dictcode获取子元素
    List<Dict> findChildrenByDictCode(String dictCode);
}

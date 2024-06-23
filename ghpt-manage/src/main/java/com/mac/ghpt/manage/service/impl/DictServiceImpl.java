package com.mac.ghpt.manage.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mac.ghpt.helper.DictHelper;
import com.mac.ghpt.manage.listener.DictListener;
import com.mac.ghpt.manage.mapper.DictMapper;
import com.mac.ghpt.manage.service.DictService;
import com.mac.ghpt.model.dto.system.DictDto;
import com.mac.ghpt.model.entity.system.Dict;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月14日, 12:49:33
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Autowired
    private DictMapper dictMapper;

    /**
     * 导入数据
     * @param file
     */
    @Override
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), DictDto.class, new DictListener(dictMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 构建树形结构
     *
     * @return
     */
    @Override
    public List<Dict> buildTrees() {
        // 先获取所有dict数据
        List<Dict> dictList = dictMapper.selectList(null);
        // 构建树形结构
        List<Dict> dicts = DictHelper.build(dictList);
        return dicts;
    }


    /**
     * 判断数据是否为空
     *
     * @return
     */
    @Override
    public Boolean isEmpty() {
        List<Dict> dictList = dictMapper.selectList(null);
        if (dictList.isEmpty()) {
            // 数据集合为null返回 true
            return true;
        } else {
            return false;
        }
    }


    /**
     * 导出数据字典
     *
     * @param response
     */
    @Override
    public void exportData(HttpServletResponse response) {
        String fileName = null;
        // 防止中文乱码，这里使用URLEncoder处理
        fileName = URLEncoder.encode("dict", StandardCharsets.UTF_8);
        // 设置响应头信息
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition",
                "attachment; filename=" + fileName + ".xlsx");

        // 查询数据库，获取数据
        List<Dict> dictList = dictMapper.selectList(null);
        List<DictDto> dictDtos = new ArrayList<>();
        // 将数据复制到dictDto中
        for (Dict dict : dictList) {
            DictDto dictDto = new DictDto();
            BeanUtils.copyProperties(dict,dictDto);
            dictDtos.add(dictDto);
        }
        // 调用方法实现write操作
        try {
            EasyExcel.write(response.getOutputStream(), DictDto.class).sheet("dict").doWrite(dictDtos);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据dictCode查询子节点
     * @param dictCode
     * @return
     */
    @Override
    public List<Dict> findChildrenByDictCode(String dictCode) {
        List<Dict> dicts = baseMapper.selectList(null);
        // 根据dictCode获取到id
        LambdaQueryWrapper<Dict> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dict::getDictCode, dictCode);
        Dict dict = dictMapper.selectOne(wrapper);
        List<Dict> list = new ArrayList<>();
        if (dict!= null) {
//            // 根据id获取到子节点
//            wrapper.clear();
//            wrapper.eq(Dict::getParentId, dict.getId());
//            List<Dict> childrenList = dictMapper.selectList(wrapper);
//            // 循环，查询子节点
//            childrenList.stream().forEach(item -> {
//                item.getChildren().add(this.findChild(item,dicts));
//            });
            list.add(findChild(dict,dicts));
            return list;
        }
        return null;
    }

    private Dict findChild(Dict dict,List<Dict> dictList) {
        dict.setChildren(new ArrayList<>());
        dictList.stream().forEach(item -> {
            if (dict.getId().longValue() == item.getParentId().longValue()) {
                dict.getChildren().add(findChild(item,dictList));
            }
        });
        return dict;
    }
}

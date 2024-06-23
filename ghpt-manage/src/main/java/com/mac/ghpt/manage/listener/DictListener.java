package com.mac.ghpt.manage.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson2.JSON;
import com.mac.ghpt.manage.mapper.DictMapper;
import com.mac.ghpt.model.dto.system.DictDto;
import com.mac.ghpt.model.entity.system.Dict;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.sql.Timestamp;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月14日, 13:28:58
 */
@Slf4j
public class DictListener implements ReadListener<DictDto> {

    private DictMapper dictMapper;

    public DictListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    /**
     * 这个每一条数据解析都会来调用，也就是一条数据一条数据的读取
     *
     * @param dictDto
     * @param analysisContext
     */
    @Override
    public void invoke(DictDto dictDto, AnalysisContext analysisContext) {
        // log.info("解析到一条数据:{}", JSON.toJSONString(dictDto));
        // 将DictDto对象中数据 复制到 Dict对象
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictDto, dict);
        dict.setCreateTime(new Timestamp(System.currentTimeMillis()));
        dict.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        dict.setIsDeleted(0);
        dictMapper.insert(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}

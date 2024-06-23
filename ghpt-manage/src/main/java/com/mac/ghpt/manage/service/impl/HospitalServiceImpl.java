package com.mac.ghpt.manage.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mac.ghpt.manage.mapper.BookingRuleMapper;
import com.mac.ghpt.manage.mapper.HospitalSetMapper;
import com.mac.ghpt.manage.repository.HospitalRepository;
import com.mac.ghpt.manage.service.HospitalService;
import com.mac.ghpt.model.dto.system.HospitalStatusDto;
import com.mac.ghpt.model.entity.system.BookingRule;
import com.mac.ghpt.model.entity.system.Hospital;
import com.mac.ghpt.model.entity.system.HospitalSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月08日, 21:21:30
 */
@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalSetMapper hospitalSetMapper;
    @Autowired
    private BookingRuleMapper bookingRuleMapper;

    /**
     * 添加医院信息
     *
     * @param filePath
     */
    @Override
    public void saveHospital(String filePath) {
        Hospital hospital = new Hospital();
        Path path = Path.of(filePath);
        try {
            String jsonString = Files.readString(path);
            // 将json字符串转化为java对象
            hospital = JSON.parseObject(jsonString, Hospital.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Constants.HOSP_CODE = hospital.getHospitalCode();
        // 传递的图片涉及到Base64编码问题，所以需要将logoData中所有的 “ ” 替换为 “+”
        String logoData = hospital.getLogoData();
        logoData = logoData.replaceAll(" ", "+");
        hospital.setLogoData(logoData);
        // 根据hospitalCode判断当前信息是否存在(直接存到MySQL数据库，这里不走MongoDB)

        HospitalSet hospitalSet = hospitalSetMapper.selectOne(new LambdaQueryWrapper<HospitalSet>()
                .eq(HospitalSet::getHospitalCode, hospital.getHospitalCode()));
        if (hospitalSet != null) {
            // 存在，更新
            hospitalSet.setUpdateTime(new Date());
            hospitalSet.setIsDeleted(0);
            hospitalSetMapper.updateById(hospitalSet);
        } else {
            // 不存在，新增
            // hospital对象
            HospitalSet hospitalSet1 = new HospitalSet();
            hospitalSet1.setHospitalCode(hospital.getHospitalCode());
            hospitalSet1.setHospitalName(hospital.getHospitalName());
            hospitalSet1.setHospitalGrade(hospital.getHospitalGrade());
            hospitalSet1.setLogoData(hospital.getLogoData());
            hospitalSet1.setStatus(hospital.getStatus());
            hospitalSet1.setAddress(hospital.getAddress());
            hospitalSet1.setPhone(hospital.getPhone());
            hospitalSet1.setIntro(hospital.getIntro());
            hospitalSet1.setRoute(hospital.getRoute());
            hospitalSet1.setCreateTime(new Date());
            hospitalSet1.setUpdateTime(new Date());
            hospitalSet1.setIsDeleted(0);
            // 添加操作
            hospitalSetMapper.insert(hospitalSet1);
        }

        BookingRule bookingRule = bookingRuleMapper.selectOne(new LambdaQueryWrapper<BookingRule>()
                .eq(BookingRule::getHospitalCode, hospital.getHospitalCode()));
        if (bookingRule != null) {
            bookingRule.setUpdateTime(new Date());
            bookingRule.setIsDeleted(0);
            bookingRuleMapper.updateById(bookingRule);
        } else {
            // bookingRule对象
            BookingRule bookingRule1 = new BookingRule();
            bookingRule1.setHospitalCode(hospital.getHospitalCode());
            bookingRule1.setCycle(hospital.getRules().getCycle());
            bookingRule1.setReleaseTime(hospital.getRules().getReleaseTime());
            bookingRule1.setStopTime(hospital.getRules().getStopTime());
            bookingRule1.setQuitDay(hospital.getRules().getQuitDay());
            bookingRule1.setQuitTime(hospital.getRules().getQuitTime());
            // 处理rule
            StringBuffer buffer = new StringBuffer();
            hospital.getRules().getRule().forEach(rule -> {
                buffer.append(rule);
            });
            bookingRule1.setRule(buffer.toString());
            bookingRule1.setCreateTime(new Date());
            bookingRule1.setUpdateTime(new Date());
            bookingRule1.setIsDeleted(0);
            // 添加操作
            bookingRuleMapper.insert(bookingRule1);
        }
    }

    @Override
    public Map<String, Object> getHospitalInfo() {
        // 这里查询所有，但实际上只有一条数据
        // 查询医院信息
        List<HospitalSet> hospitalSets = hospitalSetMapper.selectList(null);
        // 所以直接获取索引为0的数据，得到hospital对象
        HospitalSet hospitalSet = hospitalSets.get(0);
        // 查询预约规则同上一样
        List<BookingRule> bookingRules = bookingRuleMapper.selectList(null);
        BookingRule bookingRule = bookingRules.get(0);
        // 将数据放入map集合
        Map<String, Object> map = new HashMap<>();
        map.put("hospitalSet", hospitalSet);
        map.put("bookingRule", bookingRule);
        return map;
    }

    /**
     * 更新医院上线状态
     *
     * @param hospitalStatusDto
     */
    @Override
    public void updateHospStatus(HospitalStatusDto hospitalStatusDto) {
        // 获取医院编号
        String hospitalCode = hospitalStatusDto.getHospitalCode();
        // 根据医院编号获取医院对象
        LambdaQueryWrapper<HospitalSet> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(HospitalSet::getHospitalCode,hospitalCode);
        HospitalSet hospitalSet = hospitalSetMapper.selectOne(wrapper);
        // 重新设置状态
        hospitalSet.setStatus(hospitalStatusDto.getStatus());
        hospitalSet.setUpdateTime(new Date());

        // 修改数据
        hospitalSetMapper.updateById(hospitalSet);
    }


}

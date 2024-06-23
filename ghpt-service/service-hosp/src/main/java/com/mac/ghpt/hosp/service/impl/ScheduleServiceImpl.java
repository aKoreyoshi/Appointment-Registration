package com.mac.ghpt.hosp.service.impl;


import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mac.ghpt.exception.GhptException;
import com.mac.ghpt.hosp.service.ScheduleService;
import com.mac.ghpt.manage.client.HospFeignClient;
import com.mac.ghpt.model.entity.system.BookingRule;
import com.mac.ghpt.model.entity.system.Department;
import com.mac.ghpt.model.entity.system.HospitalSet;
import com.mac.ghpt.model.entity.system.Schedule;
import com.mac.ghpt.model.vo.hosp.ScheduleRuleVo;
import com.mac.ghpt.model.vo.order.ScheduleOrderVo;
import com.mac.ghpt.result.ResultCodeEnum;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月13日, 17:33:01
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private HospFeignClient hospFeignClient;
    private MongoTemplate mongoTemplate;

    @Autowired
    public void setHospFeignClient(HospFeignClient hospFeignClient, MongoTemplate mongoTemplate) {
        this.hospFeignClient = hospFeignClient;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 获取排班规则数据
     *
     * @param currentPage
     * @param pageSize
     * @param departmentCode
     * @return
     */
    @Override
    public Map<String, Object> getBookingScheduleRule(Integer currentPage, Integer pageSize, String departmentCode) {
        // 创建一个map集合，用来保存最终的数据
        Map<String, Object> result = new HashMap<>();
        // 远程调用获取医院数据
        // 这里map一共有两部分数据，分别为hospitalSet 和 bookingRule
        Map<String, Object> hospitalInfo = hospFeignClient.getHospitalInfo();
        if (hospitalInfo == null || hospitalInfo.isEmpty()) {
            throw new GhptException(ResultCodeEnum.DATA_ERROR);
        }
        LinkedHashMap<String, String> hospitalSet = (LinkedHashMap<String, String>) hospitalInfo.get("hospitalSet");
        LinkedHashMap<String, Object> bookingRule = (LinkedHashMap<String, Object>) hospitalInfo.get("bookingRule");
        // 获取可预约日期分页
        IPage iPage = this.getListDate(currentPage, pageSize, bookingRule);
        //当前页可预约日期
        List<Date> dateList = iPage.getRecords();
        // 处理日期
        List<String> dates = new ArrayList<>();
        dateList.forEach(date -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = dateFormat.format(date);
            dates.add(dateString);
        });
        //获取可预约日期科室剩余预约数
        Criteria criteria = Criteria.where("hospitalCode").is(hospitalSet.get("hospitalCode"))
                .and("departmentCode").is(departmentCode)
                .and("workDate").in(dates);

        // 优化之后的聚合管道
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate").first("workDate").as("workDate")
                        .count().as("doctorCount")
                        .sum("availableNumber").as("totalAvailableNumber")  //求和
                        .sum("reservedNumber").as("totalReservedNumber"),
                Aggregation.project().
                        andExpression("{$toDate: '$workDate'}").as("workDate")
                        .and("doctorCount").as("doctorCount")
                        .and("totalAvailableNumber").as("availableNumber")
                        .and("totalReservedNumber").as("reservedNumber"));
        // 调用mongodb的方法
        AggregationResults<ScheduleRuleVo> aggregate =
                mongoTemplate.aggregate(agg, Schedule.class, ScheduleRuleVo.class);
        // 获取科室剩余预约数 得到可预约的剩余数
        List<ScheduleRuleVo> mappedResults = aggregate.getMappedResults();
        //合并数据 将统计数据ScheduleVo根据“安排日期”合并到BookingRuleVo
//        if (!CollectionUtils.isNotEmpty(mappedResults)) {
        // 转为map集合
        Map<Date, ScheduleRuleVo> scheduleVoMap = mappedResults.stream().collect(Collectors.toMap(
                ScheduleRuleVo::getWorkDate, scheduleRuleVo -> scheduleRuleVo));
//        }
        // 获取可预约规则
        List<ScheduleRuleVo> scheduleRuleVoList = new ArrayList<>();
        for (int i = 0, len = dateList.size(); i < len; i++) {
            // 获取日期
            Date date = dateList.get(i);
            // 根据上面定义的map集合的结构，key就是date日期
            ScheduleRuleVo scheduleRuleVo = scheduleVoMap.get(date);
            // 为null 说明当天没有排班
            if (null == scheduleRuleVo) {
                scheduleRuleVo = new ScheduleRuleVo();
                scheduleRuleVo.setDoctorCount(0);
                scheduleRuleVo.setAvailableNumber(-1);
            }
            // 设置可预约日期
            scheduleRuleVo.setWorkDate(date);
            scheduleRuleVo.setWorkDateMd(date);
            // 根据当前日期计算周几
//            String dayOfWeek = this.getDayOfWeek(new DateTime(date));
            String dayOfWeek = getDayOfWeek(new DateTime(date));
            scheduleRuleVo.setDayOfWeek(dayOfWeek);
            //最后一页最后一条记录为即将预约   状态 0：正常 1：即将放号 -1：当天已停止挂号
            if (i == len - 1 && currentPage == iPage.getPages()) {
                scheduleRuleVo.setStatus(1);
            } else {
                scheduleRuleVo.setStatus(0);
            }
            //当天预约如果过了停号时间， 不能预约
            if (i == 0 && currentPage == 1) {
                DateTime stopTime = this.getDateTime(new Date(), bookingRule.get("stopTime").toString());
                if (stopTime.isBeforeNow()) {
                    scheduleRuleVo.setStatus(-1);
                }
            }
            scheduleRuleVoList.add(scheduleRuleVo);
        }
        // 最后将所有数据放入result集合
        result.put("scheduleRuleVoList", scheduleRuleVoList);
        result.put("total", iPage.getTotal());
        //其他基础数据
        Map<String, String> rule = new HashMap<>();
        rule.put("hospitalName", hospitalSet.get("hospitalName"));
        Department department =
                hospFeignClient.getDepartment(hospitalSet.get("hospitalCode"), departmentCode);
        rule.put("bigDepartmentName", department.getBigDepartmentName());
        rule.put("departmentName", department.getDepartmentName());
        // 月
        rule.put("workDateString", new DateTime().toString("yyyy年-MM月"));
        rule.put("releaseTime", bookingRule.get("releaseTime").toString());
        rule.put("stopTime", bookingRule.get("stopTime").toString());
        result.put("rule", rule);
        return result;
    }

    /**
     * 获取排班列表
     *
     * @param departmentCode
     * @param workDate
     * @return
     */
    @Override
    public List<Schedule> getDetailSchedule(String departmentCode, String workDate) {
        List<Schedule> schedules = hospFeignClient.getDetailSchedule(departmentCode, workDate);
        Map<String, Object> hospitalInfo = hospFeignClient.getHospitalInfo();
        // TODO 这里为什么 HashMap 转变为了 LinkedHashMap
        LinkedHashMap<String, String> hospitalSet = (LinkedHashMap<String, String>) hospitalInfo.get("hospitalSet");
        Department department = hospFeignClient.getDepartment(hospitalSet.get("hospitalCode"), departmentCode);
        //把得到list集合遍历，向设置其他值：医院名称、科室名称、日期对应星期
        schedules.stream().forEach(item -> {
            item.getParam().put("hospitalName", hospitalSet.get("hospitalName"));
            item.getParam().put("departmentName", department.getDepartmentName());
            item.getParam().put("dayOfWeek", this.getDayOfWeek(new DateTime(item.getWorkDate())));
        });
        return schedules;
    }


    /**
     * 根据id获取schedule
     *
     * @param scheduleId
     * @return
     */
    @Override
    public Schedule getScheduleById(String scheduleId) {
        Schedule schedule = hospFeignClient.getScheduleById(scheduleId);
//        System.out.println("schedule" + packSchedule(schedule));
        return packSchedule(schedule);
    }


    /**
     * 更新排班数据 更新后通知前端
     * @param schedule
     */
    @Override
    public void updateSchedule(Schedule schedule) {
        schedule.setUpdateTime(new Date());
        hospFeignClient.updateSchedule(schedule);
    }

    /**
     * 根据当天日期计算为周几
     *
     * @param dateTime
     * @return
     */
    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
            default:
                break;
        }
        return dayOfWeek;
    }

    private IPage getListDate(Integer currentPage, Integer pageSize, LinkedHashMap<String, Object> bookingRule) {
        // 获取当天的放号时间
        DateTime releaseTime = this.getDateTime(new Date(), bookingRule.get("releaseTime").toString());
        // 获取可预约周期
        Integer cycle = Integer.parseInt(bookingRule.get("cycle").toString());
        // 如果当天放号时间已过，则预约周期的下一天为即将放号时间 周期加1
        if (releaseTime.isBeforeNow()) cycle += 1;
        // 获取可预约的日期，最后一天为放号时间的前一天
        ArrayList<Date> dates = new ArrayList<>();
        for (int i = 0; i < cycle; i++) {
            // 计算当前预约日期
            DateTime dateTime = new DateTime().plusDays(i);
            String dateString = dateTime.toString("yyyy-MM-dd");
            // 将日期字符串转换为 DateTime 对象，并设置时间为 08:00:00
            DateTime time = new DateTime(dateString).withHourOfDay(8).withMinuteOfHour(0).withSecondOfMinute(0);
            dates.add(new DateTime(time).toDate());
        }
        // 日期分页，由于预约周期不一样，页面一排最多显示7天数据，多了就要分页显示
        List<Date> pageList = new ArrayList<>();
        int start = (currentPage - 1) * pageSize;
        int end = currentPage * pageSize;
        if (end > dates.size()) {
            end = dates.size();
        }
        for (int i = start; i < end; i++) {
            pageList.add(dates.get(i));
        }
        // 如果数据大于7，进行分页
        IPage<Date> iPage = new Page<>(currentPage, 7, dates.size());
        iPage.setRecords(pageList);
        return iPage;
    }

    /**
     * 将Date日期（yyyy-MM-dd HH:mm）转换为DateTime
     */
    private DateTime getDateTime(Date date, String timeString) {
        String dateTimeString = new DateTime(date).toString("yyyy-MM-dd") + " " + timeString;
        DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(dateTimeString);
        return dateTime;
    }


    // 封装排班信息
    private Schedule packSchedule(Schedule schedule) {
        // 设置医院名称
        Map<String, Object> hospitalInfo = hospFeignClient.getHospitalInfo();
        LinkedHashMap<String, String> hospitalSet = (LinkedHashMap<String, String>) hospitalInfo.get("hospitalSet");
        String hospitalName = hospitalSet.get("hospitalName");
        schedule.getParam().put("hospitalName", hospitalName);
        //设置科室名称
        Department department = hospFeignClient
                .getDepartment(schedule.getHospitalCode(), schedule.getDepartmentCode());
        schedule.getParam().put("departmentName", department.getDepartmentName());
        //设置日期及对应星期
        schedule.getParam().put("dayOfWeek", this.getDayOfWeek(new DateTime(schedule.getWorkDate())));
        return schedule;
    }

}
package com.mac.ghpt.hosp;

import com.mac.ghpt.model.entity.system.Schedule;
import com.mac.ghpt.model.vo.hosp.ScheduleRuleVo;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年04月16日, 10:38:13
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class Test {

    @Autowired
    private MongoTemplate mongoTemplate;



    @org.junit.Test
    public  void test(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); // 设置为中国标准时间
        String date = dateFormat.format(new Date());
        Criteria criteria = Criteria.where("hospitalCode").is("1000_0")
                .and("departmentCode").is("200040878")
                .and("workDate").is(date);
        Aggregation aggregation = Aggregation.newAggregation(
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
        AggregationResults<ScheduleRuleVo> aggregate =
                mongoTemplate.aggregate(aggregation, Schedule.class, ScheduleRuleVo.class);
        List<ScheduleRuleVo> mappedResults = aggregate.getMappedResults();
//        System.out.println("mappedResults"+mappedResults);
        Map<Date, ScheduleRuleVo> map = mappedResults.stream()
                .collect(Collectors.toMap(ScheduleRuleVo::getWorkDate, scheduleRuleVo -> scheduleRuleVo));
        System.out.println("map集合的key" + map.keySet());
        System.out.println("传入的key" + new Date());
    }

}
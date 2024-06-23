package com.mac.ghpt.manage.repository;

import com.mac.ghpt.model.entity.system.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年03月11日, 19:25:25
 */
@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    Schedule findByHospitalCodeAndScheduleId(String hospitalCode, String scheduleId);

    List<Schedule> findByDepartmentCodeAndWorkDate(String departmentCode, String workDate);

    Schedule findByScheduleId(String scheduleId);

//    @Query("{'scheduleId': ?0}")
//    void updateAvailableNumberByScheduleId(String scheduleId, int availableNumber);
}

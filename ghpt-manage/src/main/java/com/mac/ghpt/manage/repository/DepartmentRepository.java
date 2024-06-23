package com.mac.ghpt.manage.repository;

import com.mac.ghpt.model.entity.system.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年03月13日, 11:25:09
 */
@Repository
public interface DepartmentRepository extends MongoRepository<Department, String>{
    Department findByHospitalCodeAndDepartmentCode(String hospitalCode, String departmentCode);
}

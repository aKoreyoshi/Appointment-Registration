package com.mac.ghpt.manage.repository;

import com.mac.ghpt.model.entity.system.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: 马聪
 * @function:
 * @version: 1.0
 * @date: 2024年03月08日, 21:57:11
 */
@Repository
public interface HospitalRepository extends MongoRepository<Hospital, String>{
    Hospital findByHospitalCode(String hospitalCode);
}

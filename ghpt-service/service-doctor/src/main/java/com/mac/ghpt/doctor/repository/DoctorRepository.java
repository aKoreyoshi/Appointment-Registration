package com.mac.ghpt.doctor.repository;

import com.mac.ghpt.model.entity.system.DoctorSet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: 马聪
 * @version: 1.0
 * @date: 2024年04月27日, 15:52:54
 */
@Repository
public interface DoctorRepository extends MongoRepository<DoctorSet,String> {
    DoctorSet findByDoctorId(String doctorId);
}
package com.indytskyi.userserviceairport.repository;

import com.indytskyi.userserviceairport.model.PassengerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends JpaRepository<PassengerEntity, Long> {
}

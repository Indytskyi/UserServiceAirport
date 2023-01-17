package com.indytskyi.userserviceairport.repository;

import com.indytskyi.userserviceairport.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}

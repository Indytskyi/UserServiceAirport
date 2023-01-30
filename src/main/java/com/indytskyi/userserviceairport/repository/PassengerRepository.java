package com.indytskyi.userserviceairport.repository;

import com.indytskyi.userserviceairport.dto.PassengerResponseDto;
import com.indytskyi.userserviceairport.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    @Query("select new com.indytskyi.userserviceairport.dto.PassengerResponseDto(p.firstName, p.lastName,p.dataBirth, p.gender) from Passenger p")
    Optional<List<PassengerResponseDto>> findAllBy();
    Optional<Passenger> findByUserEmail(String email);
}

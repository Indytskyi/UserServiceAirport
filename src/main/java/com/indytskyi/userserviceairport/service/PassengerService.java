package com.indytskyi.userserviceairport.service;

import com.indytskyi.userserviceairport.dto.PassengerDto;

import java.util.List;

public interface PassengerService {

    List<PassengerDto> getAllPassenger();


    PassengerDto getPassengerByEmail(String email);
}

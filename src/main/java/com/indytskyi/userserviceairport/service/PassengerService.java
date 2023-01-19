package com.indytskyi.userserviceairport.service;

import com.indytskyi.userserviceairport.dto.PassengerDto;
import com.indytskyi.userserviceairport.dto.RegisterRequest;
import com.indytskyi.userserviceairport.model.Passenger;
import com.indytskyi.userserviceairport.model.User;

import java.util.List;

public interface PassengerService {
    List<PassengerDto> getAllPassenger();

    PassengerDto getPassengerByEmail(String email);

    Object getAllPassengerOrByEmail(String email);

    Passenger createPassenger(RegisterRequest request);
}

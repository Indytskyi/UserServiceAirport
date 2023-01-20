package com.indytskyi.userserviceairport.service;

import com.indytskyi.userserviceairport.dto.PassengerRequestDto;
import com.indytskyi.userserviceairport.dto.PassengerResponseDto;
import com.indytskyi.userserviceairport.dto.RegisterRequest;
import com.indytskyi.userserviceairport.model.Passenger;
import com.indytskyi.userserviceairport.model.User;

import java.util.List;

public interface PassengerService {
    List<PassengerResponseDto> getAllPassenger();

    PassengerResponseDto getPassengerByEmail(User user);

    Passenger createPassenger(RegisterRequest request);

    PassengerResponseDto updatePassengerByEmail(PassengerRequestDto requestDto, String bearerToken);
}

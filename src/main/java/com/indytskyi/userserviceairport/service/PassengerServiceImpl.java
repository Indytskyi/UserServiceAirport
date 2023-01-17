package com.indytskyi.userserviceairport.service;

import com.indytskyi.userserviceairport.dto.PassengerDto;
import com.indytskyi.userserviceairport.repository.PassengerRepository;
import com.indytskyi.userserviceairport.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService{

    private final PassengerRepository passengerRepository;
    private final UserRepository userRepository;
//    private final PassengerDtoMapper passengerDtoMapper;

    @Override
    public List<PassengerDto> getAllPassenger() {
        return passengerRepository.findAllBy().orElseThrow();
    }

    @Override
    public PassengerDto getPassengerByEmail(String email) {
        var userByEmail =  userRepository.findByEmail(email).orElseThrow();
        return new PassengerDto();
//        return passengerDtoMapper.toPassengerDto(userByEmail.getPassenger());
//        return passengerRepository.findPassengerByEmail(email).orElseThrow();
    }
}

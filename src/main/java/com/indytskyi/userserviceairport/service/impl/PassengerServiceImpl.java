package com.indytskyi.userserviceairport.service.impl;

import com.indytskyi.userserviceairport.dto.PassengerDto;
import com.indytskyi.userserviceairport.dto.RegisterRequest;
import com.indytskyi.userserviceairport.exception.ApiValidationException;
import com.indytskyi.userserviceairport.exception.ErrorResponse;
import com.indytskyi.userserviceairport.model.Passenger;
import com.indytskyi.userserviceairport.model.enums.Gender;
import com.indytskyi.userserviceairport.repository.PassengerRepository;
import com.indytskyi.userserviceairport.repository.UserRepository;
import com.indytskyi.userserviceairport.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<PassengerDto> getAllPassenger() {
        return passengerRepository.findAllBy()
                .orElseThrow(() -> new ApiValidationException(
                        List.of(new ErrorResponse("passengers", "No passengers yet"))
                ));
    }

    @Override
    public PassengerDto getPassengerByEmail(String email) {
        var userByEmail =  userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiValidationException(
                        List.of(new ErrorResponse("email", "This email doesn`t exist"))
                ));
        return modelMapper.map(userByEmail.getPassenger(), PassengerDto.class);
    }

    @Override
    public Object getAllPassengerOrByEmail(String email) {
        return email.equals("ALL")
                ? getAllPassenger()
                : getPassengerByEmail(email);
    }

    @Override
    public Passenger createPassenger(RegisterRequest request) {
        return Passenger.of()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(Gender.valueOf(request.getGender()))
                .photo(request.getPhoto())
                .dataBirth(request.getDateOfBirth())
                .build();
    }
}

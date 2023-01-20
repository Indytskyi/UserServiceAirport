package com.indytskyi.userserviceairport.service.impl;

import com.indytskyi.userserviceairport.dto.PassengerRequestDto;
import com.indytskyi.userserviceairport.dto.PassengerResponseDto;
import com.indytskyi.userserviceairport.dto.RegisterRequest;
import com.indytskyi.userserviceairport.exception.ApiValidationException;
import com.indytskyi.userserviceairport.exception.ErrorResponse;
import com.indytskyi.userserviceairport.mapper.PassengerDtoMapper;
import com.indytskyi.userserviceairport.model.Passenger;
import com.indytskyi.userserviceairport.model.User;
import com.indytskyi.userserviceairport.model.enums.Gender;
import com.indytskyi.userserviceairport.repository.PassengerRepository;
import com.indytskyi.userserviceairport.repository.UserRepository;
import com.indytskyi.userserviceairport.service.AccessService;
import com.indytskyi.userserviceairport.service.PassengerService;
import com.indytskyi.userserviceairport.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final UserService userService;
    private final PassengerDtoMapper passengerDtoMapper;
    private final AccessService accessService;

    @Override
    public List<PassengerResponseDto> getAllPassenger() {
        return passengerRepository.findAllBy()
                .orElseThrow(() -> new ApiValidationException(
                        List.of(new ErrorResponse("passengers", "No passengers yet"))
                ));
    }

    @Override
    @Transactional
    public PassengerResponseDto getPassengerByEmail(User user) {
        return passengerDtoMapper.toPassengerDto(user.getPassenger());
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

    @Override
    @Transactional
    public PassengerResponseDto updatePassengerByEmail(PassengerRequestDto requestDto, String bearerToken) {
        var user  = accessService.getUserFromTokenAfterValidation(bearerToken);
        var passenger = user.getPassenger();
        setUpdates(passenger, requestDto);
        return passengerDtoMapper.toPassengerDto(passenger);
    }

    private void setUpdates(Passenger passenger, PassengerRequestDto requestDto) {
        passenger.setGender(Gender.valueOf(requestDto.getGender()));
        passenger.setFirstName(requestDto.getFirstName());
        passenger.setLastName(requestDto.getLastName());
        passenger.setDataBirth(requestDto.getDateOfBirth());
        passenger.setPhoto(requestDto.getPhoto());
    }
}

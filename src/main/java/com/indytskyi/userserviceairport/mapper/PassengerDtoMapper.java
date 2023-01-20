package com.indytskyi.userserviceairport.mapper;

import com.indytskyi.userserviceairport.dto.PassengerResponseDto;
import com.indytskyi.userserviceairport.model.Passenger;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PassengerDtoMapper {
    PassengerResponseDto toPassengerDto(Passenger passenger);
}

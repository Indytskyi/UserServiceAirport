package com.indytskyi.userserviceairport.mapper;

import com.indytskyi.userserviceairport.dto.PassengerDto;
import com.indytskyi.userserviceairport.model.Passenger;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PassengerDtoMapper {
    PassengerDto toPassengerDto(Passenger passenger);
}

package com.indytskyi.userserviceairport.dto;

import com.indytskyi.userserviceairport.model.enums.Gender;
import lombok.Builder;

import java.util.Date;


@Builder
public record PassengerResponseDto(String firstName, String lastName, Date dataBirth, Gender gender) {

}

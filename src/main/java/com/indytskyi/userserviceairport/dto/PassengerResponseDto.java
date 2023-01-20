package com.indytskyi.userserviceairport.dto;

import com.indytskyi.userserviceairport.model.enums.Gender;

import java.util.Date;


public record PassengerResponseDto(String firstName, String lastName, Date dataBirth, Gender gender) {

}
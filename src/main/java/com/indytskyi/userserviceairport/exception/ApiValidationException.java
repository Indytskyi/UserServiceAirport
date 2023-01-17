package com.indytskyi.userserviceairport.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ApiValidationException extends RuntimeException {

    private List<ErrorResponse> errorResponses;

}
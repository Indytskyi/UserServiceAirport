package com.indytskyi.userserviceairport.service;

import com.indytskyi.userserviceairport.dto.RegisterRequestDto;
import com.indytskyi.userserviceairport.dto.RegisterResponseDto;

public interface RegistrationService {
    RegisterResponseDto register(RegisterRequestDto request);

    String confirmToken(String token);

    Object resendEmail(String email);
}

package com.indytskyi.userserviceairport.service;

import com.indytskyi.userserviceairport.dto.RegisterRequest;
import com.indytskyi.userserviceairport.dto.RegisterResponseDto;
import com.indytskyi.userserviceairport.model.User;

public interface RegistrationService {
    RegisterResponseDto register(RegisterRequest request);

    String confirmToken(String token);

    Object resendEmail(String email);
}

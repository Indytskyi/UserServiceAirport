package com.indytskyi.userserviceairport.service;

import com.indytskyi.userserviceairport.dto.AuthenticationRequest;
import com.indytskyi.userserviceairport.dto.AuthenticationResponse;
import com.indytskyi.userserviceairport.dto.RegisterRequest;
import com.indytskyi.userserviceairport.dto.RegisterResponseDto;
import com.indytskyi.userserviceairport.model.User;

public interface AuthenticationService {
    RegisterResponseDto register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    String confirmToken(String token);
}

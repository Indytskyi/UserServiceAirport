package com.indytskyi.userserviceairport.service;

import com.indytskyi.userserviceairport.dto.*;
import com.indytskyi.userserviceairport.model.User;

public interface AuthenticationService {
    RegisterResponseDto register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    String confirmToken(String token);

    Object resendEmail(String email);

    AuthenticationResponse refreshToken(RefreshTokenRequestDto refreshTokenRequestDto);
}

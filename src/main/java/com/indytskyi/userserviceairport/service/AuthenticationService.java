package com.indytskyi.userserviceairport.service;

import com.indytskyi.userserviceairport.dto.*;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequestDto request);

    AuthenticationResponse refreshToken(RefreshTokenRequestDto refreshTokenRequestDto);

    ValidateTokenResponseDto validateToken(String token);

    Object logout(String bearerToken);

}

package com.indytskyi.userserviceairport.service;

import com.indytskyi.userserviceairport.dto.*;
import com.indytskyi.userserviceairport.model.User;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse refreshToken(RefreshTokenRequestDto refreshTokenRequestDto);

    ValidateTokenResponseDto validateToken(String token);

    Object logout(String bearerToken);

}

package com.indytskyi.userserviceairport.service.impl;

import com.indytskyi.userserviceairport.dto.*;
import com.indytskyi.userserviceairport.email.BuildEmail;
import com.indytskyi.userserviceairport.email.EmailSender;
import com.indytskyi.userserviceairport.exception.ConfirmationTokenInvalidException;
import com.indytskyi.userserviceairport.exception.UserNotFoundException;
import com.indytskyi.userserviceairport.model.User;
import com.indytskyi.userserviceairport.model.token.ConfirmationToken;
import com.indytskyi.userserviceairport.security.jwt.JwtService;
import com.indytskyi.userserviceairport.service.*;
import com.indytskyi.userserviceairport.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        var user = userService.findByEmail(request.email());
        var jwtToken = jwtService.generateToken(Map.of("ROLE", user.getRole()), user);
        refreshTokenService.deleteOldRefreshTokens(user);
        var refreshToken = refreshTokenService.create(user);

        return new AuthenticationResponse(jwtToken, refreshToken.getToken());
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        var refreshToken = refreshTokenService.resolveRefreshToken(refreshTokenRequestDto.token());
        var user = refreshToken.getUser();
        var jwtToken = jwtService.generateToken(Map.of("ROLE", user.getRole()), user);
        return new AuthenticationResponse(jwtToken, refreshToken.getToken());
    }

    @Override
    public ValidateTokenResponseDto validateToken(String bearerToken) {
        var token = jwtService.resolveToken(bearerToken);
        jwtService.validateToken(token);
        var email = jwtService.getUserName(token);
        var user = userService.findByEmail(email);

        return ValidateTokenResponseDto.of()
                .userId(user.getId())
                .role(user.getRole().name())
                .build();
    }
}

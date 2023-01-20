package com.indytskyi.userserviceairport.service.impl;

import com.indytskyi.userserviceairport.dto.*;
import com.indytskyi.userserviceairport.repository.LogoutRepository;
import com.indytskyi.userserviceairport.security.jwt.JwtService;
import com.indytskyi.userserviceairport.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final LogoutRepository logoutRepository;

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

    @Override
    public Object logout(String bearerToken) {
        var token = jwtService.resolveToken(bearerToken);
        jwtService.validateToken(token);
        var email = jwtService.getUserName(token);
        var user = userService.findByEmail(email);
        if (!logoutRepository.isLoggedOut(token))
            logoutRepository.addUser(token, user.getEmail());

        refreshTokenService.deleteOldRefreshTokens(user);
        return Map.of("message", "Logoutz successful!");
    }
}

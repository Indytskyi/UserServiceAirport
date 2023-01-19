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

    private final PassengerService passengerService;
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final BuildEmail buildEmail;
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public RegisterResponseDto register(RegisterRequest request) {

        var passenger = passengerService.createPassenger(request);
        var user = userService.createUser(request, passenger);

        var linkToConfirmEmail = confirmationTokenService.createConfirmationToken(user);
        emailSender.send(request.getEmail(),
                buildEmail.buildEmail(request.getFirstName(), linkToConfirmEmail));

        return new RegisterResponseDto(
                ResponseMessage.REGISTER_SUCCESSFUL_MESSAGE.getData(),
                ResponseMessage.REGISTER_SECCESSFUL_URl.getData()
        );
    }

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

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableUser(confirmationToken.getUser().getEmail());
        return "confirmed";
    }

    @Transactional
    @Override
    public Object resendEmail(String email) {
        var user = userService.findByEmail(email);
        checkIfUserIsEnabled(user);
        confirmationTokenService.deleteOldConfirmationToken(user);
        var linkToConfirmEmail = confirmationTokenService.createConfirmationToken(user);
        emailSender.send(email,
                buildEmail.buildEmail(user.getPassenger().getFirstName(), linkToConfirmEmail));
        return "Nce";
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        var refreshToken = refreshTokenService.resolveRefreshToken(refreshTokenRequestDto.token());
        var user = refreshToken.getUser();
        var jwtToken = jwtService.generateToken(Map.of("ROLE", user.getRole()), user);
        return new AuthenticationResponse(jwtToken, refreshToken.getToken());
    }

    private void checkIfUserIsEnabled(User user) {
        if (user.isEnabled())
            throw new ConfirmationTokenInvalidException("Your email: " + user.getEmail() + " was already confirmed");
    }

}

package com.indytskyi.userserviceairport.service.impl;

import com.indytskyi.userserviceairport.dto.AuthenticationRequest;
import com.indytskyi.userserviceairport.dto.AuthenticationResponse;
import com.indytskyi.userserviceairport.dto.RegisterRequest;
import com.indytskyi.userserviceairport.dto.RegisterResponseDto;
import com.indytskyi.userserviceairport.email.BuildEmail;
import com.indytskyi.userserviceairport.email.EmailSender;
import com.indytskyi.userserviceairport.exception.ConfirmationTokenInvalidException;
import com.indytskyi.userserviceairport.exception.UserNotFoundException;
import com.indytskyi.userserviceairport.model.Passenger;
import com.indytskyi.userserviceairport.model.User;
import com.indytskyi.userserviceairport.model.enums.Gender;
import com.indytskyi.userserviceairport.model.enums.Role;
import com.indytskyi.userserviceairport.model.token.ConfirmationToken;
import com.indytskyi.userserviceairport.repository.UserRepository;
import com.indytskyi.userserviceairport.security.jwt.JwtService;
import com.indytskyi.userserviceairport.service.AuthenticationService;
import com.indytskyi.userserviceairport.service.ConfirmationTokenService;
import com.indytskyi.userserviceairport.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final BuildEmail buildEmail;

    @Override
    @Transactional
    public RegisterResponseDto register(RegisterRequest request) {
        var user = User.of()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(false)
                .role(Role.USER).build();
        var passenger = Passenger.of()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(Gender.valueOf(request.getGender()))
                .photo(request.getPhoto())
                .dataBirth(request.getDateOfBirth())
                .build();
        user.setPassenger(passenger);
        userRepository.save(user);

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
        var user = userRepository.findByEmail(request.email())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(Map.of("ROLE", user.getRole()), user);
        return new AuthenticationResponse(jwtToken);
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
        userRepository.enableUser(
                confirmationToken.getUser().getEmail());
        return "confirmed";
    }

    @Transactional
    @Override
    public Object resendEmail(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " doesn't exist"));
        checkIfUserIsEnabled(user);
        confirmationTokenService.deleteOldConfirmationToken(user);
        var linkToConfirmEmail = confirmationTokenService.createConfirmationToken(user);
        emailSender.send(email,
                buildEmail.buildEmail(user.getPassenger().getFirstName(), linkToConfirmEmail));
        return null;
    }

    private void checkIfUserIsEnabled(User user) {
        if (user.isEnabled())
            throw new ConfirmationTokenInvalidException("Your email: " + user.getEmail() + " was already confirmed");
    }

}

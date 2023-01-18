package com.indytskyi.userserviceairport.service.impl;

import com.indytskyi.userserviceairport.dto.AuthenticationRequest;
import com.indytskyi.userserviceairport.dto.AuthenticationResponse;
import com.indytskyi.userserviceairport.dto.RegisterRequest;
import com.indytskyi.userserviceairport.email.BuildEmail;
import com.indytskyi.userserviceairport.email.EmailSender;
import com.indytskyi.userserviceairport.exception.ApiValidationException;
import com.indytskyi.userserviceairport.exception.ErrorResponse;
import com.indytskyi.userserviceairport.model.Passenger;
import com.indytskyi.userserviceairport.model.User;
import com.indytskyi.userserviceairport.model.enums.Gender;
import com.indytskyi.userserviceairport.model.enums.Role;
import com.indytskyi.userserviceairport.model.token.ConfirmationToken;
import com.indytskyi.userserviceairport.repository.PassengerRepository;
import com.indytskyi.userserviceairport.repository.UserRepository;
import com.indytskyi.userserviceairport.security.jwt.JwtService;
import com.indytskyi.userserviceairport.service.AuthenticationService;
import com.indytskyi.userserviceairport.service.ConfirmationTokenService;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
    public AuthenticationResponse register(RegisterRequest request) {
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

        String token = UUID.randomUUID().toString();
        var confirmationToken = ConfirmationToken.of()
                .token(token)
                .localDateTime(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        String link = "http://localhost:8080/airport/user/register/confirm?token=" + token;
        emailSender.send(request.getEmail(),
                buildEmail.buildEmail(request.getFirstName(), link));

        log.info(token);

        return new AuthenticationResponse("HELLO");
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
        userRepository.enableAppUser(
                confirmationToken.getUser().getEmail());
        return "confirmed";
    }
}

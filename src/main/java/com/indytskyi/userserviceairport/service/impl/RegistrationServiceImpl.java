package com.indytskyi.userserviceairport.service.impl;

import com.indytskyi.userserviceairport.dto.RegisterRequestDto;
import com.indytskyi.userserviceairport.dto.RegisterResponseDto;
import com.indytskyi.userserviceairport.email.BuildEmail;
import com.indytskyi.userserviceairport.email.EmailSender;
import com.indytskyi.userserviceairport.exception.LimitedPermissionException;
import com.indytskyi.userserviceairport.model.User;
import com.indytskyi.userserviceairport.model.token.ConfirmationToken;
import com.indytskyi.userserviceairport.service.ConfirmationTokenService;
import com.indytskyi.userserviceairport.service.PassengerService;
import com.indytskyi.userserviceairport.service.RegistrationService;
import com.indytskyi.userserviceairport.service.UserService;
import com.indytskyi.userserviceairport.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {

    private final PassengerService passengerService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final BuildEmail buildEmail;
    private final UserService userService;


    @Override
    @Transactional
    public RegisterResponseDto register(RegisterRequestDto request) {
        userService.checkIfUserWithNewEmailExist(request.getEmail());
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
    @Transactional
    public Object resendEmail(String email) {
        var user = userService.findByEmail(email);
        checkIfUserIsEnabled(user);
        confirmationTokenService.deleteOldConfirmationToken(user.getConfirmationToken().getId());
        var linkToConfirmEmail = confirmationTokenService.createConfirmationToken(user);
        log.warn("DELETE");
        emailSender.send(email,
                buildEmail.buildEmail(user.getPassenger().getFirstName(), linkToConfirmEmail));
        return new RegisterResponseDto(
                ResponseMessage.RESEND_CONFIRMATION_TOKEN_MESSAGE.getData(),
                ResponseMessage.REGISTER_SECCESSFUL_URl.getData()
        );
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

    private void checkIfUserIsEnabled(User user) {
        if (user.isEnabled())
            throw new LimitedPermissionException("Your email: " + user.getEmail() + " was already confirmed");
    }
}

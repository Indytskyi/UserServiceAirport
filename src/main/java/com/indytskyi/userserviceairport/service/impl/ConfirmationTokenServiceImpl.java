package com.indytskyi.userserviceairport.service.impl;

import com.indytskyi.userserviceairport.model.User;
import com.indytskyi.userserviceairport.model.token.ConfirmationToken;
import com.indytskyi.userserviceairport.repository.ConfirmationTokenRepository;
import com.indytskyi.userserviceairport.service.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
    @Value("${LINK_TO_CONFIRM_REGISTRATION}")
    private String linkToConfirmRegistration;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Transactional
    public void saveConfirmationToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public void deleteOldConfirmationToken(Long confirmationId) {
         confirmationTokenRepository.deleteById(confirmationId);
    }

    @Override
    @Transactional
    public String createConfirmationToken(User user) {
        String token = UUID.randomUUID().toString();
        var confirmationToken = ConfirmationToken.of()
                .token(token)
                .localDateTime(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();

        confirmationTokenRepository.save(confirmationToken);
        return linkToConfirmRegistration + token;
    }


    @Transactional
    @Override
    public void setConfirmedAt(String token) {
         confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}

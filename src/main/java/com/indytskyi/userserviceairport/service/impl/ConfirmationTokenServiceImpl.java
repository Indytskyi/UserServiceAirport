package com.indytskyi.userserviceairport.service.impl;

import com.indytskyi.userserviceairport.model.token.ConfirmationToken;
import com.indytskyi.userserviceairport.repository.ConfirmationTokenRepository;
import com.indytskyi.userserviceairport.service.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Transactional
    public void saveConfirmationToken(ConfirmationToken confirmationToken) {
        confirmationTokenRepository.save(confirmationToken);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }


    @Transactional
    public void setConfirmedAt(String token) {
         confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}

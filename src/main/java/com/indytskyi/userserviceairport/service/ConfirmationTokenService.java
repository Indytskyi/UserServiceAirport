package com.indytskyi.userserviceairport.service;

import com.indytskyi.userserviceairport.model.token.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenService {
    void saveConfirmationToken(ConfirmationToken confirmationToken);
    int setConfirmedAt(String token);
    Optional<ConfirmationToken> getToken(String token);
}

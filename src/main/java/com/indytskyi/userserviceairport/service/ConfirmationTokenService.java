package com.indytskyi.userserviceairport.service;

import com.indytskyi.userserviceairport.model.User;
import com.indytskyi.userserviceairport.model.token.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenService {
    void saveConfirmationToken(ConfirmationToken confirmationToken);
    void setConfirmedAt(String token);
    Optional<ConfirmationToken> getToken(String token);
    void deleteOldConfirmationToken(User user);
    String createConfirmationToken(User user);
}

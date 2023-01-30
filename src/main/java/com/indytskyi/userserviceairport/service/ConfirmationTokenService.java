package com.indytskyi.userserviceairport.service;

import com.indytskyi.userserviceairport.model.User;
import com.indytskyi.userserviceairport.model.token.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenService {
    void setConfirmedAt(String token);
    Optional<ConfirmationToken> getToken(String token);
    void deleteOldConfirmationToken(Long confirmationId);
    String createConfirmationToken(User user);
}

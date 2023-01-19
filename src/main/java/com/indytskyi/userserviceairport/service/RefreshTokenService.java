package com.indytskyi.userserviceairport.service;

import com.indytskyi.userserviceairport.model.User;
import com.indytskyi.userserviceairport.model.token.RefreshToken;

import java.util.List;

public interface RefreshTokenService {
    RefreshToken create(User user);

    RefreshToken findByToken(String token);
    RefreshToken resolveRefreshToken(String token);

    void deleteOldRefreshTokens(User user);
}

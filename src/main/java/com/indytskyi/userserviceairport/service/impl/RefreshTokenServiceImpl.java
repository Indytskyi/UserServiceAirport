package com.indytskyi.userserviceairport.service.impl;

import com.indytskyi.userserviceairport.exception.RefreshTokenException;
import com.indytskyi.userserviceairport.model.User;
import com.indytskyi.userserviceairport.model.token.RefreshToken;
import com.indytskyi.userserviceairport.repository.RefreshTokenRepository;
import com.indytskyi.userserviceairport.service.RefreshTokenService;
import com.indytskyi.userserviceairport.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${jwt.refresh.token.expiredTime.min}")
    private long expirationPeriod;


    @Override
    @Transactional
    public RefreshToken create(User user) {
        var refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        String token = UUID.randomUUID().toString();
        while (refreshTokenRepository.findByToken(token).isPresent()) {
            token = UUID.randomUUID().toString();
        }
        refreshToken.setToken(token);
        refreshToken.setExpiredAt(LocalDateTime.now()
                .plusMinutes(expirationPeriod));
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenException(
                        "Refresh token: " + token + " wasn't found in a DB."
                ));
    }


    @Override
    public RefreshToken resolveRefreshToken(String token) {
        RefreshToken refreshToken = findByToken(token);
        if (refreshToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RefreshTokenException("Refresh token was expired. Please, make a new login.");
        }
        return refreshToken;
    }

    @Override
    public void deleteOldRefreshTokens(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}

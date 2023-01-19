package com.indytskyi.userserviceairport.repository;

import com.indytskyi.userserviceairport.model.User;
import com.indytskyi.userserviceairport.model.token.RefreshToken;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<List<RefreshToken>> findAllByUser(User user);

    Optional<RefreshToken> findByUserEmail(String email);

    Optional<RefreshToken> findByUser(User user);

    void deleteByUser(User user);
}
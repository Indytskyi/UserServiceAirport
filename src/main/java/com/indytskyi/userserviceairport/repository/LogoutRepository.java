package com.indytskyi.userserviceairport.repository;

public interface LogoutRepository {
    void addUser(String token, String email);

    boolean isLoggedOut(String token);
}

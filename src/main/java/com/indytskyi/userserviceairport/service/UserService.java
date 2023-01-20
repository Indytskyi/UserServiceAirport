package com.indytskyi.userserviceairport.service;

import com.indytskyi.userserviceairport.dto.RegisterRequest;
import com.indytskyi.userserviceairport.dto.UserDto;
import com.indytskyi.userserviceairport.model.Passenger;
import com.indytskyi.userserviceairport.model.User;

public interface UserService {

    User createUser(RegisterRequest request, Passenger passenger);
    User findByEmail(String email);
    void enableUser(String email);
    void deleteUser(String email);

    UserDto updateUser(UserDto userDto, String email);
}

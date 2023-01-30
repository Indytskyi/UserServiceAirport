package com.indytskyi.userserviceairport.service;

import com.indytskyi.userserviceairport.dto.RegisterRequestDto;
import com.indytskyi.userserviceairport.dto.UserDto;
import com.indytskyi.userserviceairport.model.Passenger;
import com.indytskyi.userserviceairport.model.User;

public interface UserService {

    User createUser(RegisterRequestDto request, Passenger passenger);

    User findByEmail(String email);
    User findById(Long id);

    void enableUser(String email);

    void deleteUser(String email);

    UserDto updateUser(UserDto userDto, String email);

    void checkIfUserWithNewEmailExist(String newEmail);

}

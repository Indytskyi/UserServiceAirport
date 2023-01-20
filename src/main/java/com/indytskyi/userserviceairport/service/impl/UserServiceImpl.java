package com.indytskyi.userserviceairport.service.impl;

import com.indytskyi.userserviceairport.dto.RegisterRequest;
import com.indytskyi.userserviceairport.dto.UserDto;
import com.indytskyi.userserviceairport.exception.UserDuplicateEmailException;
import com.indytskyi.userserviceairport.exception.UserNotFoundException;
import com.indytskyi.userserviceairport.model.Passenger;
import com.indytskyi.userserviceairport.model.User;
import com.indytskyi.userserviceairport.model.enums.Role;
import com.indytskyi.userserviceairport.repository.UserRepository;
import com.indytskyi.userserviceairport.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public User createUser(RegisterRequest request, Passenger passenger) {
        var user = User.of()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(false)
                .role(Role.USER).build();
        user.setPassenger(passenger);
        userRepository.save(user);
        return user;
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " doesn't exist"));
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User doesn't exist"));
    }

    @Override
    public void enableUser(String email) {
        userRepository.enableUser(email);
    }

    @Override
    @Transactional
    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, String email) {
        var user = findByEmail(email);
        if (!userDto.getEmail().equals(email)) {
            checkIfUserWithNewEmailExist(userDto.getEmail());
        }

        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        return userDto;
    }

    @Override
    @Transactional
    public void checkIfUserWithNewEmailExist(String newEmail) {
        if (userRepository.findByEmail(newEmail).isPresent()) {
            throw new UserDuplicateEmailException("A user with such email is already present");
        }
    }


}

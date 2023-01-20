package com.indytskyi.userserviceairport.controller;

import com.indytskyi.userserviceairport.dto.UserDto;
import com.indytskyi.userserviceairport.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/airport/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@RequestParam String email) {
        userService.deleteUser(email);
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @RequestParam String email) {
        return ResponseEntity.ok(userService.updateUser(userDto, email));
    }
}

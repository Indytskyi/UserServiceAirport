package com.indytskyi.userserviceairport.controller;

import com.indytskyi.userserviceairport.dto.UserDto;
import com.indytskyi.userserviceairport.service.AccessService;
import com.indytskyi.userserviceairport.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/airport/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AccessService accessService;

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@RequestHeader("Authorization") String bearerToken) {
        var  user = accessService.getUserFromTokenAfterValidation(bearerToken);
        userService.deleteUser(user.getEmail());
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto,
                                              @RequestHeader("Authorization") String bearerToken) {
        var  user = accessService.getUserFromTokenAfterValidation(bearerToken);
        return ResponseEntity.ok(userService.updateUser(userDto, user.getEmail()));
    }
}

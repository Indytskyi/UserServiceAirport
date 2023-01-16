package com.indytskyi.userserviceairport.controller;

import com.indytskyi.userserviceairport.dto.AuthenticationResponse;
import com.indytskyi.userserviceairport.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/airport")
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/tets")
    public ResponseEntity<String> register() {
        return ResponseEntity.ok("Hello my name is Artem");
    }
}

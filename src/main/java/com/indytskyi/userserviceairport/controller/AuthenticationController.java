package com.indytskyi.userserviceairport.controller;

import com.indytskyi.userserviceairport.dto.AuthenticationRequest;
import com.indytskyi.userserviceairport.dto.AuthenticationResponse;
import com.indytskyi.userserviceairport.dto.RegisterRequest;
import com.indytskyi.userserviceairport.dto.RegisterResponseDto;
import com.indytskyi.userserviceairport.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/airport/user/")
@RequiredArgsConstructor
public class AuthenticationController {
  private final AuthenticationService authenticationService;

  @PostMapping("/register")
  public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequest request) {
    return new ResponseEntity<>(authenticationService.register(request),
            HttpStatus.CREATED);
  }
  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request) {
    return ResponseEntity.ok(authenticationService.authenticate(request));
  }

  @GetMapping("/confirm")
  public ResponseEntity<String> confirm(@RequestParam("token") String token) {
    return ResponseEntity.ok(authenticationService.confirmToken(token));
  }

  @GetMapping("/resend-confirm-email")
  public ResponseEntity<Object> resendConfirmEmail(@RequestParam String email) {
    return ResponseEntity.ok(authenticationService.resendEmail(email));
  }
}

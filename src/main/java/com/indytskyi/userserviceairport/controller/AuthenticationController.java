package com.indytskyi.userserviceairport.controller;

import com.indytskyi.userserviceairport.dto.*;
import com.indytskyi.userserviceairport.service.AuthenticationService;
import com.indytskyi.userserviceairport.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
  private final RegistrationService registrationService;

  @Operation(
          summary = "Registration a new user",
          description = "Allows to register a new user. "
                  + "As response user gets a message that confirmation email was sent.",
          tags = {"Authentication"},
          responses = {
                  @ApiResponse(responseCode = "201", description = "Created"),
                  @ApiResponse(responseCode = "400", description = "Bad Request"),
                  @ApiResponse(responseCode = "409", description = "Conflict")
          }
  )
  @PostMapping("/register")
  public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto request) {
    return new ResponseEntity<>(registrationService.register(request),
            HttpStatus.CREATED);
  }
  @PostMapping("/login")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequestDto request) {
    return ResponseEntity.ok(authenticationService.authenticate(request));
  }

  @GetMapping("/confirm")
  public ResponseEntity<String> confirm(@RequestParam("token") String token) {
    return ResponseEntity.ok(registrationService.confirmToken(token));
  }

  @GetMapping("/resend-confirm-email")
  public ResponseEntity<Object> resendConfirmEmail(@RequestParam String email) {
    return ResponseEntity.ok(registrationService.resendEmail(email));
  }

  @GetMapping("/refresh-token")
  public ResponseEntity<AuthenticationResponse> refreshToken(
          @RequestBody RefreshTokenRequestDto refreshTokenRequestDto
  ) {
    return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequestDto));
  }

  @GetMapping("/validate-token")
  public ResponseEntity<ValidateTokenResponseDto> validateToken(
          @RequestHeader("Authorization") String bearerToken
  ) {
    return ResponseEntity.ok(authenticationService.validateToken(bearerToken));
  }

  @GetMapping ("/logout")
  public ResponseEntity<Object> logout(
          @RequestHeader("Authorization") String bearerToken) {
    return  ResponseEntity.ok(authenticationService.logout(bearerToken));
  }

}

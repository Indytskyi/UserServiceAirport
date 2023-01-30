package com.indytskyi.userserviceairport.controller;

import com.indytskyi.userserviceairport.dto.PassengerRequestDto;
import com.indytskyi.userserviceairport.dto.PassengerResponseDto;
import com.indytskyi.userserviceairport.service.AccessService;
import com.indytskyi.userserviceairport.service.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/airport/user/passenger")
@RequiredArgsConstructor
public class PassengerController {
    private final PassengerService passengerService;
    private final AccessService accessService;

    @GetMapping
    public ResponseEntity<Object> getAllPassengers(@RequestHeader("Authorization") String bearerToken) {
        accessService.checkByAdmin(bearerToken);
        return ResponseEntity.ok(passengerService.getAllPassenger());
    }

    @GetMapping("/info")
    public ResponseEntity<Object> getPassengerByEmail(
            @RequestParam(required = false, defaultValue = "ALL") String email,
            @RequestHeader("Authorization") String bearerToken) {
        var user = accessService.getUserFromTokenAfterValidation(bearerToken);
        return ResponseEntity.ok(passengerService.getPassengerByEmail(user));
    }

    @PutMapping
    public ResponseEntity<PassengerResponseDto> updatePassenger(
            @RequestBody @Valid PassengerRequestDto requestDto,
            @RequestHeader("Authorization") String bearerToken) {
        return ResponseEntity.ok(passengerService.updatePassengerByEmail(requestDto, bearerToken));
    }
}

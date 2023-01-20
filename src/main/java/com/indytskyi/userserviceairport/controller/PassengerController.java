package com.indytskyi.userserviceairport.controller;

import com.indytskyi.userserviceairport.dto.PassengerRequestDto;
import com.indytskyi.userserviceairport.dto.PassengerResponseDto;
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

    @GetMapping
    public ResponseEntity<Object> getPassengerAllOrByEmail(
            @RequestParam(required = false, defaultValue = "ALL") String email) {
        return ResponseEntity.ok(passengerService.getAllPassengerOrByEmail(email));
    }

    @PutMapping
    public ResponseEntity<PassengerResponseDto> updatePassenger(
            @RequestBody @Valid PassengerRequestDto requestDto,
            @RequestParam String email) {
        return ResponseEntity.ok(passengerService.updatePassengerByEmail(requestDto, email));
    }
}

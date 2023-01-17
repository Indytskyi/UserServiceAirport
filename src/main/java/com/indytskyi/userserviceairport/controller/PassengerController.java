package com.indytskyi.userserviceairport.controller;

import com.indytskyi.userserviceairport.dto.PassengerDto;
import com.indytskyi.userserviceairport.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airport/passenger")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @GetMapping
    public ResponseEntity<List<PassengerDto>> getAllPassenges() {
        return ResponseEntity.ok(passengerService.getAllPassenger());
    }

    @GetMapping("/{email}")
    public  ResponseEntity<PassengerDto> getPassenerByEmail(@PathVariable String email) {
        return  ResponseEntity.ok(passengerService.getPassengerByEmail(email));
    }
}
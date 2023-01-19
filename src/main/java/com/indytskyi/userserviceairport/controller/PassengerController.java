package com.indytskyi.userserviceairport.controller;

import com.indytskyi.userserviceairport.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/airport/passenger")
@RequiredArgsConstructor
public class PassengerController {
    private final PassengerService passengerService;

    @GetMapping
    public  ResponseEntity<Object> getPassenerAllOrByEmail(
            @RequestParam(required = false, defaultValue = "ALL") String email) {
        return  ResponseEntity.ok(passengerService.getAllPassengerOrByEmail(email));
    }
}

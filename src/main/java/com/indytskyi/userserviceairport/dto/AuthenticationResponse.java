package com.indytskyi.userserviceairport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public record AuthenticationResponse(String token, String refreshToken) {
}

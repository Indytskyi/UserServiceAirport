package com.indytskyi.userserviceairport.dto;

import lombok.Builder;

@Builder(toBuilder = true, builderMethodName = "of")
public record ValidateTokenResponseDto(Long userId, String role) {
}
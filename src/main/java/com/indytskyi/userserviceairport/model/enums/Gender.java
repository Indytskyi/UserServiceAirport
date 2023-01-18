package com.indytskyi.userserviceairport.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Gender {
    MALE("MALE"), FEMALE("FEMALE");
    private final String gender;
}

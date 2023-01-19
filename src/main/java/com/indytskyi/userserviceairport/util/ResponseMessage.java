package com.indytskyi.userserviceairport.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessage {
    REGISTER_SUCCESSFUL_MESSAGE(
            "Congratulations, you have successfully registered.\n" +
            "Follow the link to the email to confirm your registration"),
    REGISTER_SECCESSFUL_URl("http://0.0.0.0:1080");

    private final String data;
}

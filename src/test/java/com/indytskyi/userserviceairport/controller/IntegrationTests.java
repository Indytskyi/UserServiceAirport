package com.indytskyi.userserviceairport.controller;

import com.indytskyi.userserviceairport.dto.*;
import com.indytskyi.userserviceairport.model.enums.Gender;
import com.indytskyi.userserviceairport.model.token.ConfirmationToken;
import com.indytskyi.userserviceairport.model.token.RefreshToken;
import com.indytskyi.userserviceairport.repository.ConfirmationTokenRepository;
import com.indytskyi.userserviceairport.repository.PassengerRepository;
import com.indytskyi.userserviceairport.repository.RefreshTokenRepository;
import com.indytskyi.userserviceairport.repository.UserRepository;
import com.indytskyi.userserviceairport.util.ResponseMessage;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class IntegrationTests extends TestContainerConfig {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private UserRepository userRepository;

    private RegisterRequestDto registerRequestDto;
    private AuthenticationRequestDto authenticationRequestDto;

    @BeforeEach()
    void init() {
        registerRequestDto = RegisterRequestDto.builder()
                .email("artem2003@gmail.com")
                .password("Boom2003@")
                .firstName("Ivan")
                .lastName("Ivanov")
                .gender("MALE")
                .photo("photo.jpg")
                .dateOfBirth(new Date(2005, 6, 18))
                .build();
        authenticationRequestDto = new AuthenticationRequestDto(
                "artem2003@gmail.com",
                "Boom2003@");
    }

    @Test
    @SneakyThrows
    @Sql(value = "/remove_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void registerWithValidData() {
        //WHEN
        mockMvc.perform(post("/airport/user/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registerRequestDto)))
                .andExpect(status().isCreated());

        //THEN
        Assertions.assertTrue(userRepository.findByEmail(registerRequestDto.getEmail()).isPresent());
    }


    @Test
    @SneakyThrows
    void registerWithNotMatchPassword() {
        //GIVEN
        String expected = "Incorrect format of password";
        registerRequestDto.setPassword("password");

        //WHEN
        String actual = mockMvc.perform(post("/airport/user/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registerRequestDto)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //THEN
        Assertions.assertTrue(actual.contains(expected));
    }

    @Test
    @Sql(value = "/add_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/remove_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    void registerExistUser() {
        //GIVEN
        var expected = "A user with such email is already present";

        //WHEN + THEN
        mockMvc.perform(post("/airport/user/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registerRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(expected));
    }

    @Test
    @Sql(value = "/add_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/remove_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    void confirmEmailWithValidToken() {
        //GIVEN
        ConfirmationToken confirmationToken = confirmationTokenRepository
                .findByUserEmail(registerRequestDto.getEmail()).orElseThrow();

        //WHEN
        mockMvc.perform(get("/airport/user/confirm?token=" + confirmationToken.getToken()))
                .andExpect(status().isOk());

        //THEN
        Assertions.assertTrue(userRepository.findByEmail(registerRequestDto.getEmail()).get().isEnabled());
    }

    @Test
    @Sql(value = "/add_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/remove_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    void confirmEmailWithInValidToken() {
        //GIVEN
        String invalidToken = UUID.randomUUID().toString();

        //WHEN
        mockMvc.perform(get("/airport/user/confirm?token=" + invalidToken))
                .andExpect(status().isAccepted());

        //THEN
        Assertions.assertFalse(userRepository.findByEmail(registerRequestDto.getEmail()).get().isEnabled());
    }

    @Test
    @Sql(value = "/add_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/confirmation_token_set_experiedTime_invalid.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/remove_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    void confirmEmailWithExpiredToken() {
        //GIVEN
        String expected = "token expired";
        ConfirmationToken confirmationToken = confirmationTokenRepository
                .findByUserEmail(registerRequestDto.getEmail()).orElseThrow();
        //WHEN
        var actual = mockMvc.perform(get("/airport/user/confirm?token=" + confirmationToken.getToken()))
                .andExpect(status().isAccepted())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //THEN
        Assertions.assertTrue(actual.contains(expected));
    }

    @Test
    @Sql(value = "/add_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/remove_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    void resendConfirmEmailWithValidEmail() {
        //GIVEN
        String expected = ResponseMessage.RESEND_CONFIRMATION_TOKEN_MESSAGE.getData();

        //WHEN
        mockMvc.perform(get("/airport/user/resend-confirm-email?email=" + registerRequestDto.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(expected));

    }


    @Test
    @Sql(value = "/add_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/remove_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    void resendConfirmEmailWithInValidEmail() {
        //GIVEN
        String inccorectEmail = "artem2004@gmail.com";
        String expected = "User with email: " + inccorectEmail + " doesn't exist";

        //WHEN
        mockMvc.perform(get("/airport/user/resend-confirm-email?email=" + inccorectEmail))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(expected));

    }

    @Test
    @Sql(value = "/add_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/enable_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/remove_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    void resendConfirmEmailWithConfirmedEmail() {
        //GIVEN
        String expected = "Your email: " + registerRequestDto.getEmail() + " was already confirmed";

        //WHEN + THEN
        mockMvc.perform(get("/airport/user/resend-confirm-email?email=" + registerRequestDto.getEmail()))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.message").value(expected));

    }

    @Test
    @SneakyThrows
    @Sql(value = "/add_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/remove_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Login a person who is not enabled ")
    void loginToNotVonfirmedUser() {
        //GIVEN
        String expectedField = "Authentication";
        String expectedMessage = "Your email has not been verified";

        //WHEN + THEN
        mockMvc.perform(post("/airport/user/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authenticationRequestDto)))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.field").value(expectedField))
                .andExpect(jsonPath("$.message").value(expectedMessage));
    }

    @Test
    @SneakyThrows
    @DisplayName("Authorization of a user that does not exist ")
    void loginToNotRegisteredUser() {
        //GIVEN
        String expectedField = "Authentication";
        String expectedMessage = "Your email or password is incorrect";

        //WHEN + THEN
        mockMvc.perform(post("/airport/user/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authenticationRequestDto)))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.field").value(expectedField))
                .andExpect(jsonPath("$.message").value(expectedMessage));
    }

    @Test
    @Sql(value = "/add_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/enable_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/remove_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    @DisplayName("Login a person with: correct data + enabled")
    void loginWithCorrectUserAndEnabled() {
        //GIVEN

        //WHEN
        mockMvc.perform(post("/airport/user/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authenticationRequestDto)))
                .andExpect(status().isOk());
        //THEN
        Assertions.assertTrue(refreshTokenRepository.findByUserEmail(registerRequestDto.getEmail()).isPresent());
    }

    @Test
    @Sql(value = "/add_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/enable_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/remove_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    @DisplayName("get refresh token with correct token")
    void getRefreshTokenWithCorrectToken() {
        //GIVEN
        mockMvc.perform(post("/airport/user/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authenticationRequestDto)))
                .andExpect(status().isOk());

        RefreshToken refreshToken = refreshTokenRepository
                .findByUserEmail(registerRequestDto.getEmail())
                .orElseThrow();
        RefreshTokenRequestDto refreshTokenRequestDto = new RefreshTokenRequestDto(refreshToken.getToken());

        //WHEN
        mockMvc.perform(get("/airport/user/refresh-token")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(refreshTokenRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.refreshToken").value(refreshToken.getToken()));
    }

    @Test
    @Sql(value = "/add_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/enable_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/remove_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    @DisplayName("get refresh token with incorrect token")
    void getRefreshTokenWithIncorrectToken() {
        //GIVEN
        RefreshTokenRequestDto refreshTokenRequestDto = new RefreshTokenRequestDto(UUID.randomUUID().toString());
        String expeted = "Refresh token: " + refreshTokenRequestDto.token() + " wasn't found in a DB.";

        mockMvc.perform(post("/airport/user/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authenticationRequestDto)))
                .andExpect(status().isOk());

        //WHEN
        mockMvc.perform(get("/airport/user/refresh-token")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(refreshTokenRequestDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(expeted));
    }


    @Test
    @Sql(value = "/add_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/enable_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/remove_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    void validateCorrectToken() {
        //GIVEN
        String expectedEmail = "artem2003@gmail.com";
        String response = mockMvc.perform(post("/airport/user/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authenticationRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        AuthenticationResponse authenticationResponse = objectMapper.readValue(response, AuthenticationResponse.class);

        //WHEN
        var actualResponse = mockMvc.perform(get("/airport/user/validate-token")
                        .header("Authorization", "Bearer " + authenticationResponse.token())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ValidateTokenResponseDto actualValidateTokenResponseDto = objectMapper
                .readValue(actualResponse, ValidateTokenResponseDto.class);
        String actualEmail = userRepository.findById(actualValidateTokenResponseDto.userId()).get().getEmail();

        //THEN
        Assertions.assertEquals(expectedEmail, actualEmail);
    }

    @Test
    @Sql(value = "/add_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/enable_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/remove_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    void validateIncorrectToken() {
        //GIVEN
        String response = mockMvc.perform(post("/airport/user/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authenticationRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        AuthenticationResponse authenticationResponse = objectMapper.readValue(response, AuthenticationResponse.class);
        char[] chars = authenticationResponse.token().toCharArray();
        chars[1] = 'e';
        chars[0] = 'a';

        //WHEN
        mockMvc.perform(get("/airport/user/validate-token")
                        .header("Authorization", "Bearer" + Arrays.toString(chars))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(value = "/add_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/enable_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/remove_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    void logoutWithIncorrectToken() {
        //GIVEN
        String response = mockMvc.perform(post("/airport/user/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authenticationRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        AuthenticationResponse authenticationResponse = objectMapper.readValue(response, AuthenticationResponse.class);
        char[] chars = authenticationResponse.token().toCharArray();
        chars[1] = 'e';
        chars[0] = 'a';

        //WHEN
        mockMvc.perform(get("/airport/user/logout")
                        .header("Authorization", "Bearer" + Arrays.toString(chars))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @Sql(value = "/add_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/enable_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/remove_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    void logoutWithCorrectToken() {
        //GIVEN
        String expextedMessage = "Logoutz successful!";
        String response = mockMvc.perform(post("/airport/user/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authenticationRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        AuthenticationResponse authenticationResponse = objectMapper.readValue(response, AuthenticationResponse.class);

        //WHEN
        mockMvc.perform(get("/airport/user/logout")
                        .header("Authorization", "Bearer " + authenticationResponse.token())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(expextedMessage));
    }


    // USER CONTROLLER TESTS
    // DELETE AND UPFATE USERS


    @Test
    @Sql(value = "/add_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/enable_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @SneakyThrows
    void deleteUserWithCorrectToken() {
        //GIVEN
        String response = mockMvc.perform(post("/airport/user/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authenticationRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        AuthenticationResponse authenticationResponse = objectMapper.readValue(response, AuthenticationResponse.class);

        //WHEN
        mockMvc.perform(delete("/airport/user")
                        .header("Authorization", "Bearer " + authenticationResponse.token()))
                .andExpect(status().isNoContent());

        Assertions.assertFalse(userRepository.findByEmail(registerRequestDto.getEmail()).isPresent());
        Assertions.assertFalse(passengerRepository.findByUserEmail(registerRequestDto.getEmail()).isPresent());
        Assertions.assertFalse(confirmationTokenRepository.findByUserEmail(registerRequestDto.getEmail()).isPresent());
        Assertions.assertFalse(refreshTokenRepository.findByUserEmail(registerRequestDto.getEmail()).isPresent());
    }

    @Test
    @Sql(value = "/add_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/enable_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/remove_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    void updateUserWithCorrectToken() {
        //GIVEN
        UserDto userDto = UserDto.builder()
                .email("artem2003@gmail.com")
                .password("Boom2004@")
                .build();
        String response = mockMvc.perform(post("/airport/user/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authenticationRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        AuthenticationResponse authenticationResponse = objectMapper.readValue(response, AuthenticationResponse.class);

        //WHEN
        mockMvc.perform(put("/airport/user")
                        .header("Authorization", "Bearer " + authenticationResponse.token())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.password").value(userDto.getPassword()));

    }

    // PASSENGER CONTROLLER TESTS
    // 1) FIND ALL PASSENGERS
    // 2) FIND PASSENGER BY EMAIL
    // 3) UPDATE DATA OF PASSENGER


    @Test
    @Sql(value = "/add_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/add_user_admin.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/enable_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/remove_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = "/remove_user_admin.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    void findAllPassengersWithRoleAdmin() {
        //GIVEN
        authenticationRequestDto = new AuthenticationRequestDto(
                "artem2004@gmail.com",
                "Boom2003@"
        );
        String response = mockMvc.perform(post("/airport/user/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authenticationRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        AuthenticationResponse authenticationResponse = objectMapper.readValue(response, AuthenticationResponse.class);

        //WHEN
        mockMvc.perform(get("/airport/user/passenger")
                        .header("Authorization", "Bearer " + authenticationResponse.token()))
                .andExpect(status().isOk());

    }

    @Test
    @Sql(value = "/add_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/enable_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/remove_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    void findAllPassengersWithRoleUser() {
        //GIVEN
        String expected = "User don`t have access to this request";

        String response = mockMvc.perform(post("/airport/user/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authenticationRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        AuthenticationResponse authenticationResponse = objectMapper.readValue(response, AuthenticationResponse.class);

        //WHEN
        mockMvc.perform(get("/airport/user/passenger")
                        .header("Authorization", "Bearer " + authenticationResponse.token()))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message").value(expected));

    }

    @Test
    @Sql(value = "/add_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/enable_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/remove_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    void findPassengerByToken() {
        //GIVEN
        String response = mockMvc.perform(post("/airport/user/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authenticationRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        AuthenticationResponse authenticationResponse = objectMapper.readValue(response, AuthenticationResponse.class);

        //WHEN
        mockMvc.perform(get("/airport/user/passenger/info")
                        .header("Authorization", "Bearer " + authenticationResponse.token()))
                .andExpect(status().isOk());
    }

    /*
    {
    "firstName" : "Maks",
    "lastName" : "Stepanenko",
    "gender" : "FEMALE",
    "dateOfBirth" : "2003-07-13",
    "photo" : "photo.jpg"
}
     */

    @Test
    @Sql(value = "/add_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/enable_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/remove_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    void updatePassengerWithCorrectData() {
        //GIVEN
        PassengerRequestDto requestDto = PassengerRequestDto.builder()
                .firstName("Kate")
                .lastName("Petrova")
                .gender("FEMALE")
                .dateOfBirth(new Date(2005, 05, 05))
                .photo("photo2.png")
                .build();
        PassengerResponseDto expected = PassengerResponseDto.builder()
                .firstName("Kate")
                .lastName("Petrova")
                .gender(Gender.FEMALE)
                .dataBirth(new Date(2005, 05, 05))
                .build();
        String response = mockMvc.perform(post("/airport/user/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authenticationRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        AuthenticationResponse authenticationResponse = objectMapper.readValue(response, AuthenticationResponse.class);

        //WHEN
        String actual = mockMvc.perform(put("/airport/user/passenger")
                        .header("Authorization", "Bearer " + authenticationResponse.token())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PassengerResponseDto actualResponse = objectMapper.readValue(actual, PassengerResponseDto.class);

        Assertions.assertEquals(expected, actualResponse);
    }

    @Test
    @Sql(value = "/add_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/enable_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/remove_user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @SneakyThrows
    void updatePassengerWithInCcorectGEnder() {
        //GIVEN
        PassengerRequestDto requestDto = PassengerRequestDto.builder()
                .firstName("Kate")
                .lastName("Petrova")
                .gender("FEMAE")
                .dateOfBirth(new Date(2005, 05, 05))
                .photo("photo2.png")
                .build();

        String response = mockMvc.perform(post("/airport/user/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authenticationRequestDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        AuthenticationResponse authenticationResponse = objectMapper.readValue(response, AuthenticationResponse.class);

        //WHEN
        mockMvc.perform(put("/airport/user/passenger")
                        .header("Authorization", "Bearer " + authenticationResponse.token())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }
}
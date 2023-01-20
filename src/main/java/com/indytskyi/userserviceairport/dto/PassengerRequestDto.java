package com.indytskyi.userserviceairport.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PassengerRequestDto {
    @Size(min = 3, message = "Input correct firstName")
    private String firstName;
    @Size(min = 3, message = "Input correct lastName")
    private String lastName;

    @Pattern(regexp = "MALE|FEMALE")
    private String gender;
    private Date dateOfBirth;
    private String photo;
}

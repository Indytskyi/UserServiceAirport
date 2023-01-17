package com.indytskyi.userserviceairport.dto;

import com.indytskyi.userserviceairport.model.enums.Gender;
import jakarta.validation.constraints.Email;
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
public class RegisterRequest {
  @Email
  private String email;
  @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
          message = "Incorrect format of password")
  private String password;
  @Size(min = 3, message = "Input correct firstName")
  private String firstName;
  @Size(min = 3, message = "Input correct lastName")
  private String lastName;
  private Gender gender;
  private Date dateOfBirth;
  private String photo;


}
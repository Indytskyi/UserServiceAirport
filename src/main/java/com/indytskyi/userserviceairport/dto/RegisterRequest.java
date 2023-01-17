package com.indytskyi.userserviceairport.dto;

import com.indytskyi.userserviceairport.model.enums.Gender;
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
  private String email;
  private String password;
  private String firstName;
  private String lastName;
  private Gender gender;
  private Date dateOfBirth;
  private String photo;


}
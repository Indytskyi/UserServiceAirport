package com.indytskyi.userserviceairport.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Incorrect format of password")
    private String password;

    @OneToOne(mappedBy = "userEntity")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private PassengerEntity passengerEntity;

    public void setPassengerEntity(PassengerEntity passengerEntity) {
        this.passengerEntity = passengerEntity;
        passengerEntity.setUserEntity(this);
    }
}

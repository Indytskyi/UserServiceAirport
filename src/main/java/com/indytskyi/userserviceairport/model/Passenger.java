package com.indytskyi.userserviceairport.model;

import com.indytskyi.userserviceairport.model.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "passenger")
public class Passenger {
    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @NotNull(message = "Input correct Name")
    @Size(min = 10, message = "Input correct name")
    private String name;

    @NotNull(message = "Input correct Name")
    @Size(min = 10, message = "Input correct surname")
    private String username;

    @Temporal(TemporalType.DATE)
    private Date dataBirth;

    @Enumerated
    private Gender gender;

    private String photo;

}
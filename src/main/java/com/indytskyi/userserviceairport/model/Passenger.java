package com.indytskyi.userserviceairport.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.indytskyi.userserviceairport.model.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Builder(toBuilder = true, builderMethodName = "of")
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "passenger")
@ToString(exclude = "user")
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull(message = "Input correct Name")
    @Size(min = 3, message = "Input correct firstName")
    private String firstName;

    @NotNull(message = "Input correct Name")
    @Size(min = 3, message = "Input correct lastName")
    private String lastName;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dataBirth;

    @Enumerated
    private Gender gender;

    private String photo;

}

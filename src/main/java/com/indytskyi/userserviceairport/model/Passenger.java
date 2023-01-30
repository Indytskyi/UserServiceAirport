package com.indytskyi.userserviceairport.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.indytskyi.userserviceairport.model.enums.Gender;
import jakarta.persistence.*;
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
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
    private String firstName;
    private String lastName;
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dataBirth;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String photo;
}

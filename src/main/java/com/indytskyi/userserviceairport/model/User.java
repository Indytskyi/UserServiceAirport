package com.indytskyi.userserviceairport.model;

import com.indytskyi.userserviceairport.model.enums.Role;
import com.indytskyi.userserviceairport.model.token.ConfirmationToken;
import com.indytskyi.userserviceairport.model.token.RefreshToken;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@Builder(toBuilder = true, builderMethodName = "of")
@NoArgsConstructor
@ToString(exclude = "passenger")
@Table(name = "users")
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled;
    private boolean locked;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Passenger passenger;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private ConfirmationToken confirmationToken;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private RefreshToken refreshToken;

    public void setPassenger(Passenger passenger) {
        passenger.setUser(this);
        this.passenger = passenger;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true ;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}

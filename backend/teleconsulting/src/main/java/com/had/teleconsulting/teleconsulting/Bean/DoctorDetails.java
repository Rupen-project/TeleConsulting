package com.had.teleconsulting.teleconsulting.Bean;

import lombok.*;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Table(name="doctorDetails")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class DoctorDetails implements UserDetails {
    //isame queueID nahi rahega kyuki many to one he queue side se
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable=false,name="doctorID")
    Long doctorID;

    @Column(nullable=false,name="doctorFirstName")
    String doctorFirstName;

    @Column(nullable=true,name="doctorLastName")
    String doctorLastName;

    @Column(nullable=false,name="doctorPassword")
    String doctorPassword;

    @Column(nullable=false,name="doctorSpecialisation")
    String doctorSpecialisation;

    @Column(nullable=false,name="doctorQueueSize")
    int doctorQueueSize;

    @Column(nullable=false,name="doctorAvailable")
    int doctorAvailable;

    @Column(nullable=false,name="doctorEmail")
    String doctorEmail;

    @Column(nullable=false,name="doctorMobileNumber")
    String doctorMobileNumber;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return getDoctorPassword();
    }

    @Override
    public String getUsername() {
        return getDoctorEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

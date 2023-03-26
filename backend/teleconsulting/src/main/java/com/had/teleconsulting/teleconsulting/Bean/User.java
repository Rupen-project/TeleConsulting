package com.had.teleconsulting.teleconsulting.Bean;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Table(name="User")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable=false,name="userID")
    Long userID;

    @Column(nullable=false,name="userEmail")
    String userEmail;

    @Column(nullable=false,name="userMobileNumber")
    String userMobileNumber;

    @Column(nullable = false,name = "userPassword")
    String userPassword;
}

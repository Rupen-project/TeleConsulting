package com.had.teleconsulting.teleconsulting.Bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="User")
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable=false,name="userID")
    int userID;

    @Column(nullable=false,name="userEmail")
    String userEmail;

    @Column(nullable=false,name="userMobileNumber")
    String userMobileNumber;

    @OneToOne
    @JoinColumn(name = "patientID")
    PatientDetails patientDetails;

}

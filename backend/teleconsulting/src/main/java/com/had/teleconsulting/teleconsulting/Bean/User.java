package com.had.teleconsulting.teleconsulting.Bean;

import lombok.*;

import javax.persistence.*;

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
    int userID;

    @Column(nullable=false,name="userEmail")
    String userEmail;

    @Column(nullable=false,name="userMobileNumber")
    String userMobileNumber;

    @OneToOne
    @JoinColumn(name = "patientID")
    PatientDetails patientDetails;

}

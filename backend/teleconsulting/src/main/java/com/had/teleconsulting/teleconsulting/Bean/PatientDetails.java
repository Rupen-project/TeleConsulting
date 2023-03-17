package com.had.teleconsulting.teleconsulting.Bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="patientDetails")
@NoArgsConstructor
@Getter
@Setter
public class PatientDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable=false,name="patientID")
    int patientID;

    @Column(nullable=false,name="patientFirstName")
    String patientFirstName;

    @Column(nullable=true,name="patientLastName")
    String patientLastName;

    @Column(nullable=false,name="patientMobileNumber")
    String  patientMobileNumber;

    @Column(nullable=false,name="patientEmail")
    String patientEmail;

    @Column(nullable=false,name="patientDOB")
    String patientDOB;

    @Column(nullable=false,name="patientGender")
    String patientGender;

    @Column(nullable=false,name="patientPassword")
    String patientPassword;
}

package com.had.teleconsulting.teleconsulting.Payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@NoArgsConstructor
@Getter
@Setter

public class PatientDTO {

    Long patientID;
    String patientFirstName;
    String patientLastName;
    String  patientMobileNumber;
    String patientEmail;
    String patientDOB;
    String patientGender;
    UserDTO user;

}

package com.had.teleconsulting.teleconsulting.Payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Setter

public class PatientDTO {

    int patientID;
    String patientFirstName;
    String patientLastName;
    String  patientMobileNumber;
    String patientEmail;
    String patientDOB;
    String patientGender;
    String patientPassword;

}

package com.had.teleconsulting.teleconsulting.Payloads;

import com.had.teleconsulting.teleconsulting.Bean.User;
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

    Long patientID;
    String patientFirstName;
    String patientLastName;
    String  patientMobileNumber;
    String patientEmail;
    String patientDOB;
    String patientGender;
    UserDTO user;

}

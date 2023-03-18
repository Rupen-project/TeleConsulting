package com.had.teleconsulting.teleconsulting.Payloads;

import com.had.teleconsulting.teleconsulting.Bean.PatientDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    int userID;
    String userEmail;
    String userMobileNumber;
    PatientDetails patientDetails;
}

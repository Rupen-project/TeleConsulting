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

public class DoctorDTO {

    Long doctorID;
    String doctorFirstName;
    String doctorLastName;
    String doctorPassword;
    String doctorSpecialisation;
    int doctorQueueSize;
    int doctorAvailable;
    String doctorEmail;
    String doctorMobileNumber;

}

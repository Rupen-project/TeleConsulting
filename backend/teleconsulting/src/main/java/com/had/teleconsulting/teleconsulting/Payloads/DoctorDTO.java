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

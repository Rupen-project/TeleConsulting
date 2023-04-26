package com.had.teleconsulting.teleconsulting.Payloads;

import com.had.teleconsulting.teleconsulting.Bean.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class AppointmentDTO {

    Long appointmentID;
    String appointmentOpdType;
    String appointmentDate;
    Prescription prescription;
    FollowUP followUP;
    PatientDetails patientDetails;
    DoctorDetails doctorDetails;
    Queue queue;
    String isFollowUp;
    int followUpDay;
}

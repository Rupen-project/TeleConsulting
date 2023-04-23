package com.had.teleconsulting.teleconsulting.Bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import jakarta.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Builder
@Table(name="Appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,name = "appointmentID")
    Long appointmentID;

    @Column(nullable = false,name = "appointmentOpdType")
    String appointmentOpdType;

    @Column(nullable = false,name = "appointmentDate")
    String appointmentDate;

    @OneToOne
    @JoinColumn(name = "prescriptionID",referencedColumnName = "prescriptionID")
    Prescription prescription;

    @Column(name = "isFollowUp",nullable = true)
    String isFollowUp;

    @Column(name = "followUpDay",nullable = true)
    int followUpDay;

    @ManyToOne
    @JoinColumn(name = "patientID" )
    PatientDetails patientDetails;

    @ManyToOne
    @JoinColumn(name = "doctorID")
    DoctorDetails doctorDetails;

    @OneToOne
    @JoinColumn(name = "folloUpID")
    FollowUP followUP;

    @OneToOne
    @JoinColumn(name = "queueID")
    Queue queue;

}

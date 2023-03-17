package com.had.teleconsulting.teleconsulting.Bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="Appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,name = "appointmentID")
    int appointmentID;

    @Column(nullable = false,name = "appointmentOpdType")
    String appointmentOpdType;

    @Column(nullable = false,name = "appointmentDate")
    Date appointmentDate;

    @OneToOne
    @JoinColumn(name = "prescriptionID", nullable = false)
    Prescription prescription;

    @OneToOne
    @JoinColumn(name = "folloUpID")
    FollowUP followUP;

    @ManyToOne
    @JoinColumn(name = "patientID" ,nullable = false)
    PatientDetails patientDetails;

    @ManyToOne
    @JoinColumn(name = "doctorID")
    DoctorDetails doctorDetails;

    @OneToOne
    @JoinColumn(name = "queueID")
    Queue queue;


}

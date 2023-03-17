package com.had.teleconsulting.teleconsulting.Bean;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Queue")
public class Queue {
    // appointmentID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,name = "queueID")
    int queueID;

    @ManyToOne
    @JoinColumn(name = "doctorID",nullable = false)
    DoctorDetails doctorDetails;
}

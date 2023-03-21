package com.had.teleconsulting.teleconsulting.Bean;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Queue")
@AllArgsConstructor
@Builder
public class Queue {
    // appointmentID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,name = "queueID")
    long queueID;

    @ManyToOne
    @JoinColumn(name = "doctorID",nullable = false)
    DoctorDetails doctorDetails;
}

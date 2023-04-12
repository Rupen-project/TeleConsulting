package com.had.teleconsulting.teleconsulting.Bean;


//import jakarta.validation.constraints.Null;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Queue")
@AllArgsConstructor
@Builder
public class Queue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,name = "queueID")
    Long queueID;

    @ManyToOne
    @JoinColumn(name = "doctorID",nullable = false)
    DoctorDetails doctorDetails;

}

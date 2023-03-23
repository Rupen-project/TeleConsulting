package com.had.teleconsulting.teleconsulting.Bean;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "Prescription")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prescription {
    // appointment ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,name = "prescriptionID")
    Long prescriptionID;

//    @Column(nullable = false,name = "symptoms")
//    String symptoms;

    @Column(nullable = false,name = "ePrescription")
    String ePrescription;

}

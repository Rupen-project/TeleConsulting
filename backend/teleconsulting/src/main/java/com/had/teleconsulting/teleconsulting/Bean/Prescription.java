package com.had.teleconsulting.teleconsulting.Bean;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    @Column(nullable = false,name = "symptoms")
    String symptoms;

    @Column(nullable = false,name = "medicinesAndDosage")
    String medicinesAndDosage;

    @Column(nullable = false,name = "advice")
    String advice;

    @Column(nullable = true,name = "ePrescription")
    String ePrescription;

    @Column(name = "prescriptionUploadDate" ,nullable = true)
    String prescriptionUploadDate;


    public Prescription(String ePrescription, String prescriptionUploadDate) {
        this.ePrescription = ePrescription;
        this.prescriptionUploadDate = prescriptionUploadDate;
    }
}

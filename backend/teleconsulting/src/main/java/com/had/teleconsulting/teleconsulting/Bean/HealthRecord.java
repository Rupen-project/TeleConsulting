package com.had.teleconsulting.teleconsulting.Bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "HealthRecord")
public class HealthRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false , name = "healthRecordID")
    Long healthRecordID;

    @Column(name = "healthRecordURl" ,nullable = false)
    String healthRecordURL;

    @ManyToOne
    @JoinColumn(name = "patientId" , referencedColumnName = "patientID")
    PatientDetails patientDetails;
}
package com.had.teleconsulting.teleconsulting.Bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import org.springframework.context.annotation.Bean;

import java.util.Date;


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

    @Column(name = "healthRecordName" ,nullable = false)
    String healthRecordName;

    @Column(name = "healthRecordUploadDate" ,nullable = false)
    String healthRecordUploadDate;

    @ManyToOne
    @JoinColumn(name = "patientId" , referencedColumnName = "patientID")
    PatientDetails patientDetails;

    public HealthRecord(String healthRecordName, String healthRecordUploadDate) {
        this.healthRecordName = healthRecordName;
        this.healthRecordUploadDate = healthRecordUploadDate;
    }
}

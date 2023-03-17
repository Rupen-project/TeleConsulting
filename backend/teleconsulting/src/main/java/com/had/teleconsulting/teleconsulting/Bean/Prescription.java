package com.had.teleconsulting.teleconsulting.Bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "Prescription")
@NoArgsConstructor
public class Prescription {
    // appointment ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,name = "prescriptionID")
    int prescriptionID;

//    @Column(nullable = false,name = "symptoms")
//    String symptoms;

    @Column(nullable = false,name = "ePrescription")
    String ePrescription;

}

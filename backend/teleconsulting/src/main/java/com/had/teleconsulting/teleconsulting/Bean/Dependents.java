package com.had.teleconsulting.teleconsulting.Bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="Dependent")
@NoArgsConstructor
@Getter
@Setter
public class Dependents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,name="dependentID")
    int dependentID;

    @ManyToOne
    @JoinColumn(name = "userID",nullable = false)
    User user;

    @OneToOne
    @JoinColumn(name = "patientID",nullable = false)
    PatientDetails patientDetails;

}

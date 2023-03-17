package com.had.teleconsulting.teleconsulting.Bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="doctorDetails")
@NoArgsConstructor
@Getter
@Setter
public class DoctorDetails {
    //isame queueID nahi rahega kyuki many to one he queue side se
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable=false,name="doctorID")
    int doctorID;

    @Column(nullable=false,name="doctorFirstName")
    String doctorFirstName;

    @Column(nullable=true,name="doctorLastName")
    String doctorLastName;

    @Column(nullable=true,name="doctorPassword")
    String doctorPassword;

    @Column(nullable=true,name="doctorSpecialisation")
    String doctorSpecialisation;

    @Column(nullable=true,name="doctorQueueSize")
    int doctorQueueSize;

    @Column(nullable=true,name="doctorAvailable")
    Boolean doctorAvailable;

    @Column(nullable=true,name="doctorEmail")
    String doctorEmail;

    @Column(nullable=true,name="doctorMobileNumber")
    Number doctorMobileNumber;
}

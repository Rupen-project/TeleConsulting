package com.had.teleconsulting.teleconsulting.Bean;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="doctorDetails")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
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

    @Column(nullable=false,name="doctorPassword")
    String doctorPassword;

    @Column(nullable=false,name="doctorSpecialisation")
    String doctorSpecialisation;

    @Column(nullable=false,name="doctorQueueSize")
    int doctorQueueSize;

    @Column(nullable=false,name="doctorAvailable")
    int doctorAvailable;

    @Column(nullable=false,name="doctorEmail")
    String doctorEmail;

    @Column(nullable=false,name="doctorMobileNumber")
    String doctorMobileNumber;
}

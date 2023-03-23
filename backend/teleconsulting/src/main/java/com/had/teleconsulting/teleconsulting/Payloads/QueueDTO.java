package com.had.teleconsulting.teleconsulting.Payloads;

import com.had.teleconsulting.teleconsulting.Bean.DoctorDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@NoArgsConstructor
@Getter
@Setter
public class QueueDTO {

    Long queueID;
    DoctorDetails doctorDetails;
}

package com.had.teleconsulting.teleconsulting.Payloads;

import com.had.teleconsulting.teleconsulting.Bean.DoctorDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
public class QueueDTO {

    int queueID;
    DoctorDetails doctorDetails;
}

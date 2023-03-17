package com.had.teleconsulting.teleconsulting.Payloads;

import com.had.teleconsulting.teleconsulting.Bean.PatientDetails;
import com.had.teleconsulting.teleconsulting.Bean.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
public class DependentsDTO {

    int dependentID;
    User user;
    PatientDetails patientDetails;
}

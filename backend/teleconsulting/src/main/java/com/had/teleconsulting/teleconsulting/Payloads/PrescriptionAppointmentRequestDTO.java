package com.had.teleconsulting.teleconsulting.Payloads;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.had.teleconsulting.teleconsulting.Bean.Appointment;
import com.had.teleconsulting.teleconsulting.Bean.Prescription;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
@JsonDeserialize(using = PrescriptionAppointmentRequestDeserializer.class) // Custom deserializer is been used here
public class PrescriptionAppointmentRequestDTO {

    Prescription prescription;
    Appointment appointment;

}

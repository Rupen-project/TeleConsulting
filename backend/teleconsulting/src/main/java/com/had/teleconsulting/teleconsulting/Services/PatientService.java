package com.had.teleconsulting.teleconsulting.Services;

import com.had.teleconsulting.teleconsulting.Bean.Appointment;
import com.had.teleconsulting.teleconsulting.Exception.DoctorNotFoundException;
import com.had.teleconsulting.teleconsulting.Exception.PatientNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.AppointmentDTO;
import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
import com.had.teleconsulting.teleconsulting.Payloads.PatientDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface PatientService {

    PatientDTO createPatient(PatientDTO patient);
    Boolean getPatientByMobileNumber(String patientMobileNumber);
    PatientDTO updatePatient(PatientDTO patient, Integer patientID);
    PatientDTO getPatientByID(Long patientID) throws PatientNotFoundException;
    List<PatientDTO> getAllPatient();

    ArrayList<String> getSpecialisation();

    List<DoctorDTO> getAvailableDoctorsBySpecialisation(String category) throws DoctorNotFoundException;

    AppointmentDTO createAppointment(Map<String, Object> json);

    AppointmentDTO onCallDisconnect(AppointmentDTO appointmentDTO);
}
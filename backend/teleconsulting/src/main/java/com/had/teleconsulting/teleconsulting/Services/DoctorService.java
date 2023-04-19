package com.had.teleconsulting.teleconsulting.Services;

import com.had.teleconsulting.teleconsulting.Bean.Appointment;
import com.had.teleconsulting.teleconsulting.Bean.LoginModel;
import com.had.teleconsulting.teleconsulting.Bean.Prescription;
import com.had.teleconsulting.teleconsulting.Exception.DoctorNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.AppointmentDTO;
import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
import com.had.teleconsulting.teleconsulting.Payloads.PrescriptionDTO;
import com.had.teleconsulting.teleconsulting.Payloads.UserDTO;

import com.had.teleconsulting.teleconsulting.Exception.PatientNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.had.teleconsulting.teleconsulting.Payloads.UserDTO;
public interface DoctorService {
    DoctorDTO createDoctor(DoctorDTO doctorDTO);

    DoctorDTO loginDoctor(LoginModel loginModel) throws DoctorNotFoundException;

    DoctorDTO registerDoctor(DoctorDTO doctorDTO);

    PrescriptionDTO createPrescription(Map<String, Object> prescDetails);

    List<AppointmentDTO> getDoctorsAppointments(Long doctorID);

    String uploadPrescription(Prescription prescription, Long patientID,Long appointmentID, Appointment appointment) throws IOException;

    PatientDTO getPatientByID(Long patientID) throws PatientNotFoundException;

}

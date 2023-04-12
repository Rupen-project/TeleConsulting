package com.had.teleconsulting.teleconsulting.Services;

import com.had.teleconsulting.teleconsulting.Bean.Appointment;
import com.had.teleconsulting.teleconsulting.Bean.Appointment;
import com.had.teleconsulting.teleconsulting.Exception.DoctorNotFoundException;
import com.had.teleconsulting.teleconsulting.Exception.PatientNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.AppointmentDTO;
import com.had.teleconsulting.teleconsulting.Exception.DoctorNotFoundException;
import com.had.teleconsulting.teleconsulting.Exception.PatientNotFoundException;
import com.had.teleconsulting.teleconsulting.Payloads.AppointmentDTO;
import com.had.teleconsulting.teleconsulting.Payloads.DoctorDTO;
import com.had.teleconsulting.teleconsulting.Payloads.PatientDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    List<PatientDTO> getAllPatientOfGivenUserId(Long userId);

    List<AppointmentDTO> getAppointmentHistory(Long patientId);

    String uploadHealthRecords(MultipartFile record) throws IOException;

    byte[] getPrescription(String patientHealthRecordName) throws IOException;

    String deleteHealthRecord(String healthRecordName);

}